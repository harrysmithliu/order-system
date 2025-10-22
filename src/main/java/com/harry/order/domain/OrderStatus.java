package com.harry.order.domain;

public enum OrderStatus {
    PENDING_PAY(0), PAID(1), SHIPPED(2), COMPLETED(3), CANCELED(4);

    private final int code;

    OrderStatus(int code) {
        this.code = code;
    }

    public int getCode() {
        return code;
    }

    public static OrderStatus fromCode(Integer code) {
        if (code == null) return null;
        for (OrderStatus s : values()) if (s.code == code) return s;
        throw new IllegalArgumentException("Unknown order status: " + code);
    }
}
