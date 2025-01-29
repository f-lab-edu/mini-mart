package com.example.minimart.api.order;

import com.example.minimart.api.order.dto.request.CreateOrderRequest;
import com.example.minimart.order.application.OrderService;
import com.example.minimart.order.domain.Order;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RequestMapping("/api/orders")
@RestController
@Tag(name = "Order API", description = "주문 관련 API")
public class OrderController {

    private final OrderService orderService;

    public OrderController(OrderService orderService) {
        this.orderService = orderService;
    }

    @GetMapping("/{id}")
    @Operation(summary = "주문 조회", description = "주문 ID를 사용하여 특정 주문을 조회합니다.")
    public Order getOrder(
        @PathVariable
        @Parameter(description = "주문 ID", example = "1", required = true)
        Long id
    ) {
        return orderService.getOrder(id);
    }

    @PostMapping
    @Operation(summary = "주문 생성", description = "새로운 주문을 생성합니다.")
    public Order createOrder(@RequestBody CreateOrderRequest request) {
        return orderService.createOrder(request);
    }

    @GetMapping
    @Operation(summary = "주문 목록 조회", description = "전체 주문 목록을 조회합니다.")
    public List<Order> listOrders() {
        return orderService.listOrders();
    }

}
