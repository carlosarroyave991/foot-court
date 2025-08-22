package com.foorcourt.application.handler.impl;

import com.foorcourt.application.dto.request.order.OrderRequest;
import com.foorcourt.application.dto.request.order.OrderStatusUpdateRequest;
import com.foorcourt.application.dto.response.order.OrderResponse;
import com.foorcourt.application.handler.IOrderHandler;
import com.foorcourt.application.mapper.IOrderDtoMapper;
import com.foorcourt.domain.api.IOrderServicePort;
import com.foorcourt.domain.model.OrderModel;
import com.foorcourt.domain.model.simplemodel.OrderDishSimpleModel;
import com.foorcourt.domain.model.simplemodel.RestaurantSimpleModel;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional
public class OrderHandler implements IOrderHandler {
    
    private final IOrderServicePort orderServicePort;
    private final IOrderDtoMapper orderDtoMapper;
    
    @Override
    public OrderResponse createOrder(OrderRequest request) {
        OrderModel model = orderDtoMapper.toModel(request);
        OrderModel savedModel = orderServicePort.createOrder(model);
        return orderDtoMapper.toResponse(savedModel);
    }

    
    @Override
    @Transactional(readOnly = true)
    public Page<OrderResponse> getAllOrders(Pageable pageable) {
        Page<OrderModel> models = orderServicePort.getAllOrders(pageable);
        return models.map(orderDtoMapper::toResponse);
    }
    
    @Override
    @Transactional(readOnly = true)
    public Page<OrderResponse> getOrdersByStatus(String status, Pageable pageable, Long restaurantId) {
        Page<OrderModel> models = orderServicePort.getOrdersByStatus(status, pageable, restaurantId);
        return models.map(orderDtoMapper::toResponse);
    }


}