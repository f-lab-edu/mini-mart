package com.example.minimart.order.service.domain;

import com.example.minimart.order.repository.entity.OrderStatus;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;

public class Order {

    private Long orderId;
    private Long customerId;
    private BigDecimal totalPrice;
    private OrderStatus status;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    private List<OrderItem> orderItems;

    public Order(
        Long orderId,
        Long customerId,
        BigDecimal totalPrice,
        OrderStatus status,
        LocalDateTime createdAt,
        List<OrderItem> orderItems
    ) {
        this.orderId = orderId;
        this.customerId = customerId;
        this.totalPrice = totalPrice;
        this.status = status;
        this.createdAt = createdAt;
        this.orderItems = orderItems;
        this.updatedAt = LocalDateTime.now();
    }

    public static Order create(Long customerId, BigDecimal totalPrice, List<OrderItem> orderItems) {
        return new Order(null, customerId, totalPrice, OrderStatus.PENDING, LocalDateTime.now(), orderItems);
    }

    public void changeStatus(OrderStatus newStatus) {
        this.status = newStatus;
        this.updatedAt = LocalDateTime.now();
    }

    public void updateTotalPrice(BigDecimal newTotalPrice) {
        this.totalPrice = newTotalPrice;
        this.updatedAt = LocalDateTime.now();
    }

    public Long getOrderId() {
        return orderId;
    }

    public Long getCustomerId() {
        return customerId;
    }

    public BigDecimal getTotalPrice() {
        return totalPrice;
    }

    public OrderStatus getStatus() {
        return status;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public LocalDateTime getUpdatedAt() {
        return updatedAt;
    }

    public List<OrderItem> getOrderItems() {
        return orderItems;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Order order = (Order) o;

        return Objects.equals(orderId, order.orderId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(orderId);
    }

    @Override
    public String toString() {
        return "Order{" +
            "id=" + orderId +
            ", customerId=" + customerId +
            ", totalPrice=" + totalPrice +
            ", status=" + status +
            ", createdAt=" + createdAt +
            ", updatedAt=" + updatedAt +
            ", orderItems=" + orderItems +
            '}';
    }

}
