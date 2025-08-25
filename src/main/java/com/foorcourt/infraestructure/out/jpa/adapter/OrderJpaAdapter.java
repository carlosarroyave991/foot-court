package com.foorcourt.infraestructure.out.jpa.adapter;

import com.foorcourt.domain.model.OrderModel;
import com.foorcourt.domain.spi.IOrderPersistencePort;
import com.foorcourt.infraestructure.out.jpa.entity.OrderEntity;
import com.foorcourt.infraestructure.out.jpa.mapper.IOrderEntityMapper;
import com.foorcourt.infraestructure.out.jpa.repository.IOrderRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;

@Component
@RequiredArgsConstructor
public class OrderJpaAdapter implements IOrderPersistencePort {
    
    private final IOrderRepository orderRepository;
    private final IOrderEntityMapper orderEntityMapper;
    
    @Override
    public OrderModel save(OrderModel orderModel) {
        OrderEntity entity = orderEntityMapper.toEntity(orderModel);
        OrderEntity savedEntity = orderRepository.save(entity);
        return orderEntityMapper.toModel(savedEntity);
    }
    
    @Override
    public Page<OrderModel> findAllOrders(Pageable pageable) {
        Page<OrderEntity> entities = orderRepository.findAll(pageable);
        return entities.map(orderEntityMapper::toModel);
    }
    
    @Override
    public Page<OrderModel> findOrdersByStatus(String status, Long restaurantId, Pageable pageable) {
        Page<OrderEntity> entities = orderRepository.findByStatusAndRestaurantIdWithDishes(status, restaurantId, pageable);
        return entities.map(orderEntityMapper::toModel);
    }
    
    @Override
    public List<OrderModel> findOrdersByClientIdAndStatus(Long clientId, List<String> statuses) {
        List<OrderEntity> entities = orderRepository.findByClientIdAndStatusIn(clientId, statuses);
        return entities.stream().map(orderEntityMapper::toModel).toList();
    }
    
    @Override
    public void updateOrderAssignment(Long orderId, Long chefId, String status) {
        orderRepository.updateOrderAssignment(orderId, chefId, status);
    }
    
    @Override
    public void updateOrderStatus(Long orderId, String status) {
        orderRepository.updateStatus(orderId, status);
    }
    
    @Override
    public void updateSecurityCode(Long orderId, String securityCode) {
        orderRepository.updateSecurityCode(orderId, securityCode);
    }

    @Override
    public Optional<OrderModel> findById(Long id) {
        Optional<OrderEntity> orderEntityOptional = orderRepository.findById(id);
        return orderEntityOptional.map(orderEntityMapper::toModel);
    }
}