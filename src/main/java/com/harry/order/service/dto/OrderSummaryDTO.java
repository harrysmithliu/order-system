package com.harry.order.service.dto;

import com.harry.order.domain.OrderStatus;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * OrderSummaryDTO
 *
 * 用于 REST 接口返回的订单列表摘要视图。
 * 只包含关键信息，不携带嵌套对象，便于序列化与缓存。
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class OrderSummaryDTO {
    /** 订单主键 */
    private Long id;

    /** 订单号 */
    private String orderNo;

    /** 用户信息（仅ID与用户名） */
    private Long userId;
    private String username;

    /** 商品信息（仅ID与名称） */
    private Long productId;
    private String productName;

    /** 数量与金额 */
    private Integer quantity;
    private BigDecimal totalAmount;

    /** 订单状态 */
    private OrderStatus status;

    /** 创建与更新时间 */
    private LocalDateTime createTime;
    private LocalDateTime updateTime;
}
