package com.harry.order.repository;

import com.harry.order.domain.Order;
import com.harry.order.domain.OrderStatus;
import com.harry.order.service.dto.OrderSummaryDTO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDateTime;

public interface OrderRepository extends JpaRepository<Order, Long> {
    /**
     * 分页查询 + 动态条件（可按需传参；null 表示不启用该过滤）。
     * - 支持：status / userId / productId / 时间范围 / 关键词（匹配订单号、商品名、用户名）
     * - 返回轻量 DTO，避免实体序列化 & N+1
     */
    @Query("SELECT new com.harry.order.service.dto.OrderSummaryDTO(" +
            " o.id, o.orderNo, u.id, u.username, p.id, p.productName, " +
            " o.quantity, o.totalAmount, o.status, o.createTime, o.updateTime) " +
            // 这里改成“全限定名”，避免与关键字 ORDER 冲突
            "FROM com.harry.order.domain.Order o " +
            "JOIN o.user u " +
            "JOIN o.product p " +
            "WHERE (:status IS NULL OR o.status = :status) " +
            "AND (:userId IS NULL OR u.id = :userId) " +
            "AND (:productId IS NULL OR p.id = :productId) " +
            "AND (:createdAfter IS NULL OR o.createTime >= :createdAfter) " +
            "AND (:createdBefore IS NULL OR o.createTime < :createdBefore) " +
            "AND (:keyword IS NULL OR o.orderNo LIKE CONCAT('%', :keyword, '%') " +
            " OR p.productName LIKE CONCAT('%', :keyword, '%') " +
            " OR u.username LIKE CONCAT('%', :keyword, '%'))")
    Page<OrderSummaryDTO> findOrderSummaries(
            @Param("status") OrderStatus status,
            @Param("userId") Long userId,
            @Param("productId") Long productId,
            @Param("createdAfter") LocalDateTime createdAfter,
            @Param("createdBefore") LocalDateTime createdBefore,
            @Param("keyword") String keyword,
            Pageable pageable
    );
}
