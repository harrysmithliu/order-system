package com.harry.order.controller;

import com.harry.order.common.PageResult;
import com.harry.order.domain.OrderStatus;
import com.harry.order.exception.NotFoundException;
import com.harry.order.service.OrderService;
import com.harry.order.service.dto.OrderSummaryDTO;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;

@Tag(name = "Order API", description = "Order endpoints for pagination and filtering")
@CrossOrigin(origins = "http://localhost:5173")     //允许跨域（CORS）
@RestController
@RequestMapping("/api/orders")
public class OrderController {

    @Autowired
    private OrderService orderService;

    @Operation(
            summary = "Get paginated order list",
            description = "Filter orders by status, user, product, or date range with pagination support"
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Successfully retrieved orders",
                    content = @Content(schema = @Schema(implementation = OrderSummaryDTO.class))),
            @ApiResponse(responseCode = "404", description = "No orders found")
    })
    @GetMapping
    public PageResult<OrderSummaryDTO> getOrders(
            @Parameter(description = "Order status") @RequestParam(name = "status", required = false) OrderStatus status,
            @Parameter(description = "User ID") @RequestParam(name = "userId", required = false) Long userId,
            @Parameter(description = "Product ID") @RequestParam(name = "productId", required = false) Long productId,
            @Parameter(description = "Start creation time (ISO format)", example = "2024-01-01T00:00:00")
            @RequestParam(name = "createdAfter", required = false)
            @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime createdAfter,
            @Parameter(description = "End creation time (ISO format)", example = "2024-12-31T23:59:59")
            @RequestParam(name = "createdBefore", required = false)
            @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime createdBefore,
            @Parameter(description = "Keyword for fuzzy search") @RequestParam(name = "keyword", required = false) String keyword,
            @Parameter(description = "Page number (starting from 0)") @RequestParam(name = "page", defaultValue = "0") int page,
            @Parameter(description = "Page size") @RequestParam(name = "size", defaultValue = "10") int size
    ) {
        PageResult<OrderSummaryDTO> result = orderService.getOrderSummaries(status, userId, productId, createdAfter, createdBefore, keyword, page, size);
        if (result.getContent() == null || result.getContent().isEmpty()) {
            throw new NotFoundException("No orders found");
        }
        return result;
    }

}
