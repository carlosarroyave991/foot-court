package com.foorcourt.application.handler;

import com.foorcourt.application.dto.request.order.OrderRequest;
import com.foorcourt.application.dto.request.order.OrderStatusUpdateRequest;
import com.foorcourt.application.dto.response.order.OrderResponse;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface IOrderHandler {
    OrderResponse createOrder(OrderRequest request);
    Page<OrderResponse> getAllOrders(Pageable pageable);
    Page<OrderResponse> getOrdersByStatus(String status, Pageable pageable, Long restaurantId);
    OrderResponse assignOrderToEmployee(Long orderId, Long employeeId);
}