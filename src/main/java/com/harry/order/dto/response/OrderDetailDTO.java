package com.harry.order.dto.response;

import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
public class OrderDetailDTO {
    // 订单信息
    private Long id;
    private String orderNo;
    private Long userId;
    private Long productId;
    private Integer quantity;
    private BigDecimal totalAmount;
    private Integer status;
    private String statusName;
    private LocalDateTime createTime;
    private LocalDateTime updateTime;

    // 用户信息
    private String username;
    private String nickname;
    private String phone;
    private String email;

    // 商品信息
    private String productName;
    private String productCode;
    private BigDecimal price;
}
