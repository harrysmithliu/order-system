package com.harry.order.event;

import com.harry.order.domain.OrderStatus;
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

    private String orderNo;      // 订单No
    private OrderStatus status;      // 订单状态
    private String description;    // 可选的说明或附加信息
    private LocalDateTime occurredAt;  // 事件发生时间

    public OrderEvent(String orderNo, OrderStatus status, String description) {
        this.orderNo = orderNo;
        this.status = status;
        this.description = description;
        this.occurredAt = LocalDateTime.now(); // 自动填充时间
    }
}
