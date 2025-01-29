package com.example.minimart.order.domain;

import com.example.minimart.order.infra.entity.OrderStatus;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.*;

class OrderTest {

    @Test
    @DisplayName("create() 메서드는 PENDING 상태의 주문을 생성해야 한다")
    void createOrder() {
        // given
        Long customerId = 1L;
        BigDecimal totalPrice = BigDecimal.valueOf(100.00);

        // when
        Order order = Order.create(customerId, totalPrice);

        // then
        assertNull(order.getId());
        assertEquals(customerId, order.getCustomerId());
        assertEquals(totalPrice, order.getTotalPrice());
        assertEquals(OrderStatus.PENDING, order.getStatus());
        assertNotNull(order.getCreatedAt());
        assertNotNull(order.getUpdatedAt());
    }

    @Test
    @DisplayName("changeStatus() 메서드는 주문 상태를 변경해야 한다")
    void changeOrderStatus() {
        // given
        Order order = Order.create(1L, BigDecimal.valueOf(200.00));

        // when
        order.changeStatus(OrderStatus.CONFIRMED);

        // then
        assertEquals(OrderStatus.CONFIRMED, order.getStatus());
    }

    @Test
    @DisplayName("updateTotalPrice() 메서드는 총 가격을 변경해야 한다")
    void updateOrderTotalPrice() {
        // given
        Order order = Order.create(1L, BigDecimal.valueOf(150.00));

        // when
        BigDecimal newPrice = BigDecimal.valueOf(180.00);
        order.updateTotalPrice(newPrice);

        // then
        assertEquals(newPrice, order.getTotalPrice());
    }

}