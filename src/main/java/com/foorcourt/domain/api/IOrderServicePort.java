package com.foorcourt.domain.api;

import com.foorcourt.domain.model.OrderModel;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface IOrderServicePort {
    OrderModel createOrder(OrderModel model);
    Page<OrderModel> getAllOrders(Pageable pageable);
    Page<OrderModel> getOrdersByStatus(String status, Pageable pageable, Long restaurantId);
    OrderModel assignOrderToEmployee(Long orderId, Long employeeId);
    OrderModel updateOrderStatus(Long orderId, String status);
    OrderModel deliverOrder(Long orderId, String securityPin);
}
