package com.harry.order.scheduler;

import com.harry.order.domain.OutboxEvent;
import com.harry.order.repository.OutboxEventRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * ===============================================================
 *  Class: OutboxRelayScheduler
 *  Purpose:
 *    Periodically scans the outbox_event table for unprocessed
 *    events and publishes them to RabbitMQ. Once successfully sent,
 *    the event is marked as processed=true.
 * ===============================================================
 */
@Slf4j
@Component
@RequiredArgsConstructor
public class OutboxRelayScheduler {
    private final OutboxEventRepository outboxRepo;
    private final RabbitTemplate rabbitTemplate;

    /**
     * Every 10 seconds, fetch up to 100 unprocessed events
     * and relay them to RabbitMQ.
     */
    @Scheduled(fixedDelayString = "${outbox.scheduler.delay-ms:10000}")
    @Transactional
    public void relayOutboxEvents() {
        List<OutboxEvent> events = outboxRepo.findTop100ByProcessedFalseOrderByCreatedAtAsc();
        if (events.isEmpty()) return;

        log.info("Relaying {} outbox events to RabbitMQ ...", events.size());

        for (OutboxEvent event : events) {
            // 直接调用发送逻辑，异常交由 GlobalExceptionHandler 统一处理
            rabbitTemplate.convertAndSend(
                    "order.exchange",
                    "order." + event.getEventType().toLowerCase(),
                    event.getPayload()
            );

            event.setProcessed(true);
            log.info("Event sent: {} [{}]", event.getEventType(), event.getAggregateId());
        }
    }
}
