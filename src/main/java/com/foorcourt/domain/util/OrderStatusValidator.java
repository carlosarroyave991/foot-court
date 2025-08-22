package com.foorcourt.domain.util;

import com.foorcourt.domain.exception.BusinessException;
import com.foorcourt.domain.exception.NotFoundException;
import com.foorcourt.domain.exception.ValidationException;
import com.foorcourt.domain.model.OrderModel;
import com.foorcourt.domain.model.enums.StatusesOrder;
import com.foorcourt.domain.model.feignclient.UserModel;
import com.foorcourt.domain.spi.IUserFeignClientPort;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
public class OrderStatusValidator {
    
    private IUserFeignClientPort userFeignClientPort;
    
    public void validateStatusTransition(OrderModel order, String newStatus, Long employeeId, String securityPin, IUserFeignClientPort userFeignClientPort) {
        this.userFeignClientPort = userFeignClientPort;
        StatusesOrder currentStatus = StatusesOrder.valueOf(order.getStatus());
        StatusesOrder targetStatus = StatusesOrder.valueOf(newStatus);
        
        switch (targetStatus) {
            case CANCELADO:
                validateCancellation(currentStatus);
                break;
            case EN_PREPARACION:
                validatePreparation(currentStatus, employeeId);
                order.setChefId(employeeId);
                break;
            case LISTO:
                validateReady(currentStatus);
                break;
            case ENTREGADO:
                validateDelivery(currentStatus, securityPin);
                break;
            case PENDIENTE:
                throw new ValidationException("Cannot change status back to PENDIENTE");
            default:
                throw new ValidationException("Invalid status transition");
        }
    }
    
    private void validateCancellation(StatusesOrder currentStatus) {
        if (currentStatus != StatusesOrder.PENDIENTE) {
            throw new ValidationException("Lo sentimos, tu pedido ya está en preparación y no puede cancelarse");
        }
    }
    
    private void validatePreparation(StatusesOrder currentStatus, Long employeeId) {
        if (currentStatus != StatusesOrder.PENDIENTE) {
            throw new ValidationException("Order can only move to EN_PREPARACION from PENDIENTE");
        }
        
        if (employeeId == null) {
            throw new ValidationException("Employee ID is required for EN_PREPARACION status");
        }
        
        UserModel employeeModel = userFeignClientPort.getUserById(employeeId);
        if (employeeModel == null) {
            throw new NotFoundException("Employee not found");
        }
        
        if (!"employee".equals(employeeModel.getRole().getName())) {
            throw new ValidationException("User must have employee role");
        }
    }
    
    private void validateReady(StatusesOrder currentStatus) {
        if (currentStatus != StatusesOrder.EN_PREPARACION) {
            throw new ValidationException("Order can only move to LISTO from EN_PREPARACION");
        }
    }
    
    private void validateDelivery(StatusesOrder currentStatus, String securityPin) {
        if (currentStatus != StatusesOrder.LISTO) {
            throw new ValidationException("Only orders with LISTO status can be delivered");
        }
        
        if (securityPin == null || securityPin.trim().isEmpty()) {
            throw new ValidationException("Security PIN is required for delivery");
        }
        
        // Aquí puedes agregar validación del PIN contra la base de datos
        // Por ahora solo validamos que no esté vacío
    }
}