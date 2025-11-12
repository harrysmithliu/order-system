package com.harry.order.model.po;

import com.harry.order.model.bo.OrderStatus;
import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Table(name = "t_order",
        indexes = {
                @Index(name = "uk_order_no", columnList = "order_no", unique = true),
                @Index(name = "idx_user_id", columnList = "user_id"),
                @Index(name = "idx_product_id", columnList = "product_id"),
                @Index(name = "idx_status", columnList = "status"),
                @Index(name = "idx_total_amount", columnList = "total_amount"),
                @Index(name = "idx_create_time", columnList = "create_time"),
                @Index(name = "idx_create_time_status", columnList = "create_time,status"),
                @Index(name = "idx_user_status_time", columnList = "user_id,status,create_time"),
                @Index(name = "idx_product_status_time", columnList = "product_id,status,create_time")
        })
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@ToString(exclude = {"user","product"})
public class Order {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "order_no", nullable = false, length = 32)
    private String orderNo;

    /** 大表场景：单向 LAZY 关联，禁止级联，避免放回 JSON 时无限递归 */
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "user_id", nullable = false)
//    @BatchSize(size = 64) // 批量抓取，减少 N+1
    private User user;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "product_id", nullable = false)
//    @BatchSize(size = 64)
    private Product product;

    @Column(nullable = false)
    private Integer quantity;

    @Column(name = "total_amount", nullable = false, precision = 10, scale = 2)
    private BigDecimal totalAmount;

    /** 使用枚举 + 转换器映射到 TINYINT */
    @Column(name = "status", nullable = false)
    private OrderStatus status;

    /** 交给数据库维护（DEFAULT/ON UPDATE），避免应用层写放大 */
    @Column(name = "create_time", nullable = false, insertable = false, updatable = false)
    private LocalDateTime createTime;

    @Column(name = "update_time", nullable = false, insertable = false, updatable = false)
    private LocalDateTime updateTime;
}
