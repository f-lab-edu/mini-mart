package com.example.minimart.order.repository.entity;

import jakarta.persistence.*;
import org.springframework.util.Assert;

import java.math.BigDecimal;
import java.util.Objects;

@Entity
@Table(name = "order_items")
public class OrderItem {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private Long orderId;

    @Column(nullable = false)
    private Long productId;

    @Column(nullable = false, length = 255)
    private String productName;

    @Column(length = 255)
    private String productOption;

    @Column(nullable = false, precision = 19, scale = 2)
    private BigDecimal unitPrice;

    @Column(nullable = false)
    private int quantity;

    @Column(nullable = false, precision = 19, scale = 2)
    private BigDecimal totalPrice;

    @Column(nullable = false)
    private boolean deleted = false;

    protected OrderItem() {
    }

    public OrderItem(
        Long orderId,
        Long productId,
        String productName,
        String productOption,
        BigDecimal unitPrice,
        int quantity
    ) {
        validateOrderId(productId);
        validateProductId(productId);
        validateProductName(productName);
        validateUnitPrice(unitPrice);
        validateQuantity(quantity);

        this.orderId = orderId;
        this.productId = productId;
        this.productName = productName;
        this.productOption = productOption;
        this.unitPrice = unitPrice;
        this.quantity = quantity;
        this.totalPrice = calculateTotalPrice();
    }

    public BigDecimal calculateTotalPrice() {
        return this.unitPrice.multiply(BigDecimal.valueOf(this.quantity));
    }

    public void assignToOrder(Long orderId) {
        Assert.notNull(orderId, "Order ID는 null일 수 없습니다.");

        this.orderId = orderId;
    }

    private void validateOrderId(Long orderId) {
        Assert.notNull(orderId, "주문 ID는 null일 수 없습니다.");
        Assert.isTrue(orderId > 0, "주문 ID는 0보다 커야 합니다.");
    }

    private void validateProductId(Long productId) {
        Assert.notNull(productId, "상품 ID는 null일 수 없습니다.");
        Assert.isTrue(productId > 0, "상품 ID는 0보다 커야 합니다.");
    }

    private void validateProductName(String productName) {
        Assert.hasText(productName, "상품 이름은 null이거나 비어 있을 수 없습니다.");
    }

    private void validateUnitPrice(BigDecimal unitPrice) {
        Assert.notNull(unitPrice, "단가는 null일 수 없습니다.");
        Assert.isTrue(unitPrice.compareTo(BigDecimal.ZERO) > 0, "단가는 0보다 커야 합니다.");
    }

    private void validateQuantity(int quantity) {
        Assert.isTrue(quantity > 0, "수량은 0보다 커야 합니다.");
    }

    public Long getId() {
        return id;
    }

    public Long getOrderId() {
        return orderId;
    }

    public Long getProductId() {
        return productId;
    }

    public String getProductName() {
        return productName;
    }

    public String getProductOption() {
        return productOption;
    }

    public BigDecimal getUnitPrice() {
        return unitPrice;
    }

    public int getQuantity() {
        return quantity;
    }

    public BigDecimal getTotalPrice() {
        return totalPrice;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        OrderItem orderItem = (OrderItem) o;
        return Objects.equals(id, orderItem.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }

    @Override
    public String toString() {
        return "OrderItem{" +
            "id=" + id +
            ", orderId=" + orderId +
            ", productId=" + productId +
            ", productName='" + productName + '\'' +
            ", productOption='" + productOption + '\'' +
            ", unitPrice=" + unitPrice +
            ", quantity=" + quantity +
            ", totalPrice=" + totalPrice +
            ", deleted=" + deleted +
            '}';
    }

}
