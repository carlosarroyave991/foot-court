package com.foorcourt.domain.spi;

import com.foorcourt.domain.model.OrderModel;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Optional;

public interface IOrderPersistencePort {
    OrderModel save(OrderModel model);
    List<OrderModel> findOrdersByClientIdAndStatus(Long clientId, List<String> status);
    Page<OrderModel> findAllOrders(Pageable pageable);
    Page<OrderModel> findOrdersByStatus(String status, Long restaurantId ,Pageable pageable);
    Optional<OrderModel> findById(Long id);
    void updateOrderAssignment(Long orderId, Long chefId, String status);
    void updateOrderStatus(Long orderId, String status);
    void updateSecurityCode(Long orderId, String securityCode);
    //OrderModel update(OrderModel model);
}
