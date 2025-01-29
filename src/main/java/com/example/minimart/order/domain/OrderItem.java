package com.example.minimart.order.domain;

import java.math.BigDecimal;
import java.util.Objects;

public class OrderItem {

    private Long id;
    private Long productId;
    private String productName;
    private String productOption;
    private BigDecimal unitPrice;
    private int quantity;
    private BigDecimal totalPrice;

    public OrderItem(
        Long id,
        Long productId,
        String productName,
        String productOption,
        BigDecimal unitPrice,
        int quantity
    ) {
        this.id = id;
        this.productId = productId;
        this.productName = productName;
        this.productOption = productOption;
        this.unitPrice = unitPrice;
        this.quantity = quantity;
        this.totalPrice = calculateTotalPrice();
    }

    public static OrderItem create(Long productId, String productName, String productOption, BigDecimal unitPrice, int quantity) {
        return new OrderItem(null, productId, productName, productOption, unitPrice, quantity);
    }

    private BigDecimal calculateTotalPrice() {
        return this.unitPrice.multiply(BigDecimal.valueOf(this.quantity));
    }

    public Long getId() {
        return id;
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
            ", productId=" + productId +
            ", productName='" + productName + '\'' +
            ", productOption='" + productOption + '\'' +
            ", unitPrice=" + unitPrice +
            ", quantity=" + quantity +
            ", totalPrice=" + totalPrice +
            '}';
    }

}
