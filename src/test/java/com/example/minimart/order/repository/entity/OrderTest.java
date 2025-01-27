package com.example.minimart.order.repository.entity;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

class OrderTest {

    @Test
    @DisplayName("null 리스트를 추가하려고 할 때 IllegalArgumentException 예외 발생")
    void addItemsWithNullList() {
        // given
        // when
        // then
        assertThrows(IllegalArgumentException.class, () -> new Order(1L, BigDecimal.TEN, OrderStatus.PENDING, null));
    }

    @Test
    @DisplayName("주문 항목이 비어 있는 경우 IllegalArgumentException 예외 발생")
    void orderWithEmptyItemsThrowsException() {
        // given
        // when
        // then
        assertThrows(IllegalArgumentException.class, () -> new Order(1L, BigDecimal.TEN, OrderStatus.PENDING, List.of()));
    }

    @Test
    @DisplayName("OrderItem에 orderId를 수동으로 설정하여 관계를 확인")
    void setOrderIdManuallyInOrderItem() {
        // given
        OrderItem item1 = new OrderItem(1L, "Product A", "Option A", BigDecimal.TEN, 2);
        List<OrderItem> items = List.of(item1);
        Long orderId = 123L;

        // when
        Order order = new Order(1L, BigDecimal.TEN, OrderStatus.PENDING, items);
        order.assignOrderIdToItems(orderId, items);

        // then
        assertEquals(orderId, item1.getOrderId());
    }

    @Test
    @DisplayName("총 가격이 음수인 경우 IllegalArgumentException 예외 발생")
    void orderWithNegativeTotalPriceThrowsException() {
        // given
        List<OrderItem> items = List.of(new OrderItem(1L, "Product A", "Option A", BigDecimal.TEN, 1));

        // when
        // then
        assertThrows(IllegalArgumentException.class, () -> new Order(1L, BigDecimal.valueOf(-10), OrderStatus.PENDING, items));
    }

    @Test
    @DisplayName("주문 상태가 null인 경우 IllegalArgumentException 예외 발생")
    void orderWithNullStatusThrowsException() {
        // given
        List<OrderItem> items = List.of(new OrderItem(1L, "Product A", "Option A", BigDecimal.TEN, 1));

        // when
        // then
        assertThrows(IllegalArgumentException.class, () -> new Order(1L, BigDecimal.TEN, null, items));
    }

}