package com.harry.order.service.dto;

import com.harry.order.domain.OrderStatus;
import io.swagger.v3.oas.annotations.media.Schema;
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
@Schema(description = "Order summary information")
public class OrderSummaryDTO {

    /** 订单主键 */
    @Schema(description = "Order ID", example = "1001")
    private Long id;

    /** 订单号 */
    @Schema(description = "Order number", example = "ORD-20250310-0001")
    private String orderNo;

    /** 用户信息（仅ID与用户名） */
    @Schema(description = "User ID", example = "501")
    private Long userId;
    @Schema(description = "Username", example = "harryliu")
    private String username;

    /** 商品信息（仅ID与名称） */
    @Schema(description = "Product ID", example = "888")
    private Long productId;
    @Schema(description = "Product name", example = "iPhone 15 Pro")
    private String productName;

    /** 数量与金额 */
    @Schema(description = "Quantity purchased", example = "2")
    private Integer quantity;
    @Schema(description = "Total order amount", example = "1599.00")
    private BigDecimal totalAmount;

    /** 订单状态 */
    @Schema(description = "Order status", example = "PAID")
    private OrderStatus status;

    /** 创建与更新时间 */
    @Schema(description = "Creation time", example = "2025-03-10T14:23:00")
    private LocalDateTime createTime;
    @Schema(description = "Update time", example = "2025-03-10T16:00:00")
    private LocalDateTime updateTime;
}
