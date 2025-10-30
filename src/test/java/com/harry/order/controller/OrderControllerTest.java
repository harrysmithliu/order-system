package com.harry.order.controller;

import com.harry.order.service.OrderService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(OrderController.class)
public class OrderControllerTest {
    @Autowired
    MockMvc mvc;
    @MockBean
    OrderService orderService;

    @Test
    void list_shouldReturn400_whenParamInvalid() throws Exception {
        mvc.perform(get("/api/orders?page=-1&size=0"))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.code").value("VALIDATION_ERROR"));
    }

//    @Test
//    void list_shouldReturn200_whenOK() throws Exception {
//        when(orderService.getOrderSummaries(null, null, null, null, null, "", 0, 10))
//                .thenReturn(/* mock Page<OrderSummaryDTO> */ PageTestUtils.pageOf(10));
//
//        mvc.perform(get("/api/orders?page=0&size=10"))
//                .andExpect(status().isOk())
//                .andExpect(jsonPath("$.content").isArray());
//    }
}
