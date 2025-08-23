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
import com.foorcourt.domain.model.enums.StatusesOrder;
import com.foorcourt.domain.model.feignclient.UserModel;
import com.foorcourt.domain.model.simplemodel.OrderDishSimpleModel;
import com.foorcourt.domain.model.simplemodel.RestaurantSimpleModel;
import com.foorcourt.domain.spi.IOrderPersistencePort;
import com.foorcourt.domain.spi.IUserFeignClientPort;
import com.foorcourt.domain.util.OrderStatusValidator;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import static com.foorcourt.domain.exception.error.CommonErrorCode.*;

/**
 * Clase usada para implementar la logica de negocio sobre cada funcion
 */
@RequiredArgsConstructor
public class OrderUseCase implements IOrderServicePort {
    private final IDishServicePort iDishServicePort;
    private final IRestaurantServicePort iRestaurantServicePort;
    private final IUserFeignClientPort iUserFeignClientPort;
    private final IOrderPersistencePort iOrderPersistencePort;
    private final OrderStatusValidator orderStatusValidator;

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

        savedOrder.setRestaurant(model.getRestaurant());
        savedOrder.setOrdersDishes(model.getOrdersDishes());
        
        return savedOrder;
    }


    @Override
    public Page<OrderModel> getAllOrders(Pageable pageable) {
        return iOrderPersistencePort.findAllOrders(pageable);
    }

    @Override
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
        UserModel userModel = Optional.ofNullable(iUserFeignClientPort.getUserById(employeeId))
                .orElseThrow(() -> new NotFoundException(USER_NOT_FOUND));

        // actualizo solo chefId y status sin tocar las relaciones
        iOrderPersistencePort.updateOrderAssignment(orderId, employeeId, StatusesOrder.EN_PREPARACION.name());
        
        // retorno la orden actualizada
        return iOrderPersistencePort.findById(orderId).orElse(orderModel);
    }
}
