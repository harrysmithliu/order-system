package com.harry.order.dto.response;

import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
public class OrderListDTO {
    private Long id;
    private String orderNo;
    private Long userId;
    private String username;
    private String nickname;
    private Long productId;
    private String productName;
    private Integer quantity;
    private BigDecimal totalAmount;
    private Integer status;
    private String statusName;
    private LocalDateTime createTime;
}
