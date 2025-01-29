package com.example.minimart.order.infra.entity;

import jakarta.persistence.*;
import org.springframework.util.Assert;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;

@Table(name = "orders")
@Entity
public class OrderEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private Long customerId;

    @Column(nullable = false, precision = 19, scale = 2)
    private BigDecimal totalPrice;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private OrderStatus status;

    @Column(nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @Column(nullable = false)
    private LocalDateTime updatedAt;

    @Column(nullable = false)
    private boolean deleted = false;

    protected OrderEntity() {
    }

    public OrderEntity(
        Long customerId,
        BigDecimal totalPrice,
        OrderStatus status
    ) {
        validateCustomerId(customerId);
        validateTotalPrice(totalPrice);
        validateOrderStatus(status);

        this.customerId = customerId;
        this.totalPrice = totalPrice;
        this.status = status;
    }

    @PrePersist
    protected void onCreate() {
        this.createdAt = LocalDateTime.now();
        this.updatedAt = LocalDateTime.now();
    }

    @PreUpdate
    protected void onUpdate() {
        this.updatedAt = LocalDateTime.now();
    }

    public void assignOrderIdToItems(Long orderId, List<OrderItemEntity> items) {
        Assert.notNull(orderId, "Order ID는 null일 수 없습니다.");
        Assert.notEmpty(items, "OrderItem 리스트는 비어 있을 수 없습니다.");

        items.forEach(item -> item.assignToOrder(orderId));
    }

    private void validateCustomerId(Long customerId) {
        Assert.notNull(customerId, "고객 ID는 null일 수 없습니다.");
        Assert.isTrue(customerId > 0, "고객 ID는 0보다 커야 합니다.");
    }

    private void validateTotalPrice(BigDecimal totalPrice) {
        Assert.notNull(totalPrice, "총 가격은 null일 수 없습니다.");
        Assert.isTrue(totalPrice.compareTo(BigDecimal.ZERO) >= 0, "총 가격은 0보다 작을 수 없습니다.");
    }

    private void validateOrderItems(List<OrderItemEntity> items) {
        Assert.notNull(items, "주문 항목은 null일 수 없습니다.");
        Assert.notEmpty(items, "주문 항목은 비어 있을 수 없습니다.");
    }

    private void validateOrderStatus(OrderStatus status) {
        Assert.notNull(status, "주문 상태는 null일 수 없습니다.");
    }

    public Long getId() {
        return id;
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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        OrderEntity order = (OrderEntity) o;
        return Objects.equals(id, order.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }

    @Override
    public String toString() {
        return "Order{" +
            "id=" + id +
            ", customerId=" + customerId +
            ", totalPrice=" + totalPrice +
            ", status=" + status +
            ", createdAt=" + createdAt +
            ", updatedAt=" + updatedAt +
            ", deleted=" + deleted +
            '}';
    }

}
