package com.harry.order.controller;


import com.harry.order.common.PageResult;
import com.harry.order.dto.request.OrderQueryDTO;
import com.harry.order.dto.response.OrderDetailDTO;
import com.harry.order.dto.response.OrderListDTO;
import com.harry.order.service.OrderService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/orders")
@Slf4j
public class OrderController {
    @Autowired
    private OrderService orderService;

    /**
     * 分页查询订单列表
     *
     * GET /api/orders/list?userId=1&status=1&pageNum=1&pageSize=20
     */
    @GetMapping("/list")
    public PageResult<OrderListDTO> getOrderList(OrderQueryDTO query) {
        log.info("接收查询请求: {}", query);
        return orderService.getOrderList(query);
    }

    /**
     * 查询订单详情
     *
     * GET /api/orders/1
     */
    @GetMapping("/{orderId}")
    public OrderDetailDTO getOrderDetail(@PathVariable Long orderId) {
        log.info("查询订单详情: orderId={}", orderId);
        return orderService.getOrderDetail(orderId);
    }
}
