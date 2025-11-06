package com.harry.order.service;

import com.harry.order.config.RabbitMQConfig;
import com.harry.order.domain.Order;
import com.harry.order.domain.OrderStatus;
import com.harry.order.event.OrderEvent;
import com.harry.order.exception.NotFoundException;
import com.harry.order.repository.OrderRepository;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Caching;
import org.springframework.stereotype.Service;

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
                .orElseThrow(() -> new NotFoundException("订单不存在: " + orderNo));

        order.setStatus(OrderStatus.CANCELED);
        repo.save(order);

        // 2. 构造事件对象
        OrderEvent event = new OrderEvent(orderNo, "CANCELED", "订单已取消");

        // 3. 发送事件到 RabbitMQ
        rabbitTemplate.convertAndSend(
                RabbitMQConfig.ORDER_TOPIC_EXCHANGE, // 交换机
                "order.canceled",                   // routing key
                event                               // 消息体
        );

        log.info("[ORDER_EVENT] 已发布事件到MQ: {}", event);
    }

    // 更新状态、发货、支付等都同理做 Evict
}
