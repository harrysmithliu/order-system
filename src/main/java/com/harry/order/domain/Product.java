package com.harry.order.domain;


import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.BatchSize;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@BatchSize(size = 64)
@Entity
@Table(name = "t_product",
        indexes = {
                @Index(name = "uk_product_code", columnList = "product_code", unique = true),
                @Index(name = "idx_category", columnList = "category")
        })
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Product {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "product_name", nullable = false, length = 200)
    private String productName;

    @Column(name = "product_code", nullable = false, length = 50)
    private String productCode;

    @Column(nullable = false, precision = 10, scale = 2)
    private BigDecimal price;

    @Column(nullable = false)
    private Integer stock;

    @Column(length = 50)
    private String category;

    @Column(name = "create_time", nullable = false, insertable = false, updatable = false)
    private LocalDateTime createTime;
}
