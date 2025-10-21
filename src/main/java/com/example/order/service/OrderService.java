package com.example.order.service;

import com.example.order.common.PageResult;
import com.example.order.dto.request.OrderQueryDTO;
import com.example.order.dto.response.OrderDetailDTO;
import com.example.order.dto.response.OrderListDTO;

public interface OrderService {

    /**
     * 分页查询订单列表（带条件）
     *
     * @param query 查询条件
     * @return 分页结果
     */
    PageResult<OrderListDTO> getOrderList(OrderQueryDTO query);

    /**
     * 根据ID查询订单详情
     *
     * @param orderId 订单ID
     * @return 订单详情
     */
    OrderDetailDTO getOrderDetail(Long orderId);

}
