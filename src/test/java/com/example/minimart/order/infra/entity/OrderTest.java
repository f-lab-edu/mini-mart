package com.example.minimart.order.infra.entity;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.assertThrows;

class OrderTest {

    @Test
    @DisplayName("총 가격이 음수인 경우 IllegalArgumentException 예외 발생")
    void orderWithNegativeTotalPriceThrowsException() {
        // given

        // when
        // then
        assertThrows(IllegalArgumentException.class, () -> new OrderEntity(1L, BigDecimal.valueOf(-10), OrderStatus.PENDING));
    }

    @Test
    @DisplayName("주문 상태가 null인 경우 IllegalArgumentException 예외 발생")
    void orderWithNullStatusThrowsException() {
        // given

        // when
        // then
        assertThrows(IllegalArgumentException.class, () -> new OrderEntity(1L, BigDecimal.TEN, null));
    }

}