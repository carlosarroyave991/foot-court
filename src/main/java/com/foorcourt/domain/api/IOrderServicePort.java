package com.foorcourt.domain.api;

import com.foorcourt.domain.model.OrderModel;
import com.foorcourt.domain.model.feignclient.UserModel;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface IOrderServicePort {
    OrderModel createOrder(OrderModel model);
    Page<OrderModel> getAllOrders(Pageable pageable);
    Page<OrderModel> getOrdersByStatus(String status, Pageable pageable, Long restaurantId);
}
