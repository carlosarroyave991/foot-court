package com.foorcourt.domain.usecase;

import com.foorcourt.domain.api.IDishServicePort;
import com.foorcourt.domain.api.IOrderServicePort;
import com.foorcourt.domain.api.IRestaurantServicePort;
import com.foorcourt.domain.exception.BusinessException;
import com.foorcourt.domain.exception.NotFoundException;
import com.foorcourt.domain.exception.ValidationException;
import com.foorcourt.domain.model.DishModel;
import com.foorcourt.domain.model.OrderModel;
import com.foorcourt.domain.model.RestaurantModel;
import com.foorcourt.domain.model.feignclient.SmsNotificationModel;
import com.foorcourt.domain.model.enums.StatusesOrder;
import com.foorcourt.domain.model.feignclient.TraceabilityModel;
import com.foorcourt.domain.model.feignclient.UserModel;
import com.foorcourt.domain.model.simplemodel.OrderDishSimpleModel;
import com.foorcourt.domain.spi.IOrderPersistencePort;
import com.foorcourt.domain.spi.ISmsFeignClientPort;
import com.foorcourt.domain.spi.ITraceabilityFeignClientPort;
import com.foorcourt.domain.spi.IUserFeignClientPort;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.Random;

import static com.foorcourt.domain.exception.error.CommonErrorCode.*;
import static com.foorcourt.domain.util.Const.MESSAGE_STATUS_READY;

/**
 * Clase usada para implementar la logica de negocio sobre cada funcion
 */
@RequiredArgsConstructor
@Transactional
public class OrderUseCase implements IOrderServicePort {
    private final IDishServicePort iDishServicePort;
    private final IRestaurantServicePort iRestaurantServicePort;
    private final IUserFeignClientPort iUserFeignClientPort;
    private final IOrderPersistencePort iOrderPersistencePort;
    private final ISmsFeignClientPort iSmsFeignClientPort;
    private final ITraceabilityFeignClientPort iTraceabilityFeignClientPort;

    @Override
    public OrderModel createOrder(OrderModel model) {
        // valido restaurante
        Optional<RestaurantModel> restaurantModel = iRestaurantServicePort.findById(model.getRestaurant().getId());
        if (restaurantModel.isEmpty()) throw new BusinessException(RESTAURANT_NOT_FOUND);

        // valido el cliente
        Optional<UserModel> userModel = Optional.ofNullable(iUserFeignClientPort.getUserById(model.getClientId()));
        if (userModel.isEmpty()) throw new BusinessException(USER_NOT_FOUND);

        // valido que el cliente no tenga pedidos en proceso (excluye CANCELADO y ENTREGADO)
        List<String> statusesInProcess = Arrays.asList(
                StatusesOrder.PENDIENTE.name(),
                StatusesOrder.EN_PREPARACION.name(),
                StatusesOrder.LISTO.name()
        );
        List<OrderModel> ordersInProcess = iOrderPersistencePort.findOrdersByClientIdAndStatus(
                model.getClientId(), statusesInProcess);
        if (!ordersInProcess.isEmpty()) throw new BusinessException(CLIENT_ALREADY_HAS_AN_ORDER);


        // valido que tenga platos
        if (model.getOrdersDishes() == null || model.getOrdersDishes().isEmpty()) {
            throw new ValidationException(ORDER_MUST_HAVENT_DISH);
        }

        // valido cada plato y que pertenezca al restaurante
        for (OrderDishSimpleModel orderDish : model.getOrdersDishes()) {
            if (orderDish.getId() == null) throw new ValidationException(INVALID_ID);
            if (orderDish.getQuantity() == null || orderDish.getQuantity() <= 0) throw new ValidationException(INVALID_QUANTITY);

            // verifico que el plato exista y pertenezca al restaurante
            Optional<DishModel> dishModel = iDishServicePort.findById(orderDish.getId());
            if (dishModel.isEmpty()) throw new NotFoundException(INVALID_ID);
            if (!dishModel.get().getRestaurant().getId().equals(model.getRestaurant().getId())) throw new ValidationException(RESTAURANT_NOT_FOUND);
            if (!dishModel.get().getActive()) throw new ValidationException(DISH_DISABLE);

            // enriquecer con nombre del plato
            orderDish.setDishName(dishModel.get().getName());
        }

        // establecer valores por defecto
        model.setDate(LocalDate.now());
        model.setStatus(StatusesOrder.PENDIENTE.name());
        model.setChefId(null); // se asigna después
        
        // enriquecer con nombre del restaurante
        model.getRestaurant().setName(restaurantModel.get().getName());
        
        OrderModel savedOrder = iOrderPersistencePort.save(model);
        
        // Llenar datos de la trazabilidad después del save
        TraceabilityModel traceabilityModel = new TraceabilityModel();
        traceabilityModel.setOrderId(savedOrder.getId());
        traceabilityModel.setClientId(savedOrder.getClientId());
        traceabilityModel.setClientEmail(userModel.get().getEmail());
        traceabilityModel.setDate(LocalDate.now());
        traceabilityModel.setNewStatus(savedOrder.getStatus());
        iTraceabilityFeignClientPort.saveTraceability(traceabilityModel);

        savedOrder.setRestaurant(model.getRestaurant());
        savedOrder.setOrdersDishes(model.getOrdersDishes());
        
        return savedOrder;
    }


    @Override
    @Transactional(readOnly = true)
    public Page<OrderModel> getAllOrders(Pageable pageable) {
        return iOrderPersistencePort.findAllOrders(pageable);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<OrderModel> getOrdersByStatus(String status, Pageable pageable, Long restaurantId) {
        // valido que el status sea válido
        try {
            StatusesOrder.valueOf(status);
        } catch (IllegalArgumentException e) {
            throw new ValidationException(INVALID_STATUS);
        }

        // valido que el restaurante exista
        Optional<RestaurantModel> restaurant = iRestaurantServicePort.findById(restaurantId);
        if (restaurant.isEmpty()) throw new NotFoundException(RESTAURANT_NOT_FOUND);

        // obtener pedidos filtrados por restaurante y estado
        Page<OrderModel> orders = iOrderPersistencePort.findOrdersByStatus(status, restaurantId, pageable);
        
        // enriquecer cada orden con información de platos
        return orders.map(order -> {
            if (order.getOrdersDishes() != null) {
                for (OrderDishSimpleModel orderDish : order.getOrdersDishes()) {
                    Optional<DishModel> dishModel = iDishServicePort.findById(orderDish.getId());
                    dishModel.ifPresent(model -> orderDish.setDishName(model.getName()));
                }
            }
            return order;
        });
    }

    @Override
    public OrderModel assignOrderToEmployee(Long orderId, Long employeeId) {
        // valido la orden
        OrderModel orderModel = iOrderPersistencePort.findById(orderId)
                .orElseThrow(() -> new NotFoundException(ID_NOT_FOUND));

        // valido el empleado
        UserModel userEmployee = Optional.ofNullable(iUserFeignClientPort.getUserById(employeeId))
                .orElseThrow(() -> new NotFoundException(USER_NOT_FOUND));

        // guardar trazabilidad del cambio de status
        saveTraceability(orderModel, StatusesOrder.EN_PREPARACION.name(), employeeId);


        // actualizo solo chefId y status sin tocar las relaciones
        iOrderPersistencePort.updateOrderAssignment(orderId, employeeId, StatusesOrder.EN_PREPARACION.name());
        
        // retorno la orden actualizada
        return iOrderPersistencePort.findById(orderId).orElse(orderModel);
    }

    @Override
    public OrderModel updateOrderStatus(Long orderId, String status) {
        OrderModel orderModel = iOrderPersistencePort.findById(orderId)
                .orElseThrow(() -> new NotFoundException(ID_NOT_FOUND));

        // pedidos entregados no pueden ser modificados
        if (orderModel.getStatus().equals(StatusesOrder.ENTREGADO.name())) {
            throw new BusinessException(ORDER_ALREADY_DELIVERED);
        }

        // si el pedido está EN_PREPARACION o LISTO no puede ser cancelado
        if (status.equals(StatusesOrder.CANCELADO.name()) && (orderModel.getStatus().equals(StatusesOrder.EN_PREPARACION.name()) ||
                orderModel.getStatus().equals(StatusesOrder.LISTO.name())) ||
                orderModel.getStatus().equals(StatusesOrder.ENTREGADO.name())
        ) {
            throw new BusinessException(STATUS_DONT_BE_CANCELED);
        }

        // solo pedidos LISTO pueden pasar a ENTREGADO (usar deliverOrder para esto)
        if (status.equals(StatusesOrder.ENTREGADO.name())) {
            throw new BusinessException(INVALID_STATUS_TRANSITION);
        }
        
        if (status.equals(StatusesOrder.LISTO.name())){
            // generar código aleatorio de 6 dígitos
            String securityCode = String.format("%06d", new Random().nextInt(1000000));
            
            // actualizar código de seguridad usando query directa
            iOrderPersistencePort.updateSecurityCode(orderId, securityCode);
            
            // consultar el numero de telefono del cliente
            UserModel userModel = iUserFeignClientPort.getUserById(orderModel.getClientId());
            
            // enviar notificación SMS
            SmsNotificationModel smsNotificationModel = new SmsNotificationModel();
            smsNotificationModel.setPhone(userModel.getPhone());
            smsNotificationModel.setMessage(orderModel.getRestaurant().getName() + MESSAGE_STATUS_READY + securityCode);
            iSmsFeignClientPort.sendOrderStatusNotification(smsNotificationModel);
        }

        // guardar trazabilidad del cambio de status
        saveTraceability(orderModel, status, null);
        
        // actualizar status usando query directa
        iOrderPersistencePort.updateOrderStatus(orderId, status);

        // retornar la orden actualizada
        return iOrderPersistencePort.findById(orderId).orElse(orderModel);
    }

    @Override
    public OrderModel deliverOrder(Long orderId, String securityPin) {
        OrderModel orderModel = iOrderPersistencePort.findById(orderId)
                .orElseThrow(() -> new NotFoundException(ID_NOT_FOUND));

        // solo pedidos LISTO pueden ser entregados
        if (!orderModel.getStatus().equals(StatusesOrder.LISTO.name())) {
            throw new BusinessException(INVALID_STATUS_TRANSITION);
        }

        // validar PIN de seguridad
        if (!orderModel.getSecurityCode().equals(securityPin)) {
            throw new BusinessException(INVALID_SECURITY_PIN);
        }

        // guardar trazabilidad del cambio de status
        saveTraceability(orderModel, StatusesOrder.ENTREGADO.name(), null);
        
        // actualizar a ENTREGADO
        iOrderPersistencePort.updateOrderStatus(orderId, StatusesOrder.ENTREGADO.name());

        // retornar la orden actualizada
        return iOrderPersistencePort.findById(orderId).orElse(orderModel);
    }
    
    private void saveTraceability(OrderModel orderModel, String newStatus, Long employeeId) {
        UserModel clientModel = iUserFeignClientPort.getUserById(orderModel.getClientId());
        
        TraceabilityModel traceability = new TraceabilityModel();
        traceability.setOrderId(orderModel.getId());
        traceability.setClientId(orderModel.getClientId());
        traceability.setClientEmail(clientModel.getEmail());
        traceability.setDate(LocalDate.now());
        traceability.setLastStatus(orderModel.getStatus());
        traceability.setNewStatus(newStatus);
        
        if (employeeId != null) {
            UserModel employeeModel = iUserFeignClientPort.getUserById(employeeId);
            traceability.setEmployeeId(employeeId);
            traceability.setEmployeeEmail(employeeModel.getEmail());
        }
        
        iTraceabilityFeignClientPort.saveTraceability(traceability);
    }
}
