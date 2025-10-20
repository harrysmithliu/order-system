package com.example.order.service;

import com.example.order.common.PageResult;
import com.example.order.dto.request.CreateOrderDTO;
import com.example.order.dto.request.OrderQueryDTO;
import com.example.order.dto.response.OrderDetailDTO;
import com.example.order.dto.response.OrderListDTO;

public interface OrderService {

    /**
     * 分页查询订单列表（MyBatis实现）
     *
     * @param query 查询条件
     * @return 分页结果
     */
    PageResult<OrderListDTO> getOrderList(OrderQueryDTO query);

    /**
     * 分页查询订单列表（JPA实现）
     *
     * @param query 查询条件
     * @return 分页结果
     */
    PageResult<OrderListDTO> getOrderListByJpa(OrderQueryDTO query);

    /**
     * 获取订单详情（带缓存）
     *
     * @param orderId 订单ID
     * @return 订单详情
     */
    OrderDetailDTO getOrderDetail(Long orderId);

    /**
     * 根据订单号获取详情
     *
     * @param orderNo 订单号
     * @return 订单详情
     */
    OrderDetailDTO getOrderDetailByNo(String orderNo);

    /**
     * 创建订单（事务控制）
     *
     * @param dto 创建订单请求
     * @return 订单详情
     */
    OrderDetailDTO createOrder(CreateOrderDTO dto);

    /**
     * 支付订单
     *
     * @param orderId 订单ID
     * @return 是否成功
     */
    boolean payOrder(Long orderId);

    /**
     * 取消订单
     *
     * @param orderId 订单ID
     * @param reason 取消原因
     * @return 是否成功
     */
    boolean cancelOrder(Long orderId, String reason);

    /**
     * 发货
     *
     * @param orderId 订单ID
     * @param trackingNo 物流单号
     * @return 是否成功
     */
    boolean shipOrder(Long orderId, String trackingNo);

    /**
     * 完成订单
     *
     * @param orderId 订单ID
     * @return 是否成功
     */
    boolean completeOrder(Long orderId);

    /**
     * 统计用户订单数量
     *
     * @param userId 用户ID
     * @return 订单数量
     */
    Long countUserOrders(Long userId);

    /**
     * 计算用户总消费金额
     *
     * @param userId 用户ID
     * @return 总金额
     */
    java.math.BigDecimal calculateUserTotalAmount(Long userId);

}
