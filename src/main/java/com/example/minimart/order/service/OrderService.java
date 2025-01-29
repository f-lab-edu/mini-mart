package com.example.minimart.order.service;

import com.example.minimart.order.controller.dto.request.CreateOrderRequest;
import com.example.minimart.order.infra.OrderItemJpaRepository;
import com.example.minimart.order.infra.OrderJpaRepository;
import com.example.minimart.order.infra.entity.OrderEntity;
import com.example.minimart.order.infra.entity.OrderItemEntity;
import com.example.minimart.order.infra.entity.OrderStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Transactional
@Service
public class OrderService {

    private final OrderJpaRepository orderJpaRepository;
    private final OrderItemJpaRepository orderItemJpaRepository;

    public OrderService(OrderJpaRepository orderJpaRepository, OrderItemJpaRepository orderItemJpaRepository) {
        this.orderJpaRepository = orderJpaRepository;
        this.orderItemJpaRepository = orderItemJpaRepository;
    }

    public OrderEntity getOrder(Long id) {
        return orderJpaRepository.findById(id)
            .orElseThrow(() -> new IllegalArgumentException("주문을 찾을 수 없습니다. 주문 ID: " + id));
    }

    public void createOrder(CreateOrderRequest request) {
        OrderEntity order = new OrderEntity(
            request.getCustomerId(),
            request.getTotalPrice(),
            OrderStatus.PENDING
        );

        OrderEntity savedOrder = orderJpaRepository.save(order);
        Long orderId = savedOrder.getId();

        List<OrderItemEntity> orderItems = request.getItems().stream()
            .map(item -> new OrderItemEntity(
                orderId,
                item.getProductId(),
                item.getProductName(),
                item.getOption(),
                item.getPrice(),
                item.getQuantity()
            ))
            .toList();

        orderItemJpaRepository.saveAll(orderItems);
    }

    public List<OrderEntity> listOrders() {
        return orderJpaRepository.findAll();
    }

}
