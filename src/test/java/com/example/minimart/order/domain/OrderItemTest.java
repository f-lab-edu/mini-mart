package com.example.minimart.order.domain;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.assertEquals;

class OrderItemTest {

    @Test
    @DisplayName("calculateTotalPrice() 메서드가 정확한 값을 반환한다")
    void calculateTotalPriceReturnsCorrectValue() {
        // given
        OrderItem orderItem = new OrderItem(1L, 1L, "Product A", "Option A", BigDecimal.valueOf(99.99), 5);

        // when
        BigDecimal totalPrice = orderItem.getTotalPrice();

        // then
        assertEquals(BigDecimal.valueOf(499.95), totalPrice);
    }

    @Test
    @DisplayName("unitPrice가 0이면 총 가격도 0이어야 한다")
    void calculateTotalPriceWithZeroUnitPrice() {
        // given
        OrderItem orderItem = new OrderItem(1L, 1L, "Product A", "Option A", BigDecimal.ZERO, 10);

        // when
        BigDecimal totalPrice = orderItem.getTotalPrice();

        // then
        assertEquals(BigDecimal.ZERO, totalPrice);
    }

    @Test
    @DisplayName("quantity가 1이면 totalPrice는 unitPrice와 동일해야 한다")
    void calculateTotalPriceWithQuantityOne() {
        // given
        BigDecimal unitPrice = BigDecimal.valueOf(19.99);
        OrderItem orderItem = new OrderItem(1L, 1L, "Product A", "Option A", unitPrice, 1);

        // when
        BigDecimal totalPrice = orderItem.getTotalPrice();

        // then
        assertEquals(unitPrice, totalPrice);
    }

    @Test
    @DisplayName("unitPrice가 소수점 값을 가질 경우 정확하게 계산되어야 한다")
    void calculateTotalPriceWithDecimalUnitPrice() {
        // given
        OrderItem orderItem = new OrderItem(1L, 1L, "Product A", "Option A", new BigDecimal("3.333"), 3);

        // when
        BigDecimal totalPrice = orderItem.getTotalPrice();

        // then
        assertEquals(BigDecimal.valueOf(9.999), totalPrice);
    }

    @Test
    @DisplayName("quantity가 큰 값일 경우에도 정상적으로 계산되어야 한다")
    void calculateTotalPriceWithLargeQuantity() {
        // given
        OrderItem orderItem = new OrderItem(1L, 1L, "Product A", "Option A", BigDecimal.valueOf(10.50), 100_000);

        // when
        BigDecimal totalPrice = orderItem.getTotalPrice();

        // then
        assertEquals(BigDecimal.valueOf(1_050_000.00), totalPrice);
    }

}