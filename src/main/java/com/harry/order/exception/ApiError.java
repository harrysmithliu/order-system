package com.harry.order.exception;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.OffsetDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ApiError {
    private String code;              // 业务错误码（如 ORDER_NOT_FOUND）
    private String message;           // 给前端看的提示
    private String detail;            // 便于排查的细节（可选）
    private String path;              // 请求路径
    private OffsetDateTime timestamp; // 出错时间
}
