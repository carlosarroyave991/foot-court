package com.foorcourt.domain.exception.error;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
@AllArgsConstructor
public enum CommonErrorCode  implements ErrorCode{
    // Validation errors
    VALIDATION_ERROR("ERR_VALIDATION", "Validation error", HttpStatus.BAD_REQUEST.value(), ErrorCategory.VALIDATION),
    INVALID_INPUT("ERR_INVALID_INPUT", "Invalid input data", HttpStatus.BAD_REQUEST.value(), ErrorCategory.VALIDATION),
    INVALID_FORMAT("ERR_INVALID_FORMAT", "Invalid data format", HttpStatus.BAD_REQUEST.value(), ErrorCategory.VALIDATION),
    MISSING_FIELD("ERR_MISSING_FIELD", "Required field is missing", HttpStatus.BAD_REQUEST.value(), ErrorCategory.VALIDATION),
    INVALID_EMAIL("ERR_EMAIL_INVALID", "Invalid email format", HttpStatus.BAD_REQUEST.value(), ErrorCategory.VALIDATION),
    INVALID_PHONE("ERR_PHONE_INVALID", "Invalid phone number", HttpStatus.BAD_REQUEST.value(), ErrorCategory.VALIDATION),
    INVALID_ID("ERR_ID_INVALID", "Invalid identifier", HttpStatus.BAD_REQUEST.value(), ErrorCategory.VALIDATION),
    INVALID_NIT("ERR_NIT_INVALID", "Invalid Nit", HttpStatus.BAD_REQUEST.value(), ErrorCategory.VALIDATION),
    INVALID_ZIPCODE("ERR_INVALID_ZIPCODE", "Invalid zip code", HttpStatus.BAD_REQUEST.value(), ErrorCategory.VALIDATION),
    INVALID_USERNAME("ERR_USERNAME_INVALID", "Invalid username", HttpStatus.BAD_REQUEST.value(), ErrorCategory.VALIDATION),
    INVALID_QUANTITY("ERR_QUANTITY_INVALID", "Invalid quantity", HttpStatus.BAD_REQUEST.value(), ErrorCategory.VALIDATION),
    INVALID_PASSWORD("ERR_PASSWORD_INVALID", "Invalid password", HttpStatus.BAD_REQUEST.value(), ErrorCategory.VALIDATION),
    INVALID_USER_TYPE("ERR_USER_TYPE_INVALID", "Invalid user type", HttpStatus.BAD_REQUEST.value(), ErrorCategory.VALIDATION),
    INVALID_DISH("ERR_DISH_INVALID", "Invalid dish", HttpStatus.BAD_REQUEST.value(), ErrorCategory.VALIDATION),
    INVALID_STATUS("ERR_STATUS_INVALID", "Invalid status", HttpStatus.BAD_REQUEST.value(), ErrorCategory.VALIDATION),
    INVALID_ROLE("ERR_USER_ROLE", "Invalid role", HttpStatus.BAD_REQUEST.value(), ErrorCategory.VALIDATION),
    EMPTY_PASSWORD("ERR_PASSWORD_EMPTY", "Password cannot be empty", HttpStatus.BAD_REQUEST.value(), ErrorCategory.VALIDATION),


    // Resource errors
    RESOURCE_NOT_FOUND("ERR_NOT_FOUND", "Resource not found", HttpStatus.NOT_FOUND.value(), ErrorCategory.RESOURCE),
    RESOURCE_ALREADY_EXISTS("ERR_DUPLICATE_RESOURCE", "Resource already exists", HttpStatus.CONFLICT.value(), ErrorCategory.RESOURCE),
    RESTAURANT_NOT_FOUND("ERR_RESTAURANT_NOT_FOUND", "Restaurant not found", HttpStatus.NOT_FOUND.value(), ErrorCategory.RESOURCE),
    CATEGORY_NOT_FOUND("ERR_CATEGORY_NOT_FOUND", "Category not found", HttpStatus.NOT_FOUND.value(), ErrorCategory.RESOURCE),
    USER_ALREADY_EXISTS("ERR_DUPLICATE_USER", "User already exists", HttpStatus.CONFLICT.value(), ErrorCategory.RESOURCE),
    ID_ROLE_NOT_FOUND("ERR_ID_ROLE_NOT_FOUND", "Role id not found", HttpStatus.NOT_FOUND.value(),ErrorCategory.BUSINESS),
    CLIENT_ALREADY_HAS_AN_ORDER("ERR_CLIENT_ALREADY_HAS_AN_ORDER", "The customer already has an order", HttpStatus.CONFLICT.value(),ErrorCategory.BUSINESS),
    ORDER_MUST_HAVENT_DISH("ERR_ORDER_MUST_HAVENT_DISH", "Order must have at least one dish", HttpStatus.CONFLICT.value(),ErrorCategory.BUSINESS),
    DNI_ALREADY_EXISTS("ERR_DUPLICATE_DNI", "DNI already exists", HttpStatus.CONFLICT.value(), ErrorCategory.RESOURCE),
    NIT_ALREADY_EXISTS("ERR_DUPLICATE_NIT", "NIT already exists", HttpStatus.CONFLICT.value(), ErrorCategory.RESOURCE),
    DNI_NOT_FOUND("ERR_DNI_NOT_FOUND", "DNI not found", HttpStatus.CONFLICT.value(), ErrorCategory.RESOURCE),
    USER_NOT_FOUND("ERR_USER_NOT_FOUND", "User not found", HttpStatus.NOT_FOUND.value(), ErrorCategory.RESOURCE),
    ID_NOT_FOUND("ERR_ID_NOT_FOUND", "Id not found", HttpStatus.NOT_FOUND.value(), ErrorCategory.RESOURCE),
    USER_NAME_NOT_FOUND("ERR_USER_NAME_NOT_FOUND", "No matches were found.", HttpStatus.NOT_FOUND.value(), ErrorCategory.RESOURCE),
    EMAIL_ALREADY_EXISTS("ERR_EMAIL_EXISTS", "Email already registered", HttpStatus.CONFLICT.value(), ErrorCategory.RESOURCE),
    DB_EMPTY("ERR_DB_EMPTY", "No data found in the database.", HttpStatus.NOT_FOUND.value(), ErrorCategory.RESOURCE),

    // Authentication errors
    UNAUTHORIZED("ERR_UNAUTHORIZED", "Unauthorized access", HttpStatus.UNAUTHORIZED.value(), ErrorCategory.AUTHENTICATION),
    INVALID_CREDENTIALS("ERR_INVALID_CREDENTIALS", "Invalid credentials", HttpStatus.UNAUTHORIZED.value(), ErrorCategory.AUTHENTICATION),
    INVALID_TOKEN("ERR_INVALID_TOKEN", "Invalid token", HttpStatus.UNAUTHORIZED.value(), ErrorCategory.AUTHENTICATION),
    EXPIRED_TOKEN("ERR_EXPIRED_TOKEN", "Token has expired", HttpStatus.UNAUTHORIZED.value(), ErrorCategory.AUTHENTICATION),

    // Authorization errors
    FORBIDDEN("ERR_SECURITY", "Access forbidden", HttpStatus.FORBIDDEN.value(), ErrorCategory.AUTHORIZATION),
    INSUFFICIENT_PERMISSIONS("ERR_INSUFFICIENT_PERMISSIONS", "Insufficient permissions", HttpStatus.FORBIDDEN.value(), ErrorCategory.AUTHORIZATION),

    // Data access errors
    DATA_ACCESS_ERROR("ERR_DATA_ACCESS", "Data access error", HttpStatus.INTERNAL_SERVER_ERROR.value(), ErrorCategory.DATA_ACCESS),
    DATABASE_ERROR("ERR_DATABASE", "Database error", HttpStatus.INTERNAL_SERVER_ERROR.value(), ErrorCategory.DATA_ACCESS),

    // System errors
    INTERNAL_ERROR("ERR_INTERNAL_SERVER", "Internal server error", HttpStatus.INTERNAL_SERVER_ERROR.value(), ErrorCategory.SYSTEM),
    SERVICE_UNAVAILABLE("ERR_SERVICE_UNAVAILABLE", "Service unavailable", HttpStatus.SERVICE_UNAVAILABLE.value(), ErrorCategory.SYSTEM),

    // External service errors
    EXTERNAL_SERVICE_ERROR("ERR_EXTERNAL_SERVICE", "External service error", HttpStatus.INTERNAL_SERVER_ERROR.value(), ErrorCategory.EXTERNAL_SERVICE),
    EXTERNAL_SERVICE_TIMEOUT("ERR_EXTERNAL_SERVICE_TIMEOUT", "External service timeout", HttpStatus.GATEWAY_TIMEOUT.value(), ErrorCategory.EXTERNAL_SERVICE);

    private final String code;
    private final String message;
    private final int statusCode;
    private final ErrorCategory category;
}
