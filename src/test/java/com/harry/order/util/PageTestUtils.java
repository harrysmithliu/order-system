package com.harry.order.util;

import com.harry.order.common.PageResult;
import com.harry.order.model.bo.OrderStatus;
import com.harry.order.model.vo.OrderSummaryVO;
import org.springframework.data.domain.*;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * 简单的分页工具类，用于测试
 */
public class PageTestUtils {

    /**
     * 创建一个空列表分页（默认内容为空）
     */
    public static <T> PageResult<T> emptyPage(int size) {
        //return new PageImpl<>(Collections.emptyList(), PageRequest.of(0, size), 0);
        Page<T> p = new PageImpl<>(Collections.emptyList(), PageRequest.of(0, size), 0);
        return new PageResult<>(p.getContent(), p.getNumber(), p.getSize(), p.getTotalElements());
    }

    /**
     * 创建一个包含指定数量元素的分页
     * 用于测试 list() 或分页接口返回结构
     */
    public static PageResult<OrderSummaryVO> pageOf(int size) {
        List<OrderSummaryVO> list = new ArrayList<>();
        for (int i = 1; i <= size; i++) {
            OrderSummaryVO dto = new OrderSummaryVO();
            dto.setId((long) i);
            dto.setOrderNo("ORD-" + i);
            dto.setUsername("User" + i);
            dto.setProductName("Product" + i);
            dto.setQuantity(i);
            dto.setTotalAmount(BigDecimal.valueOf(i * 100.00));
            dto.setStatus(OrderStatus.fromCode(1));
            dto.setCreateTime(java.time.LocalDateTime.now());
            list.add(dto);
        }
        //        return new PageImpl<>(list, PageRequest.of(0, size), list.size());
        Page<OrderSummaryVO> p = new PageImpl<>(list, PageRequest.of(0, size), list.size());
        return new PageResult<>(p.getContent(), p.getNumber(), p.getSize(), p.getTotalElements());
    }

    /**
     * 自定义数据列表分页
     */
    public static <T> PageResult<T> pageOf(List<T> content, int page, int size) {
        //return new PageImpl<>(content, PageRequest.of(page, size), content.size());
        Page<T> p = new PageImpl<>(content, PageRequest.of(page, size), content.size());
        return new PageResult<>(p.getContent(), p.getNumber(), p.getSize(), p.getTotalElements());
    }
}
