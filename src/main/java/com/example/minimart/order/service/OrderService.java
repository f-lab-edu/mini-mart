package com.example.minimart.order.service;

import com.example.minimart.order.controller.dto.request.CreateOrderItemRequest;
import com.example.minimart.order.controller.dto.request.CreateOrderRequest;
import com.example.minimart.order.repository.OrderItemJpaRepository;
import com.example.minimart.order.repository.OrderJpaRepository;
import com.example.minimart.order.repository.entity.OrderEntity;
import com.example.minimart.order.repository.entity.OrderItemEntity;
import com.example.minimart.order.repository.entity.OrderStatus;
import com.example.minimart.order.service.domain.Order;
import com.example.minimart.order.service.domain.OrderItem;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class OrderService {

    private final OrderJpaRepository orderJpaRepository;
    private final OrderItemJpaRepository orderItemJpaRepository;

    public OrderService(OrderJpaRepository orderJpaRepository, OrderItemJpaRepository orderItemJpaRepository) {
        this.orderJpaRepository = orderJpaRepository;
        this.orderItemJpaRepository = orderItemJpaRepository;
    }

    public Order getOrder(Long orderId) {
        OrderEntity orderEntity = orderJpaRepository.findById(orderId)
            .orElseThrow(() -> new IllegalArgumentException("주문을 찾을 수 없습니다. 주문 ID: " + orderId));

        List<OrderItemEntity> orderItemEntities = orderItemJpaRepository.findByOrderId(orderId);

        return toOrderDomain(orderEntity, orderItemEntities);
    }

    @Transactional
    public Order createOrder(CreateOrderRequest request) {
        try {
            validateCustomerId(request.getCustomerId());
            validateTotalPrice(request.getTotalPrice());
            validateOrderItems(request.getItems());

            // Order DTO -> Entity
            OrderEntity orderEntity = new OrderEntity(
                request.getCustomerId(),
                request.getTotalPrice(),
                OrderStatus.PENDING
            );
            // Order Save
            OrderEntity savedOrderEntity = orderJpaRepository.save(orderEntity);
            Long orderId = savedOrderEntity.getId();

            // OrderItem DTO -> Entity
            List<OrderItemEntity> orderItemEntities = request.getItems().stream()
                .map(item -> {
                    validateProductId(item.getProductId());
                    validateProductName(item.getProductName());
                    validateUnitPrice(item.getPrice());
                    validateQuantity(item.getQuantity());

                    return new OrderItemEntity(
                        orderId,
                        item.getProductId(),
                        item.getProductName(),
                        item.getOption(),
                        item.getPrice(),
                        item.getQuantity(),
                        item.getPrice().multiply(BigDecimal.valueOf(item.getQuantity()))
                    );
                })
                .collect(Collectors.toList());
            // OrderItem Save
            orderItemJpaRepository.saveAll(orderItemEntities);

            // Entity -> Domain
            return toOrderDomain(savedOrderEntity, orderItemEntities);
        } catch (IllegalArgumentException e) {
            throw e;
        } catch (Exception e) {
            throw new RuntimeException("주문 저장 중 오류 발생 - 고객 ID: " + request.getCustomerId(), e);
        }
    }

    public List<Order> listOrders() {
        List<OrderEntity> orderEntities = orderJpaRepository.findAll();

        return orderEntities.stream()
            .map(order -> {
                List<OrderItemEntity> orderItems = orderItemJpaRepository.findByOrderId(order.getId());

                return toOrderDomain(order, orderItems);
            })
            .collect(Collectors.toList());
    }

    private Order toOrderDomain(OrderEntity orderEntity, List<OrderItemEntity> orderItemEntities) {
        List<OrderItem> orderItems = orderItemEntities.stream()
            .map(this::toOrderItemDomain)
            .collect(Collectors.toList());

        return new Order(
            orderEntity.getId(),
            orderEntity.getCustomerId(),
            orderEntity.getTotalPrice(),
            orderEntity.getStatus(),
            orderEntity.getCreatedAt(),
            orderItems
        );
    }

    private OrderItem toOrderItemDomain(OrderItemEntity itemEntity) {
        return new OrderItem(
            itemEntity.getId(),
            itemEntity.getProductId(),
            itemEntity.getProductName(),
            itemEntity.getProductOption(),
            itemEntity.getUnitPrice(),
            itemEntity.getQuantity()
        );
    }

    private void validateCustomerId(Long customerId) {
        if (customerId == null || customerId <= 0) {
            throw new IllegalArgumentException("고객 ID는 0보다 커야 합니다.");
        }
    }

    private void validateTotalPrice(BigDecimal totalPrice) {
        if (totalPrice == null || totalPrice.compareTo(BigDecimal.ZERO) < 0) {
            throw new IllegalArgumentException("총 주문 금액은 0보다 크거나 같아야 합니다.");
        }
    }

    private void validateOrderItems(List<CreateOrderItemRequest> items) {
        if (items == null || items.isEmpty()) {
            throw new IllegalArgumentException("주문 항목은 최소 1개 이상이어야 합니다.");
        }
    }

    private void validateProductId(Long productId) {
        if (productId == null || productId <= 0) {
            throw new IllegalArgumentException("상품 ID는 0보다 커야 합니다.");
        }
    }

    private void validateProductName(String productName) {
        if (productName == null || productName.trim().isEmpty()) {
            throw new IllegalArgumentException("상품 이름은 비어 있을 수 없습니다.");
        }
    }

    private void validateUnitPrice(BigDecimal unitPrice) {
        if (unitPrice == null || unitPrice.compareTo(BigDecimal.ZERO) <= 0) {
            throw new IllegalArgumentException("단가는 0보다 커야 합니다.");
        }
    }

    private void validateQuantity(int quantity) {
        if (quantity <= 0) {
            throw new IllegalArgumentException("수량은 0보다 커야 합니다.");
        }
    }

}
