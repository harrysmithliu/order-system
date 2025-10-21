package com.harry.order.entity;

import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * 订单实体类
 */
@Entity
@Table(name = "t_order")
@EntityListeners(AuditingEntityListener.class)
@Data
@NoArgsConstructor
@AllArgsConstructor
@DynamicUpdate
@DynamicInsert
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE, region = "orderCache")
public class Order implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "order_no")
    private String orderNo;

    @Column(name = "user_id")
    private Long userId;

    /**
     * 用户对象 - 多对一关联
     * 使用懒加载避免N+1问题
     */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", insertable = false, updatable = false)
    private User user;

    @Column(name = "product_id")
    private Long productId;

    /**
     * 商品对象 - 多对一关联
     */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "product_id", insertable = false, updatable = false)
    private Product product;

    private Integer quantity;

    @Column(name = "total_amount")
    private BigDecimal totalAmount;

    private Integer status;

    @CreatedDate
    @Column(name = "create_time", updatable = false)
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private LocalDateTime createTime;

    @LastModifiedDate
    @Column(name = "update_time")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private LocalDateTime updateTime;

    private Integer deleted;

    @Version
    private Integer version;

    /**
     * 获取状态名称
     */
    @Transient
    public String getStatusName() {
        return switch (status) {
            case 0 -> "待支付";
            case 1 -> "已支付";
            case 2 -> "已发货";
            case 3 -> "已完成";
            case 4 -> "已取消";
            default -> "未知";
        };
    }

    /**
     * 业务方法：支付订单
     */
    public void pay() {
        if (this.status != 0) {
            throw new IllegalStateException("只有待支付订单才能支付");
        }
        this.status = 1;
    }

    /**
     * 业务方法：发货
     */
    public void ship() {
        if (this.status != 1) {
            throw new IllegalStateException("只有已支付订单才能发货");
        }
        this.status = 2;
    }

    /**
     * 业务方法：完成订单
     */
    public void complete() {
        if (this.status != 2) {
            throw new IllegalStateException("只有已发货订单才能完成");
        }
        this.status = 3;
    }

    /**
     * 业务方法：取消订单
     */
    public void cancel() {
        if (this.status > 1) {
            throw new IllegalStateException("订单已发货，不能取消");
        }
        this.status = 4;
    }

    /**
     * 生命周期回调 - 持久化前
     */
    @PrePersist
    public void prePersist() {
        if (createTime == null) {
            createTime = LocalDateTime.now();
        }
        if (updateTime == null) {
            updateTime = LocalDateTime.now();
        }
        if (status == null) {
            status = 0;
        }
        if (deleted == null) {
            deleted = 0;
        }
    }

    /**
     * 生命周期回调 - 更新前
     */
    @PreUpdate
    public void preUpdate() {
        updateTime = LocalDateTime.now();
    }
}