package com.example.minimart.order.controller;

import com.example.minimart.order.controller.dto.request.CreateOrderRequest;
import com.example.minimart.order.repository.entity.Order;
import com.example.minimart.order.service.OrderService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RequestMapping("/api/orders")
@RestController
public class OrderController {

    private final OrderService orderService;

    public OrderController(OrderService orderService) {
        this.orderService = orderService;
    }

    @GetMapping("/{id}")
    public Order getOrder(@PathVariable Long id) {
        return orderService.getOrder(id);
    }

    @PostMapping
    public void createOrder(@RequestBody CreateOrderRequest request) {
        orderService.createOrder(request);
    }

    @GetMapping
    public List<Order> listOrders() {
        return orderService.listOrders();
    }

}
