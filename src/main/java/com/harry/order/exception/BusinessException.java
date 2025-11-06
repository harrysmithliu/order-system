package com.harry.order.exception;

import com.harry.order.common.ErrorCode;

public class BusinessException extends RuntimeException {
    private final String code;

    /**
     * 使用 ErrorCode 构造
     */
    public BusinessException(ErrorCode errorCode) {
        super(errorCode.getMessage());
        this.code = errorCode.getCode();
    }

    /**
     * 使用 ErrorCode 并自定义消息
     */
    public BusinessException(ErrorCode errorCode, String customMessage) {
        super(customMessage);
        this.code = errorCode.getCode();
    }

    /**
     * 向后兼容：直接传入 code 和 message（不推荐）
     */
    @Deprecated
    public BusinessException(String code, String message) {
        super(message);
        this.code = code;
    }

    public String getCode() {
        return code;
    }
}