package com.example.order.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

// enums/OrderStatusEnum.java
@Getter
@AllArgsConstructor
public enum OrderStatusEnum {

    PENDING(0, "待支付"),
    PAID(1, "已支付"),
    SHIPPED(2, "已发货"),
    COMPLETED(3, "已完成"),
    CANCELLED(4, "已取消");

    private final Integer code;
    private final String desc;

    public static OrderStatusEnum getByCode(Integer code) {
        for (OrderStatusEnum status : values()) {
            if (status.getCode().equals(code)) {
                return status;
            }
        }
        return null;
    }

    public static String getDescByCode(Integer code) {
        OrderStatusEnum status = getByCode(code);
        return status != null ? status.getDesc() : "未知";
    }
}