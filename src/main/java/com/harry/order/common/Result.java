package com.harry.order.common;

import lombok.Getter;

import java.time.LocalDateTime;

@Getter
public class Result<T> {
    private int code;
    private String message;
    private T data;
    private String timestamp;

    // 成功响应
    public static <T> Result<T> success() {
        return success(null);
    }

    public static <T> Result<T> success(T data) {
        Result<T> result = new Result<>();
        result.code = 200;
        result.message = "success";
        result.data = data;
        result.timestamp = LocalDateTime.now().toString();
        return result;
    }

    // 失败响应
    public static <T> Result<T> fail(int code, String message) {
        Result<T> result = new Result<>();
        result.code = code;
        result.message = message;
        result.timestamp = LocalDateTime.now().toString();
        return result;
    }

}
