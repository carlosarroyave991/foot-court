package com.foorcourt.infraestructure.input.rest;

import com.foorcourt.application.dto.request.order.OrderRequest;
import com.foorcourt.application.dto.response.order.OrderResponse;
import com.foorcourt.application.handler.IOrderHandler;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/orders")
@RequiredArgsConstructor
@Tag(name = "Order", description = "Endpoints for order management")
public class OrderRestController {
    private final IOrderHandler handler;

    @PostMapping
    @Operation(summary = "Create a new order", description = "Creates a new order with the provided details")
    @ApiResponse(responseCode = "201", description = "Order created successfully")
    @ApiResponse(responseCode = "400", description = "Invalid request data")
    public ResponseEntity<OrderResponse> createOrder(@Valid @RequestBody OrderRequest orderRequest) {
        OrderResponse response = handler.createOrder(orderRequest);
        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }

    @GetMapping
    @Operation(summary = "Get all orders", description = "Retrieves all orders with pagination")
    @ApiResponse(responseCode = "200", description = "Orders retrieved successfully")
    public ResponseEntity<Page<OrderResponse>> getAllOrders(
            @Parameter(description = "Page number (0-based)") @RequestParam(defaultValue = "0") int page,
            @Parameter(description = "Page size") @RequestParam(defaultValue = "10") int size) {
        Pageable pageable = PageRequest.of(page, size);
        Page<OrderResponse> orders = handler.getAllOrders(pageable);
        return ResponseEntity.ok(orders);
    }

    @GetMapping("/status/{status}")
    @Operation(summary = "Get orders by status", description = "Retrieves orders filtered by status for a specific restaurant")
    @ApiResponse(responseCode = "200", description = "Orders retrieved successfully")
    @ApiResponse(responseCode = "404", description = "No orders found")
    public ResponseEntity<Page<OrderResponse>> getOrdersByStatus(
            @Parameter(description = "Order status") @PathVariable String status,
            @Parameter(description = "Page number (0-based)") @RequestParam(defaultValue = "0") int page,
            @Parameter(description = "Page size") @RequestParam(defaultValue = "10") int size,
            @Parameter(description = "Restaurant ID") @RequestParam Long restaurantId) {
        Pageable pageable = PageRequest.of(page, size);
        Page<OrderResponse> orders = handler.getOrdersByStatus(status, pageable, restaurantId);
        return ResponseEntity.ok(orders);
    }
    
    @PatchMapping("/{orderId}/assign")
    @Operation(summary = "Assign order to employee", description = "Assigns an order to an employee and changes status")
    @ApiResponse(responseCode = "200", description = "Order assigned successfully")
    @ApiResponse(responseCode = "404", description = "Order or employee not found")
    public ResponseEntity<OrderResponse> assignOrderToEmployee(
            @Parameter(description = "Order ID") @PathVariable Long orderId,
            @Parameter(description = "Employee ID") @RequestParam Long employeeId) {
        OrderResponse response = handler.assignOrderToEmployee(orderId, employeeId);
        return ResponseEntity.ok(response);
    }
    
    @PatchMapping("/{orderId}/status")
    @Operation(summary = "Update order status", description = "Updates the status of an order")
    @ApiResponse(responseCode = "200", description = "Order status updated successfully")
    @ApiResponse(responseCode = "404", description = "Order not found")
    public ResponseEntity<OrderResponse> updateOrderStatus(
            @Parameter(description = "Order ID") @PathVariable Long orderId,
            @Parameter(description = "New status") @RequestParam String status) {
        OrderResponse response = handler.updateOrderStatus(orderId, status);
        return ResponseEntity.ok(response);
    }
}
