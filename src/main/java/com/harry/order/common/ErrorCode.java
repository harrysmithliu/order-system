package com.harry.order.common;

import lombok.Getter;

/**
 * Global error code enumeration
 *
 * Each enum constant represents a standardized application-level error code and message.
 */
@Getter
public enum ErrorCode {
    // Success
    SUCCESS("0000", "Operation succeeded"),

    // ==== 10xx: Order-related errors ====
    ORDER_NOT_FOUND("1001", "Order not found"),
    ORDER_ALREADY_CANCELED("1002", "Order has already been canceled and cannot be canceled again"),
    ORDER_CANNOT_CANCEL("1003", "The current order status does not allow cancellation"),
    ORDER_ALREADY_SHIPPED("1004", "Order has already been shipped and cannot be canceled"),
    ORDER_INVALID_STATUS("1005", "Invalid order status"),

    // Payment-related errors (11XX)
    PAYMENT_FAILED("1101", "Payment failed"),
    PAYMENT_TIMEOUT("1102", "Payment timed out"),

    // Inventory-related errors (12XX)
    STOCK_INSUFFICIENT("1201", "Insufficient stock"),
    STOCK_UPDATE_FAILED("1202", "Failed to update inventory"),

    // Parameter errors (40XX)
    INVALID_PARAMETER("4001", "Invalid parameter"),
    MISSING_PARAMETER("4002", "Missing required parameter"),

    // System errors (50XX)
    SYSTEM_ERROR("5001", "Internal system error"),
    DATABASE_ERROR("5002", "Database operation failed"),
    MQ_ERROR("5003", "Message queue error");

    private final String code;
    private final String message;

    ErrorCode(String code, String message) {
        this.code = code;
        this.message = message;
    }

}
