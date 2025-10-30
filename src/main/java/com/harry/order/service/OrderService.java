package com.harry.order.service;

import com.harry.order.common.BadRequestException;
import com.harry.order.domain.OrderStatus;
import com.harry.order.repository.OrderRepository;
import com.harry.order.service.dto.OrderSummaryDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
public class OrderService {

    @Autowired
    private OrderRepository orderRepository;

    /**
     * 在 Service 的分页查询方法上加 @Cacheable，并指定 key 生成器
     * @param status
     * @param userId
     * @param productId
     * @param createdAfter
     * @param createdBefore
     * @param keyword
     * @param page
     * @param size
     * @return
     */
    @Cacheable(cacheNames = "order:pages", keyGenerator = "queryKey",
            unless = "#result == null || #result.isEmpty()")
    public Page<OrderSummaryDTO> getOrderSummaries(
            OrderStatus status,
            Long userId,
            Long productId,
            LocalDateTime createdAfter,
            LocalDateTime createdBefore,
            String keyword,
            int page,
            int size
    ) {
        Pageable pageable = PageRequest.of(page, size, Sort.by("createTime").descending());
        if (page < 0 || size <= 0) {
            throw new BadRequestException("page/size must be positive");
        }
        return orderRepository.findOrderSummaries(status, userId, productId, createdAfter, createdBefore, keyword, pageable);
    }
}
