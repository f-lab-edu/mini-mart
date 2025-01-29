package com.example.minimart.order.infra.entity;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

class OrderItemTest {

    @Test
    @DisplayName("유효한 값으로 OrderItem 생성 성공")
    void createOrderItemWithValidData() {
        // given
        Long orderId = 1L;
        Long productId = 1L;
        String productName = "Product A";
        String productOption = "Option A";
        BigDecimal unitPrice = BigDecimal.valueOf(100.50);
        int quantity = 3;

        // when
        OrderItem orderItem = new OrderItem(orderId, productId, productName, productOption, unitPrice, quantity);

        // then
        assertEquals(BigDecimal.valueOf(301.50), orderItem.getTotalPrice());
        assertEquals("Product A", orderItem.getProductName());
    }

    @Test
    @DisplayName("unitPrice가 null일 경우 IllegalArgumentException 발생")
    void createOrderItemWithNullUnitPrice() {
        // given
        Long orderId = 1L;
        Long productId = 1L;
        String productName = "Product A";
        String productOption = "Option A";
        BigDecimal unitPrice = null;
        int quantity = 3;

        // when
        // then
        assertThrows(IllegalArgumentException.class, () -> new OrderItem(orderId, productId, productName, productOption, unitPrice, quantity));
    }

    @Test
    @DisplayName("quantity가 0이거나 음수일 경우 IllegalArgumentException 발생")
    void createOrderItemWithInvalidQuantity() {
        // given
        Long orderId = 1L;
        Long productId = 1L;
        String productName = "Product A";
        String productOption = "Option A";
        BigDecimal unitPrice = BigDecimal.valueOf(100.50);

        // when
        // then
        assertThrows(IllegalArgumentException.class, () -> new OrderItem(orderId, productId, productName, productOption, unitPrice, 0));
        assertThrows(IllegalArgumentException.class, () -> new OrderItem(orderId, productId, productName, productOption, unitPrice, -1));
    }

    @Test
    @DisplayName("calculateTotalPrice 메서드가 정확한 값을 반환")
    void calculateTotalPriceReturnsCorrectValue() {
        // given
        OrderItem orderItem = new OrderItem(1L, 1L, "Product A", "Option A", BigDecimal.valueOf(99.99), 5);

        // when
        BigDecimal totalPrice = orderItem.calculateTotalPrice();

        // then
        assertEquals(BigDecimal.valueOf(499.95), totalPrice);
    }

    @Test
    @DisplayName("assignToOrder 메서드를 사용하여 유효한 orderId를 설정")
    void assignToOrderWithValidOrderId() {
        // given
        OrderItem orderItem = new OrderItem(1L, 1L, "Product A", "Option A", BigDecimal.TEN, 2);
        Long orderId = 123L;

        // when
        orderItem.assignToOrder(orderId);

        // then
        assertEquals(orderId, orderItem.getOrderId());
    }

    @Test
    @DisplayName("assignToOrder 메서드 호출 시 orderId가 null이면 IllegalArgumentException 발생")
    void assignToOrderWithNullOrderIdThrowsException() {
        // given
        OrderItem orderItem = new OrderItem(1L, 1L, "Product A", "Option A", BigDecimal.TEN, 2);

        // when
        // then
        assertThrows(IllegalArgumentException.class, () -> orderItem.assignToOrder(null));
    }

    @Test
    @DisplayName("assignToOrder 메서드로 이미 설정된 orderId를 재설정")
    void reassignToOrderWithNewOrderId() {
        // given
        OrderItem orderItem = new OrderItem(1L, 1L, "Product A", "Option A", BigDecimal.TEN, 2);
        Long initialOrderId = 123L;
        Long newOrderId = 456L;

        // when
        orderItem.assignToOrder(initialOrderId);
        orderItem.assignToOrder(newOrderId);

        // then
        assertEquals(newOrderId, orderItem.getOrderId());
    }

}