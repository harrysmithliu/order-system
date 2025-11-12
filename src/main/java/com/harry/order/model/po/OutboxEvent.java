package com.harry.order.model.po;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "outbox_event")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class OutboxEvent {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String aggregateType; // e.g. "order"
    private String aggregateId;   // e.g. orderId
    private String eventType;     // e.g. "OrderCreated", "OrderUpdated"

    @Lob
    private String payload;       // JSON payload of the event

    private LocalDateTime createdAt = LocalDateTime.now();

    private boolean processed = false; // default false
}
