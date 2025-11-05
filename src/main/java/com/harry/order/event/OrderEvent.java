package com.harry.order.event;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;
import java.io.Serializable;
import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class OrderEvent implements Serializable {

    private Long orderId;          // 订单ID
    private String eventType;      // 事件类型: created / updated / canceled
    private String description;    // 可选的说明或附加信息
    private LocalDateTime occurredAt;  // 事件发生时间

    public OrderEvent(Long orderId, String eventType, String description) {
        this.orderId = orderId;
        this.eventType = eventType;
        this.description = description;
        this.occurredAt = LocalDateTime.now(); // 自动填充时间
    }
}
