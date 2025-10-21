package com.example.order.repository;

import com.example.order.entity.Order;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface OrderRepository extends JpaRepository<Order, Long>,
        JpaSpecificationExecutor<Order> {

    /**
     * 根据订单号查询
     * 方法名查询：JPA会自动实现
     */
    Optional<Order> findByOrderNo(String orderNo);

    /**
     * 根据用户ID分页查询
     */
    Page<Order> findByUserId(Long userId, Pageable pageable);

    /**
     * 根据用户ID和状态查询
     */
    Page<Order> findByUserIdAndStatus(Long userId, Integer status, Pageable pageable);

    /**
     * 查询订单详情（JOIN FETCH 避免N+1问题）
     * 使用JPQL手动关联查询
     */
    @Query("SELECT o FROM Order o " +
            "LEFT JOIN FETCH o.user u " +
            "LEFT JOIN FETCH o.product p " +
            "WHERE o.id = :orderId")
    Optional<Order> findByIdWithDetails(@Param("orderId") Long orderId);
}
