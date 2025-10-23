package com.harry.order.service;

import com.harry.order.domain.OrderStatus;
import com.harry.order.repository.OrderRepository;
import com.harry.order.service.dto.OrderSummaryDTO;
import org.springframework.beans.factory.annotation.Autowired;
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
        return orderRepository.findOrderSummaries(status, userId, productId, createdAfter, createdBefore, keyword, pageable);
    }
}
