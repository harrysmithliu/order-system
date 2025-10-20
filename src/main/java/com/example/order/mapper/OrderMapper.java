package com.example.order.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.example.order.dto.response.OrderDetailDTO;
import com.example.order.dto.response.OrderListDTO;
import com.example.order.dto.request.OrderQueryDTO;
import com.example.order.entity.Order;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.data.domain.Page;
import org.springframework.data.repository.query.Param;

@Mapper
public interface OrderMapper extends BaseMapper<Order> {

    /**
     * 分页查询订单列表（三表联查）
     */
    IPage<OrderListDTO> selectOrderList(Page<OrderListDTO> page, @Param("query") OrderQueryDTO query);

    /**
     * 查询订单详情（三表联查）
     */
    OrderDetailDTO selectOrderDetail(@Param("orderId") Long orderId);

    /**
     * 根据订单号查询详情
     */
    OrderDetailDTO selectOrderDetailByNo(@Param("orderNo") String orderNo);
}