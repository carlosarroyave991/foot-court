package com.foorcourt.infraestructure.input.rest;

import com.foorcourt.application.dto.request.dish.DishChangeStatusRequest;
import com.foorcourt.application.dto.request.dish.DishRequest;
import com.foorcourt.application.dto.request.dish.DishUpdateRequest;
import com.foorcourt.application.dto.response.dish.DishResponse;
import com.foorcourt.application.dto.response.dish.DishSimpleResponse;
import com.foorcourt.application.handler.IDishHandler;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/dishes")
@RequiredArgsConstructor
@Tag(name = "Dish", description = "Endpoints for dish management")
public class DishRestController {
    private final IDishHandler handler;
    
    @PostMapping
    @Operation(summary = "Create a new dish")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Dish created successfully"),
            @ApiResponse(responseCode = "400", description = "Invalid input data"),
            @ApiResponse(responseCode = "404", description = "Restaurant or category not found")
    })
    public ResponseEntity<DishResponse> save(
            @Valid @RequestBody DishRequest request,
            @RequestParam Long restaurantId,
            @RequestParam Long categoryId) {
        DishResponse response = handler.save(request, restaurantId, categoryId);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }
    
    @PutMapping("/{id}")
    @Operation(summary = "Update an existing dish")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Dish updated successfully"),
            @ApiResponse(responseCode = "400", description = "Invalid input data"),
            @ApiResponse(responseCode = "404", description = "Dish, restaurant or category not found")
    })
    public ResponseEntity<DishResponse> update(
            @PathVariable Long id,
            @Valid @RequestBody DishUpdateRequest request,
            @RequestParam Long restaurantId,
            @RequestParam Long categoryId) {
        request.setId(id);
        DishResponse response = handler.update(request, restaurantId, categoryId);
        return ResponseEntity.ok(response);
    }
    
    @PatchMapping("/{id}/status")
    @Operation(summary = "Change dish status (enable/disable)")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Dish status changed successfully"),
            @ApiResponse(responseCode = "404", description = "Dish not found")
    })
    public ResponseEntity<DishResponse> changeStatus(
            @PathVariable Long id,
            @Valid @RequestBody DishChangeStatusRequest request) {
        request.setId(id);
        DishResponse response = handler.changeStatusDish(request);
        return ResponseEntity.ok(response);
    }
    
    @GetMapping
    @Operation(summary = "Get dishes by restaurant and optionally by category")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Dishes retrieved successfully"),
            @ApiResponse(responseCode = "404", description = "Restaurant or category not found")
    })
    public ResponseEntity<Page<DishSimpleResponse>> getDishesByRestaurant(
            @RequestParam Long restaurantId,
            @RequestParam(required = false) Long categoryId,
            @PageableDefault(size = 10, sort = "name") Pageable pageable) {
        Page<DishSimpleResponse> response = handler.findAllDishesByRestaurantId(pageable, restaurantId, categoryId);
        return ResponseEntity.ok(response);
    }
}