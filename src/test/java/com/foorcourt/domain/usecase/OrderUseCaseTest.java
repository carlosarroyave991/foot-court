package com.foorcourt.domain.usecase;

import com.foorcourt.domain.api.IDishServicePort;
import com.foorcourt.domain.api.IRestaurantServicePort;
import com.foorcourt.domain.exception.BusinessException;
import com.foorcourt.domain.exception.NotFoundException;
import com.foorcourt.domain.exception.ValidationException;
import com.foorcourt.domain.model.DishModel;
import com.foorcourt.domain.model.OrderModel;
import com.foorcourt.domain.model.RestaurantModel;
import com.foorcourt.domain.model.SmsNotificationModel;
import com.foorcourt.domain.model.enums.StatusesOrder;
import com.foorcourt.domain.model.feignclient.UserModel;
import com.foorcourt.domain.model.simplemodel.OrderDishSimpleModel;
import com.foorcourt.domain.model.simplemodel.RestaurantSimpleModel;
import com.foorcourt.domain.spi.IOrderPersistencePort;
import com.foorcourt.domain.spi.ISmsFeignClientPort;
import com.foorcourt.domain.spi.IUserFeignClientPort;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;

import java.util.Arrays;
import java.util.Collections;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class OrderUseCaseTest {

    @Mock
    private IDishServicePort dishServicePort;
    @Mock
    private IRestaurantServicePort restaurantServicePort;
    @Mock
    private IUserFeignClientPort userFeignClientPort;
    @Mock
    private IOrderPersistencePort orderPersistencePort;
    @Mock
    private ISmsFeignClientPort smsFeignClientPort;

    @InjectMocks
    private OrderUseCase orderUseCase;

    private OrderModel orderModel;
    private UserModel userModel;
    private RestaurantModel restaurantModel;
    private DishModel dishModel;

    @BeforeEach
    void setUp() {
        RestaurantSimpleModel restaurant = new RestaurantSimpleModel();
        restaurant.setId(1L);
        restaurant.setName("Test Restaurant");

        orderModel = new OrderModel();
        orderModel.setId(1L);
        orderModel.setClientId(1L);
        orderModel.setStatus(StatusesOrder.PENDIENTE.name());
        orderModel.setSecurityCode("123456");
        orderModel.setRestaurant(restaurant);

        userModel = new UserModel();
        userModel.setId(1L);
        userModel.setPhone(String.valueOf(123456789L));

        restaurantModel = new RestaurantModel();
        restaurantModel.setId(1L);
        restaurantModel.setName("Test Restaurant");

        dishModel = new DishModel();
        dishModel.setId(1L);
        dishModel.setName("Test Dish");
        dishModel.setActive(true);
        dishModel.setRestaurant(restaurant);
    }

    @Test
    void createOrder_WhenValidOrder_ShouldCreateSuccessfully() {
        // Given
        OrderDishSimpleModel orderDish = new OrderDishSimpleModel();
        orderDish.setId(1L);
        orderDish.setQuantity(2);
        orderModel.setOrdersDishes(Arrays.asList(orderDish));

        when(restaurantServicePort.findById(1L)).thenReturn(Optional.of(restaurantModel));
        when(userFeignClientPort.getUserById(1L)).thenReturn(userModel);
        when(orderPersistencePort.findOrdersByClientIdAndStatus(eq(1L), anyList())).thenReturn(Collections.emptyList());
        when(dishServicePort.findById(1L)).thenReturn(Optional.of(dishModel));
        when(orderPersistencePort.save(any(OrderModel.class))).thenReturn(orderModel);

        // When
        OrderModel result = orderUseCase.createOrder(orderModel);

        // Then
        assertNotNull(result);
        verify(orderPersistencePort).save(any(OrderModel.class));
    }

    @Test
    void createOrder_WhenRestaurantNotFound_ShouldThrowBusinessException() {
        // Given
        when(restaurantServicePort.findById(1L)).thenReturn(Optional.empty());

        // When & Then
        assertThrows(BusinessException.class, () -> orderUseCase.createOrder(orderModel));
    }

    @Test
    void createOrder_WhenClientHasOrderInProcess_ShouldThrowBusinessException() {
        // Given
        when(restaurantServicePort.findById(1L)).thenReturn(Optional.of(restaurantModel));
        when(userFeignClientPort.getUserById(1L)).thenReturn(userModel);
        when(orderPersistencePort.findOrdersByClientIdAndStatus(eq(1L), anyList())).thenReturn(Arrays.asList(orderModel));

        // When & Then
        assertThrows(BusinessException.class, () -> orderUseCase.createOrder(orderModel));
    }

    @Test
    void getAllOrders_ShouldReturnPageOfOrders() {
        // Given
        Page<OrderModel> mockPage = new PageImpl<>(Arrays.asList(orderModel));
        when(orderPersistencePort.findAllOrders(any(Pageable.class))).thenReturn(mockPage);

        // When
        Page<OrderModel> result = orderUseCase.getAllOrders(mock(Pageable.class));

        // Then
        assertNotNull(result);
        assertEquals(1, result.getContent().size());
    }

    @Test
    void getOrdersByStatus_WhenValidStatus_ShouldReturnOrders() {
        // Given
        Page<OrderModel> mockPage = new PageImpl<>(Arrays.asList(orderModel));
        when(restaurantServicePort.findById(1L)).thenReturn(Optional.of(restaurantModel));
        when(orderPersistencePort.findOrdersByStatus(anyString(), eq(1L), any(Pageable.class))).thenReturn(mockPage);

        // When
        Page<OrderModel> result = orderUseCase.getOrdersByStatus("PENDIENTE", mock(Pageable.class), 1L);

        // Then
        assertNotNull(result);
        assertEquals(1, result.getContent().size());
    }

    @Test
    void getOrdersByStatus_WhenInvalidStatus_ShouldThrowValidationException() {
        // When & Then
        assertThrows(ValidationException.class, 
            () -> orderUseCase.getOrdersByStatus("INVALID_STATUS", mock(Pageable.class), 1L));
    }

    @Test
    void assignOrderToEmployee_WhenValidData_ShouldAssignSuccessfully() {
        // Given
        when(orderPersistencePort.findById(1L)).thenReturn(Optional.of(orderModel));
        when(userFeignClientPort.getUserById(2L)).thenReturn(userModel);
        when(orderPersistencePort.findById(1L)).thenReturn(Optional.of(orderModel));

        // When
        OrderModel result = orderUseCase.assignOrderToEmployee(1L, 2L);

        // Then
        assertNotNull(result);
        verify(orderPersistencePort).updateOrderAssignment(1L, 2L, StatusesOrder.EN_PREPARACION.name());
    }

    @Test
    void assignOrderToEmployee_WhenOrderNotFound_ShouldThrowNotFoundException() {
        // Given
        when(orderPersistencePort.findById(1L)).thenReturn(Optional.empty());

        // When & Then
        assertThrows(NotFoundException.class, 
            () -> orderUseCase.assignOrderToEmployee(1L, 2L));
    }

    @Test
    void updateOrderStatus_WhenStatusIsListo_ShouldGenerateSecurityCodeAndSendSms() {
        // Given
        when(orderPersistencePort.findById(1L)).thenReturn(Optional.of(orderModel));
        when(userFeignClientPort.getUserById(1L)).thenReturn(userModel);
        when(orderPersistencePort.findById(1L)).thenReturn(Optional.of(orderModel));

        // When
        OrderModel result = orderUseCase.updateOrderStatus(1L, StatusesOrder.LISTO.name());

        // Then
        verify(orderPersistencePort).updateSecurityCode(eq(1L), anyString());
        verify(smsFeignClientPort).sendOrderStatusNotification(any(SmsNotificationModel.class));
        assertNotNull(result);
    }

    @Test
    void updateOrderStatus_WhenOrderAlreadyDelivered_ShouldThrowBusinessException() {
        // Given
        orderModel.setStatus(StatusesOrder.ENTREGADO.name());
        when(orderPersistencePort.findById(1L)).thenReturn(Optional.of(orderModel));

        // When & Then
        assertThrows(BusinessException.class, 
            () -> orderUseCase.updateOrderStatus(1L, StatusesOrder.LISTO.name()));
    }

    @Test
    void updateOrderStatus_WhenTryingToSetEntregado_ShouldThrowBusinessException() {
        // Given
        when(orderPersistencePort.findById(1L)).thenReturn(Optional.of(orderModel));

        // When & Then
        assertThrows(BusinessException.class, 
            () -> orderUseCase.updateOrderStatus(1L, StatusesOrder.ENTREGADO.name()));
    }

    @Test
    void deliverOrder_WhenValidPinAndStatus_ShouldDeliverSuccessfully() {
        // Given
        orderModel.setStatus(StatusesOrder.LISTO.name());
        when(orderPersistencePort.findById(1L)).thenReturn(Optional.of(orderModel));
        when(orderPersistencePort.findById(1L)).thenReturn(Optional.of(orderModel));

        // When
        OrderModel result = orderUseCase.deliverOrder(1L, "123456");

        // Then
        verify(orderPersistencePort).updateOrderStatus(1L, StatusesOrder.ENTREGADO.name());
        assertNotNull(result);
    }

    @Test
    void deliverOrder_WhenInvalidPin_ShouldThrowBusinessException() {
        // Given
        orderModel.setStatus(StatusesOrder.LISTO.name());
        when(orderPersistencePort.findById(1L)).thenReturn(Optional.of(orderModel));

        // When & Then
        assertThrows(BusinessException.class, 
            () -> orderUseCase.deliverOrder(1L, "wrong_pin"));
    }

    @Test
    void deliverOrder_WhenOrderNotReady_ShouldThrowBusinessException() {
        // Given
        orderModel.setStatus(StatusesOrder.EN_PREPARACION.name());
        when(orderPersistencePort.findById(1L)).thenReturn(Optional.of(orderModel));

        // When & Then
        assertThrows(BusinessException.class, 
            () -> orderUseCase.deliverOrder(1L, "123456"));
    }

    @Test
    void deliverOrder_WhenOrderNotFound_ShouldThrowNotFoundException() {
        // Given
        when(orderPersistencePort.findById(1L)).thenReturn(Optional.empty());

        // When & Then
        assertThrows(NotFoundException.class, 
            () -> orderUseCase.deliverOrder(1L, "123456"));
    }
}