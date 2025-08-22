package com.foorcourt.infraestructure.out.jpa.adapter;

import com.foorcourt.domain.model.OrderModel;
import com.foorcourt.domain.model.simplemodel.OrderDishSimpleModel;
import com.foorcourt.domain.spi.IOrderPersistencePort;
import com.foorcourt.infraestructure.out.jpa.entity.DishEntity;
import com.foorcourt.infraestructure.out.jpa.entity.OrderDishEntity;
import com.foorcourt.infraestructure.out.jpa.entity.OrderEntity;
import com.foorcourt.infraestructure.out.jpa.mapper.IOrderEntityMapper;
import com.foorcourt.infraestructure.out.jpa.repository.IDishRepository;
import com.foorcourt.infraestructure.out.jpa.repository.IOrderDishRepository;
import com.foorcourt.infraestructure.out.jpa.repository.IOrderRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
public class OrderJpaAdapter implements IOrderPersistencePort {
    
    private final IOrderRepository orderRepository;
    private final IOrderDishRepository orderDishRepository;
    private final IDishRepository dishRepository;
    private final IOrderEntityMapper orderMapper;

    @Override
    public Page<OrderModel> findOrdersByStatus(String status, Long restaurantId, Pageable pageable) {
        return null;
    }

    @Override
    @Transactional
    public OrderModel save(OrderModel model) {
        // Mapear OrderModel a OrderEntity
        OrderEntity orderEntity = orderMapper.toEntity(model);
        
        // Guardar la orden primero
        OrderEntity savedOrder = orderRepository.save(orderEntity);
        
        // Guardar los platos de la orden
        if (model.getOrdersDishes() != null && !model.getOrdersDishes().isEmpty()) {
            List<OrderDishEntity> orderDishEntities = new ArrayList<>();
            
            for (OrderDishSimpleModel orderDishModel : model.getOrdersDishes()) {
                // Buscar el plato
                Optional<DishEntity> dishEntity = dishRepository.findById(orderDishModel.getId());
                if (dishEntity.isPresent()) {
                    OrderDishEntity orderDishEntity = OrderDishEntity.builder()
                            .quantity(orderDishModel.getQuantity())
                            .order(savedOrder)
                            .dish(dishEntity.get())
                            .build();
                    orderDishEntities.add(orderDishEntity);
                }
            }
            
            // Guardar todos los OrderDishEntity
            orderDishRepository.saveAll(orderDishEntities);
            savedOrder.setOrdersDishes(orderDishEntities);
        }
        
        return orderMapper.toModel(savedOrder);
    }

    @Override
    public List<OrderModel> findOrdersByClientIdAndStatus(Long clientId, List<String> statuses) {
        List<OrderEntity> orders = orderRepository.findByClientIdAndStatusIn(clientId, statuses);
        return orders.stream()
                .map(orderMapper::toModel)
                .toList();
    }



    @Override
    public Page<OrderModel> findAllOrders(Pageable pageable) {
        Page<OrderEntity> orders = orderRepository.findAll(pageable);
        return orders.map(orderMapper::toModel);
    }


    @Override
    public Optional<OrderModel> findById(Long id) {
        Optional<OrderEntity> orderEntity = orderRepository.findById(id);
        return orderEntity.map(orderMapper::toModel);
    }
    

}