package com.example.order.dto;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class OrderQueryDTO {
    private Long userId;
    private Integer status;
    private String orderNo;
    private LocalDateTime startTime;
    private LocalDateTime endTime;
    private Integer pageNum = 1;
    private Integer pageSize = 20;
}
