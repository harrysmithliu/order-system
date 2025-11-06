package com.harry.order.exception;

import com.harry.order.common.ErrorCode;

public class NotFoundException extends BusinessException {

    public NotFoundException(String resourceName) {
        super(ErrorCode.ORDER_NOT_FOUND, resourceName + "不存在");
    }

}