package com.harry.order.event;

import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class OrderEventListener {
    /**
     * 监听 order.events 队列
     * @param event 收到的订单事件对象
     */
    @RabbitListener(queues = "order.events")
    public void handleOrderEvent(OrderEvent event) {
        log.info("[OrderEventListener] Received order event: {}", event);
        // TODO
        // 当前阶段：只是打印，验证收发闭环
        // 后续你可以在这里加业务逻辑，例如通知用户、刷新缓存等
    }
}
