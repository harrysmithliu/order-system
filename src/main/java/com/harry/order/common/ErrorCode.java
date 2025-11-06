package com.harry.order.common;

import lombok.Getter;

/**
 * 错误码枚举类
 * 规范: 0000=成功, 1XXX=业务异常, 4XXX=客户端异常, 5XXX=服务器异常
 */
@Getter
public enum ErrorCode {
    // 成功
    SUCCESS("0000", "操作成功"),

    // 订单相关错误 (1XXX)
    ORDER_NOT_FOUND("1001", "订单不存在"),
    ORDER_ALREADY_CANCELED("1002", "订单已取消，无法重复取消"),
    ORDER_CANNOT_CANCEL("1003", "当前订单状态不允许取消"),
    ORDER_ALREADY_SHIPPED("1004", "订单已发货，无法取消"),
    ORDER_INVALID_STATUS("1005", "订单状态无效"),

    // 支付相关错误 (1XXX)
    PAYMENT_FAILED("1101", "支付失败"),
    PAYMENT_TIMEOUT("1102", "支付超时"),

    // 库存相关错误 (1XXX)
    STOCK_INSUFFICIENT("1201", "库存不足"),
    STOCK_UPDATE_FAILED("1202", "库存更新失败"),

    // 参数错误 (4XXX)
    INVALID_PARAMETER("4001", "参数无效"),
    MISSING_PARAMETER("4002", "参数缺失"),

    // 系统错误 (5XXX)
    SYSTEM_ERROR("5001", "系统内部错误"),
    DATABASE_ERROR("5002", "数据库操作失败"),
    MQ_ERROR("5003", "消息队列错误");

    private final String code;
    private final String message;

    ErrorCode(String code, String message) {
        this.code = code;
        this.message = message;
    }

}
