package com.example.minimart.order.service;

import com.example.minimart.order.controller.dto.request.CreateOrderItemRequest;
import com.example.minimart.order.controller.dto.request.CreateOrderRequest;
import com.example.minimart.order.repository.OrderItemJpaRepository;
import com.example.minimart.order.repository.OrderJpaRepository;
import com.example.minimart.order.repository.entity.OrderEntity;
import com.example.minimart.order.repository.entity.OrderItemEntity;
import com.example.minimart.order.repository.entity.OrderStatus;
import com.example.minimart.order.service.domain.Order;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class OrderServiceTest {

    @Mock
    private OrderJpaRepository orderJpaRepository;

    @Mock
    private OrderItemJpaRepository orderItemJpaRepository;

    @InjectMocks
    private OrderService orderService;

    private static CreateOrderRequest validRequest;

    @BeforeAll
    static void beforeAll() {
        CreateOrderItemRequest item = new CreateOrderItemRequest(1L, "Product A", 2, BigDecimal.TEN, "Option A");
        validRequest = new CreateOrderRequest(1L, BigDecimal.valueOf(20), List.of(item));
    }

    @Test
    @DisplayName("정상적인 주문 생성")
    void createOrderSuccess() {
        // given
        OrderEntity savedOrderEntity = new OrderEntity(1L, 1L, BigDecimal.valueOf(20), OrderStatus.PENDING);
        List<OrderItemEntity> orderItemEntities = List.of(
            new OrderItemEntity(1L, 1L, "Product A", "Option A", BigDecimal.TEN, 2, BigDecimal.valueOf(20))
        );
        when(orderJpaRepository.save(any())).thenReturn(savedOrderEntity);
        when(orderItemJpaRepository.saveAll(any())).thenReturn(orderItemEntities);

        // when
        Order createdOrder = orderService.createOrder(validRequest);

        // then
        assertNotNull(createdOrder);
        assertEquals(OrderStatus.PENDING, createdOrder.getStatus());
        verify(orderJpaRepository).save(any(OrderEntity.class));
        verify(orderItemJpaRepository).saveAll(any());
    }

    @Test
    @DisplayName("존재하는 주문 조회 성공")
    void getOrderSuccess() {
        // given
        OrderEntity orderEntity = new OrderEntity(1L, 1L, BigDecimal.valueOf(20), OrderStatus.PENDING);
        when(orderJpaRepository.findById(1L)).thenReturn(Optional.of(orderEntity));
        when(orderItemJpaRepository.findByOrderId(1L)).thenReturn(Collections.emptyList());

        // when
        Order foundOrder = orderService.getOrder(1L);

        // then
        assertNotNull(foundOrder);
        assertEquals(1L, foundOrder.getOrderId());
        verify(orderJpaRepository).findById(1L);
    }

    @Test
    @DisplayName("존재하지 않는 주문 조회시 예외 발생")
    void getOrderNotFoundThrowsException() {
        // given
        when(orderJpaRepository.findById(-1L)).thenReturn(Optional.empty());

        // when
        // then
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> orderService.getOrder(-1L));
        assertEquals("주문을 찾을 수 없습니다. 주문 ID: -1", exception.getMessage());
    }

    @Test
    @DisplayName("고객 ID가 0 이하일 경우 예외 발생")
    void createOrderWithInvalidCustomerIdThrowsException() {
        // given
        CreateOrderRequest invalidRequest = new CreateOrderRequest(0L, BigDecimal.TEN, validRequest.getItems());

        // when
        // then
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> orderService.createOrder(invalidRequest));
        assertEquals("고객 ID는 0보다 커야 합니다.", exception.getMessage());
    }

    @Test
    @DisplayName("주문 항목이 없을 경우 예외 발생")
    void createOrderWithoutItemsThrowsException() {
        // given
        CreateOrderRequest invalidRequest = new CreateOrderRequest(1L, BigDecimal.TEN, Collections.emptyList());

        // when
        // then
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> orderService.createOrder(invalidRequest));
        assertEquals("주문 항목은 최소 1개 이상이어야 합니다.", exception.getMessage());
    }

    @Test
    @DisplayName("총 주문 금액이 음수일 경우 예외 발생")
    void createOrderWithNegativeTotalPriceThrowsException() {
        // given
        CreateOrderRequest invalidRequest = new CreateOrderRequest(1L, BigDecimal.valueOf(-10), validRequest.getItems());

        // when
        // then
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> orderService.createOrder(invalidRequest));
        assertEquals("총 주문 금액은 0보다 크거나 같아야 합니다.", exception.getMessage());
    }

}