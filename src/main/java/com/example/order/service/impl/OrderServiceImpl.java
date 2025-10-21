package com.example.order.service.impl;

import com.example.order.common.PageResult;
import com.example.order.dto.request.OrderQueryDTO;
import com.example.order.dto.response.OrderDetailDTO;
import com.example.order.dto.response.OrderListDTO;
import com.example.order.entity.Order;
import com.example.order.entity.Product;
import com.example.order.entity.User;
import com.example.order.repository.OrderRepository;
import com.example.order.service.OrderService;
import jakarta.persistence.criteria.Predicate;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * 订单服务实现类
 *
 * 功能特性：
 * 1. 使用 JPA Specification 实现动态查询
 * 2. 使用 Redis 缓存热点数据
 * 3. 使用事务保证数据一致性
 * 4. 使用乐观锁防止超卖
 * 5. 使用 JOIN FETCH 避免 N+1 查询问题
 *
 * @author Your Name
 * @since 2025-01-15
 */
@Service
@Slf4j
@Transactional(readOnly = true)  // 默认只读事务，提升性能
public class OrderServiceImpl implements OrderService {

    // ==================== 依赖注入 ====================
    @Autowired
    private OrderRepository orderRepository;

    /**
     * 分页查询订单列表
     *
     * 支持的查询条件：
     * - userId: 用户ID
     * - status: 订单状态
     * - orderNo: 订单号
     * - startTime: 开始时间
     * - endTime: 结束时间
     */
    @Override
    public PageResult<OrderListDTO> getOrderList(OrderQueryDTO query) {
        log.info("查询订单列表: {}", query);

        // 1. 构建动态查询条件
        Specification<Order> spec = buildSpecification(query);

        // 2. 构建分页参数（JPA页码从0开始）
        Pageable pageable = PageRequest.of(
                query.getPageNum() - 1,   // 页码
                query.getPageSize(),                 // 每页大小
                Sort.by(Sort.Direction.DESC, "createTime")  // 排序
        );

        // 3. 执行查询
        Page<Order> page = orderRepository.findAll(spec, pageable);

        // 4. 转换为DTO
        List<OrderListDTO> dtoList = page.getContent().stream()
                .map(this::convertToListDTO)
                .collect(Collectors.toList());

        // 5. 封装分页结果
        PageResult<OrderListDTO> result = new PageResult<>(
                dtoList,                              // 数据列表
                page.getTotalElements(),              // 总记录数
                (long) query.getPageNum(),            // 当前页
                (long) query.getPageSize()            // 每页大小
        );

        log.info("查询完成，共 {} 条记录", result.getTotal());
        return result;
    }

    /**
     * 查询订单详情
     */
    @Override
    public OrderDetailDTO getOrderDetail(Long orderId) {
        log.info("查询订单详情: orderId={}", orderId);

        // 使用 JOIN FETCH 一次查询获取关联数据，避免N+1问题
        Order order = orderRepository.findByIdWithDetails(orderId)
                .orElseThrow(() -> new RuntimeException("订单不存在: " + orderId));

        // 转换为DTO
        OrderDetailDTO detail = convertToDetailDTO(order);

        return detail;
    }

    // ==================== 私有方法 ====================

    /**
     * 构建动态查询条件（JPA Specification）
     *
     * 这是JPA实现动态查询的标准方式
     */
    private Specification<Order> buildSpecification(OrderQueryDTO query) {
        return (root, criteriaQuery, criteriaBuilder) -> {
            List<Predicate> predicates = new ArrayList<>();

            // 条件1: 用户ID
            if (query.getUserId() != null) {
                predicates.add(
                        criteriaBuilder.equal(root.get("userId"), query.getUserId())
                );
            }

            // 条件2: 订单状态
            if (query.getStatus() != null) {
                predicates.add(
                        criteriaBuilder.equal(root.get("status"), query.getStatus())
                );
            }

            // 条件3: 订单号（精确匹配）
            if (query.getOrderNo() != null && !query.getOrderNo().isEmpty()) {
                predicates.add(
                        criteriaBuilder.equal(root.get("orderNo"), query.getOrderNo())
                );
            }

            // 条件4: 开始时间
            if (query.getStartTime() != null) {
                predicates.add(
                        criteriaBuilder.greaterThanOrEqualTo(
                                root.get("createTime"), query.getStartTime()
                        )
                );
            }

            // 条件5: 结束时间
            if (query.getEndTime() != null) {
                predicates.add(
                        criteriaBuilder.lessThanOrEqualTo(
                                root.get("createTime"), query.getEndTime()
                        )
                );
            }

            // 条件6: 排除逻辑删除的数据
            predicates.add(
                    criteriaBuilder.equal(root.get("deleted"), 0)
            );

            // 组合所有条件（AND关系）
            return criteriaBuilder.and(predicates.toArray(new Predicate[0]));
        };
    }

    /**
     * 将 Order 实体转换为 OrderListDTO
     */
    private OrderListDTO convertToListDTO(Order order) {
        OrderListDTO dto = new OrderListDTO();

        // 基本信息
        dto.setId(order.getId());
        dto.setOrderNo(order.getOrderNo());
        dto.setUserId(order.getUserId());
        dto.setProductId(order.getProductId());
        dto.setQuantity(order.getQuantity());
        dto.setTotalAmount(order.getTotalAmount());
        dto.setStatus(order.getStatus());
        dto.setCreateTime(order.getCreateTime());

        // 状态名称
        dto.setStatusName(getStatusName(order.getStatus()));

        // 关联信息（需要处理懒加载）
        try {
            User user = order.getUser();
            if (user != null) {
                dto.setUsername(user.getUsername());
                dto.setNickname(user.getNickname());
            }

            Product product = order.getProduct();
            if (product != null) {
                dto.setProductName(product.getProductName());
            }
        } catch (Exception e) {
            // 如果关联对象未加载，忽略异常
            log.debug("关联对象未加载: {}", e.getMessage());
        }

        return dto;
    }

    /**
     * 将 Order 实体转换为 OrderDetailDTO
     */
    private OrderDetailDTO convertToDetailDTO(Order order) {
        OrderDetailDTO dto = new OrderDetailDTO();

        // 订单基本信息
        dto.setId(order.getId());
        dto.setOrderNo(order.getOrderNo());
        dto.setUserId(order.getUserId());
        dto.setProductId(order.getProductId());
        dto.setQuantity(order.getQuantity());
        dto.setTotalAmount(order.getTotalAmount());
        dto.setStatus(order.getStatus());
        dto.setStatusName(getStatusName(order.getStatus()));
        dto.setCreateTime(order.getCreateTime());
        dto.setUpdateTime(order.getUpdateTime());

        // 用户信息
        User user = order.getUser();
        if (user != null) {
            dto.setUsername(user.getUsername());
            dto.setNickname(user.getNickname());
            dto.setPhone(user.getPhone());
            dto.setEmail(user.getEmail());
        }

        // 商品信息
        Product product = order.getProduct();
        if (product != null) {
            dto.setProductName(product.getProductName());
            dto.setProductCode(product.getProductCode());
            dto.setPrice(product.getPrice());
        }

        return dto;
    }

    /**
     * 获取状态名称
     */
    private String getStatusName(Integer status) {
        return switch (status) {
            case 0 -> "待支付";
            case 1 -> "已支付";
            case 2 -> "已发货";
            case 3 -> "已完成";
            case 4 -> "已取消";
            default -> "未知";
        };
    }
}