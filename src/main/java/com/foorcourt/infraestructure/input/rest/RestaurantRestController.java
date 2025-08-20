package com.foorcourt.infraestructure.input.rest;

import com.foorcourt.application.dto.request.restaurant.RestaurantRequest;
import com.foorcourt.application.dto.response.restaurant.RestaurantResponse;
import com.foorcourt.application.handler.IRestaurantHandler;
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
@RequestMapping("/api/v1/restaurants")
@RequiredArgsConstructor
@Tag(name = "Restaurant", description = "Endpoints for restaurant options")
public class RestaurantRestController {
    private final IRestaurantHandler handler;
    
    @PostMapping
    @Operation(summary = "Create a new restaurant")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Restaurant created successfully"),
            @ApiResponse(responseCode = "400", description = "Invalid input data"),
            @ApiResponse(responseCode = "404", description = "Owner not found")
    })
    public ResponseEntity<RestaurantResponse> save(@Valid @RequestBody RestaurantRequest request) {
        RestaurantResponse response = handler.save(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }
    
    @GetMapping
    @Operation(summary = "Get all restaurants with pagination")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Restaurants retrieved successfully"),
            @ApiResponse(responseCode = "400", description = "Invalid pagination parameters")
    })
    public ResponseEntity<Page<RestaurantResponse>> getAll(
            @PageableDefault(size = 10, sort = "name") Pageable pageable) {
        Page<RestaurantResponse> response = handler.getAll(pageable);
        return ResponseEntity.ok(response);
    }
}
