package com.harry.order.entity;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.*;
import org.hibernate.annotations.Cache;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

/**
 * 商品实体类
 */
@Entity
@Table(name = "t_product")
@EntityListeners(AuditingEntityListener.class)
@Data
@NoArgsConstructor
@AllArgsConstructor
@DynamicUpdate
@DynamicInsert
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE, region = "productCache")
public class Product implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "product_name")
    private String productName;

    @Column(name = "product_code")
    private String productCode;

    private BigDecimal price;

    private Integer stock;

    private String category;

    private String description;

    @Column(name = "image_url")
    private String imageUrl;

    private Integer status;

    @Column(name = "sales_count")
    private Integer salesCount;

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
     * 商品的所有订单 - 一对多关联
     */
    @OneToMany(mappedBy = "product", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @JsonIgnore
    @BatchSize(size = 10)
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    private List<Order> orders = new ArrayList<>();

    /**
     * 添加订单
     */
    public void addOrder(Order order) {
        orders.add(order);
        order.setProduct(this);
        order.setProductId(this.id);
    }

    /**
     * 业务方法：减少库存（带乐观锁）
     */
    public void decreaseStock(Integer quantity) {
        if (this.stock < quantity) {
            throw new IllegalStateException("库存不足，当前库存：" + this.stock);
        }
        this.stock -= quantity;
        this.salesCount += quantity;
    }

    /**
     * 业务方法：增加库存
     */
    public void increaseStock(Integer quantity) {
        this.stock += quantity;
    }

    /**
     * 业务方法：上架
     */
    public void shelf() {
        this.status = 1;
    }

    /**
     * 业务方法：下架
     */
    public void unshelf() {
        this.status = 0;
    }

    @PrePersist
    public void prePersist() {
        if (createTime == null) {
            createTime = LocalDateTime.now();
        }
        if (updateTime == null) {
            updateTime = LocalDateTime.now();
        }
        if (stock == null) {
            stock = 0;
        }
        if (status == null) {
            status = 1;
        }
        if (salesCount == null) {
            salesCount = 0;
        }
        if (deleted == null) {
            deleted = 0;
        }
    }

    @PreUpdate
    public void preUpdate() {
        updateTime = LocalDateTime.now();
    }
}