package com.harry.order.controller;

import com.harry.order.common.PageResult;
import com.harry.order.common.Result;
import com.harry.order.domain.OrderStatus;
import com.harry.order.exception.ApiError;
import com.harry.order.exception.BusinessException;
import com.harry.order.exception.NotFoundException;
import com.harry.order.service.OrderCommandService;
import com.harry.order.service.OrderQueryService;
import com.harry.order.service.dto.OrderSummaryDTO;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.constraints.Min;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;

@Slf4j
@Tag(name = "Order API", description = "Order endpoints for pagination and filtering")
@CrossOrigin(origins = "http://localhost:5173")     //允许跨域（CORS）
@RestController
@RequestMapping("/api/orders")
@SecurityRequirement(name = "bearerAuth") // apply JWT requirement to this controller
public class OrderController {

    @Autowired
    private OrderQueryService orderQueryService;

    @Autowired
    private OrderCommandService orderCommandService;

    @Operation(
            summary = "Get paginated order list",
            description = "Filter orders by status, user, product, or date range with pagination support"
    )
    @ApiResponses({
            @ApiResponse(
                    responseCode = "200",
                    description = "Successfully retrieved orders",
                    content = @Content(schema = @Schema(implementation = PageResult.class))
            ),
            @ApiResponse(
                    responseCode = "400",
                    description = "Invalid query parameters (e.g., page < 0, size < 1)",
                    content = @Content(schema = @Schema(implementation = ApiError.class))
            ),
            @ApiResponse(
                    responseCode = "404",
                    description = "No orders found matching the criteria",
                    content = @Content(schema = @Schema(implementation = ApiError.class))
            ),
            @ApiResponse(
                    responseCode = "500",
                    description = "Internal server error",
                    content = @Content(schema = @Schema(implementation = ApiError.class))
            )
    })
    @GetMapping
    public PageResult<OrderSummaryDTO> getOrders(
            HttpServletRequest request,
            @Parameter(description = "Order status") @RequestParam(name = "status", required = false) OrderStatus status,
            //@Parameter(description = "User ID") @RequestParam(name = "userId", required = false) Long userId,
            @Parameter(description = "Product ID") @RequestParam(name = "productId", required = false) Long productId,
            @Parameter(description = "Start creation time (ISO format)", example = "2024-01-01T00:00:00")
            @RequestParam(name = "createdAfter", required = false)
            @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime createdAfter,
            @Parameter(description = "End creation time (ISO format)", example = "2024-12-31T23:59:59")
            @RequestParam(name = "createdBefore", required = false)
            @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime createdBefore,
            @Parameter(description = "Keyword for fuzzy search") @RequestParam(name = "keyword", required = false) String keyword,
            @Parameter(description = "Page number (starting from 0)") @RequestParam(name = "page", defaultValue = "0") @Min(0) int page,
            @Parameter(description = "Page size") @RequestParam(name = "size", defaultValue = "10") @Min(1) int size
    ) {
        String userKey = (String) request.getAttribute("userKey");
        Long userId = (Long) request.getAttribute("userId");
        log.info("User [{}|{}] requests order list.", userId, userKey);

        PageResult<OrderSummaryDTO> result = orderQueryService.getOrderSummaries(status, userId, productId, createdAfter, createdBefore, keyword, page, size);
        if (result == null || result.getContent() == null || result.getContent().isEmpty()) {
            throw new NotFoundException("Order");
        }
        return result;
    }

    /**
     * 取消订单
     *
     * @param orderNo 订单号
     * @return 成功则返回空结果
     * @throws NotFoundException 订单不存在时抛出
     * @throws BusinessException 业务异常时抛出（如订单已取消）
     */
    @Operation(summary = "Cancel an order")
    @ApiResponses({
            @ApiResponse(
                    responseCode = "200",
                    description = "Order cancelled successfully",
                    content = @Content(schema = @Schema(implementation = Result.class))
            ),
            @ApiResponse(
                    responseCode = "400",
                    description = "Invalid order status or duplicate cancel attempt",
                    content = @Content(schema = @Schema(implementation = ApiError.class))
            ),
            @ApiResponse(
                    responseCode = "404",
                    description = "Order not found",
                    content = @Content(schema = @Schema(implementation = ApiError.class))
            ),
            @ApiResponse(
                    responseCode = "500",
                    description = "Internal server error",
                    content = @Content(schema = @Schema(implementation = ApiError.class))
            )
    })
    @PutMapping("/{orderNo}/cancel")
    public Result<Void> cancelOrder(
            HttpServletRequest request,
            @PathVariable String orderNo
    ) {
        String userKey = (String) request.getAttribute("userKey");
        Long userId = (Long) request.getAttribute("userId");
        log.info("User [{}|{}] requests to cancel order [{}].", userId, userKey, orderNo);

        // 无需 try-catch，异常交由 GlobalExceptionHandler 处理
        orderCommandService.cancel(orderNo, userKey);
        return Result.success();
    }

}
