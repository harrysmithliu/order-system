package com.harry.order.controller;

import com.harry.order.ValidationConfig;
import com.harry.order.common.PageResult;
import com.harry.order.service.OrderQueryService;
import com.harry.order.model.vo.OrderSummaryVO;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(OrderController.class)
@Import(ValidationConfig.class)  // 引入下面的配置类
public class OrderControllerTest {

    @Autowired
    MockMvc mvc;

    @MockBean
    OrderQueryService orderQueryService;

    @Test
    void list_shouldReturn400_whenParamInvalid() throws Exception {
        mvc.perform(get("/api/orders?page=-1&size=0"))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.code").value("VALIDATION_ERROR"));
    }

    @Test
    void list_shouldReturn200_whenOK() throws Exception {
        // 创建真实的测试数据
        OrderSummaryVO order = new OrderSummaryVO();
        order.setId(1L);
        order.setOrderNo("ORD-001");
        order.setUsername("testUser");
        order.setProductName("Test Product");
        order.setQuantity(1);
        order.setTotalAmount(BigDecimal.valueOf(100.00));
        order.setCreateTime(LocalDateTime.now());

        List<OrderSummaryVO> orders = List.of(order);
        PageResult<OrderSummaryVO> mockPage = new PageResult<>(orders, 0, 10, 1L);

        // 关键：使用 ArgumentMatchers 确保 Mock 匹配所有可能的参数组合
        when(orderQueryService.getOrderSummaries(
                isNull(),           // status - null
                isNull(),           // userId - null
                isNull(),           // productId - null
                isNull(),           // startTime - null
                isNull(),           // endTime - null
                eq(""),             // search - 空字符串
                eq(0),              // page - 0
                eq(10)              // size - 10
        )).thenReturn(mockPage);

        // 或者使用更宽松的 Mock 来调试
//        when(orderService.getOrderSummaries(
//                any(), any(), any(), any(), any(), anyString(), anyInt(), anyInt()
//        )).thenReturn(mockPage);
        when(orderQueryService.getOrderSummaries(any(), any(), any(), any(), any(), any(), anyInt(), anyInt()))
                .thenAnswer(invocation -> {
                    System.out.println("Mock 被调用了！");
                    return mockPage;
                });

        mvc.perform(get("/api/orders?page=0&size=10"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content").isArray());
    }

    @Test
    void list_shouldReturn200_withMultipleOrders() throws Exception {
        // 测试返回多条订单
        List<OrderSummaryVO> orders = List.of(
                createOrderSummary(1L, "ORD-001"),
                createOrderSummary(2L, "ORD-002"),
                createOrderSummary(3L, "ORD-003")
        );
        PageResult<OrderSummaryVO> mockPage = new PageResult<>(orders, 0, 10, 3L);

//        when(orderService.getOrderSummaries(
//                any(), any(), any(), any(), any(), anyString(), anyInt(), anyInt()
//        )).thenReturn(mockPage);
        when(orderQueryService.getOrderSummaries(any(), any(), any(), any(), any(), any(), anyInt(), anyInt()))
                .thenAnswer(invocation -> {
                    System.out.println("Mock 被调用了！");
                    return mockPage;
                });

        mvc.perform(get("/api/orders?page=0&size=10"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content").isArray())
                .andExpect(jsonPath("$.content.length()").value(3));
    }

    // 辅助方法：创建测试订单
    private OrderSummaryVO createOrderSummary(Long id, String orderNo) {
        OrderSummaryVO dto = new OrderSummaryVO();
        dto.setId(id);
        dto.setOrderNo(orderNo);
        dto.setUsername("User" + id);
        dto.setProductName("Product" + id);
        dto.setQuantity(1);
        dto.setTotalAmount(BigDecimal.valueOf(100.00));
        dto.setCreateTime(LocalDateTime.now());
        return dto;
    }

    @Test
    void list_printJsonUsingMvcResult() throws Exception {
        List<OrderSummaryVO> orders = List.of(
                createOrderSummary(1L, "ORD-001"),
                createOrderSummary(2L, "ORD-002"),
                createOrderSummary(3L, "ORD-003")
        );
        PageResult<OrderSummaryVO> mockPage = new PageResult<>(orders, 0, 10, 3L);

        when(orderQueryService.getOrderSummaries(any(), any(), any(), any(), any(), any(), anyInt(), anyInt()))
                .thenAnswer(invocation -> {
                    System.out.println("Mock 被调用了！");
                    return mockPage;
                });

        MvcResult result = mvc.perform(get("/api/orders?page=0&size=10"))
                .andExpect(status().isOk())
                .andReturn();

        String json = result.getResponse().getContentAsString();
        System.out.println("响应 JSON: " + json);
    }
}
