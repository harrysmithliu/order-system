package com.example.order.dto.response;

import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
public class OrderDetailDTO {
    private Long id;
    private String orderNo;
    private Long userId;
    private String username;
    private String nickname;
    private String phone;
    private String email;
    private Long productId;
    private String productName;
    private String productCode;
    private BigDecimal price;
    private Integer quantity;
    private BigDecimal totalAmount;
    private Integer status;
    private String statusName;
    private LocalDateTime createTime;
    private LocalDateTime updateTime;
}
