package com.harry.order.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.harry.order.domain.OutboxEvent;
import com.harry.order.repository.OutboxEventRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class OutboxService {
    private final OutboxEventRepository outboxRepo;
    private final ObjectMapper objectMapper;

    public void saveEvent(String aggregateType, String aggregateId, String eventType, Object payload) {
        try {
            String json = objectMapper.writeValueAsString(payload);
            OutboxEvent event = OutboxEvent.builder()
                    .aggregateType(aggregateType)
                    .aggregateId(aggregateId)
                    .eventType(eventType)
                    .payload(json)
                    .build();
            outboxRepo.save(event);
            log.info("Outbox event saved: {} {}", eventType, aggregateId);
        } catch (Exception e) {
            log.error("Failed to serialize outbox event: {}", e.getMessage());
        }
    }
}
