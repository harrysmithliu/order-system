package com.harry.order.service;

import com.harry.order.common.ErrorCode;
import com.harry.order.config.RabbitMQConfig;
import com.harry.order.domain.Order;
import com.harry.order.domain.OrderStatus;
import com.harry.order.event.OrderEvent;
import com.harry.order.exception.BusinessException;
import com.harry.order.exception.NotFoundException;
import com.harry.order.repository.OrderRepository;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Caching;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Slf4j
@Service
@AllArgsConstructor
public class OrderCommandService {
    private final OrderRepository repo;
    private final RabbitTemplate rabbitTemplate;

    @Caching(evict = {
            @CacheEvict(cacheNames = "order:byId", key = "#result.id", condition = "#result != null"),
            @CacheEvict(cacheNames = "order:pages", allEntries = true) // 页缓存整体失效（简单可靠）
    })
    public Order create(Order order) {
        return repo.save(order);
    }

    @Caching(evict = {
            @CacheEvict(cacheNames = "order:byNo", key = "#orderNo"),
            @CacheEvict(cacheNames = "order:pages", allEntries = true)
    })
    public void cancel(String orderNo) {
//        repo.deleteById(orderId);
        // 1. 数据层操作
        Order order = repo.findByOrderNo(orderNo)
                .orElseThrow(() -> new NotFoundException("订单 " + orderNo));

        // 2. 检查订单状态 - 如果已经是CANCELED则直接返回失败
        if (order.getStatus() == OrderStatus.CANCELED) {
            throw new BusinessException(ErrorCode.ORDER_ALREADY_CANCELED);
        }

        // 3. 检查订单是否允许取消（可选的业务规则）
        if (!isOrderCancelable(order)) {
            throw new BusinessException(ErrorCode.ORDER_CANNOT_CANCEL);
        }

        order.setStatus(OrderStatus.CANCELED);
        order.setUpdateTime(LocalDateTime.now());
        repo.save(order);

        // 发布事件
        publishOrderCanceledEvent(order);
    }

    /**
     * 判断订单是否可以取消
     * 业务规则: 仅PENDING_PAY和PAID状态可以取消
     */
    private boolean isOrderCancelable(Order order) {
        OrderStatus status = order.getStatus();
        return status == OrderStatus.PENDING_PAY || status == OrderStatus.PAID;
    }

    private void publishOrderCanceledEvent(Order order) {
        OrderEvent event = new OrderEvent(
                order.getOrderNo(),
                OrderStatus.CANCELED,
                "订单已取消"
        );
        rabbitTemplate.convertAndSend(
                RabbitMQConfig.ORDER_TOPIC_EXCHANGE, // 交换机
                "order.canceled",                    // routing key
                event                                // 消息体
        );
        log.info("[ORDER_EVENT] 已发布事件到MQ: {}", event);
    }

    // 更新状态、发货、支付等都同理做 Evict
}
