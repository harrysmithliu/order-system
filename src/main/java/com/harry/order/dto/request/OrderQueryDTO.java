package com.harry.order.dto.request;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class OrderQueryDTO {
    /**
     * 用户ID
     */
    private Long userId;

    /**
     * 订单状态
     */
    private Integer status;

    /**
     * 订单号
     */
    private String orderNo;

    /**
     * 开始时间
     */
    private LocalDateTime startTime;

    /**
     * 结束时间
     */
    private LocalDateTime endTime;

    /**
     * 页码（从1开始）
     */
    private Integer pageNum = 1;

    /**
     * 每页大小
     */
    private Integer pageSize = 20;
}
