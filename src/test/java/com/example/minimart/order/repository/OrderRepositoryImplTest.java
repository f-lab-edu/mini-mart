package com.example.minimart.order.repository;

import com.example.minimart.order.service.domain.Order;
import com.example.minimart.order.service.domain.OrderItem;
import com.example.minimart.order.repository.entity.OrderEntity;
import com.example.minimart.order.repository.entity.OrderItemEntity;
import com.example.minimart.order.repository.entity.OrderStatus;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class OrderRepositoryImplTest {

    @Mock
    private OrderJpaRepository orderJpaRepository;

    @Mock
    private OrderItemJpaRepository orderItemJpaRepository;

    @InjectMocks
    private OrderRepositoryImpl orderRepository;

    @Test
    @DisplayName("정상적인 주문 저장")
    void saveOrderSuccess() {
        // given
        List<OrderItem> orderItems = List.of(
            new OrderItem(1L, 100L, "상품 A", "옵션 A", BigDecimal.TEN.valueOf(10.00), 2),
            new OrderItem(2L, 200L, "상품 B", "옵션 B", BigDecimal.valueOf(15.00), 3)
        );

        Order order = new Order(1L, 1L, BigDecimal.valueOf(55.00), OrderStatus.PENDING, null, orderItems);

        OrderEntity savedOrderEntity = new OrderEntity(1L, order.getCustomerId(), order.getTotalPrice(), order.getStatus());
        List<OrderItemEntity> savedOrderItemEntities = List.of(
            new OrderItemEntity(1L, 100L, "상품 A", "옵션 A", BigDecimal.valueOf(10.00), 2, BigDecimal.valueOf(20.00)),
            new OrderItemEntity(2L, 200L, "상품 B", "옵션 B", BigDecimal.valueOf(15.00), 3, BigDecimal.valueOf(45.00))
        );

        when(orderJpaRepository.save(any(OrderEntity.class))).thenReturn(savedOrderEntity);
        when(orderItemJpaRepository.saveAll(any())).thenReturn(savedOrderItemEntities);

        // when
        Order savedOrder = assertDoesNotThrow(() -> orderRepository.save(order));

        // then
        assertNotNull(savedOrder);
        assertEquals(order.getCustomerId(), savedOrder.getCustomerId());
        assertEquals(order.getTotalPrice(), savedOrder.getTotalPrice());
        assertEquals(orderItems.size(), savedOrder.getOrderItems().size());
    }

    @Test
    @DisplayName("주문 조회 성공")
    void findOrderByIdSuccess() {
        // given
        Long orderId = 1L;
        OrderEntity orderEntity = new OrderEntity(orderId, 1L, BigDecimal.valueOf(55.00), OrderStatus.PENDING);
        List<OrderItemEntity> orderItemEntities = List.of(
            new OrderItemEntity(1L, 100L, "상품 A", "옵션 A", BigDecimal.valueOf(10.00), 2, BigDecimal.valueOf(20.00)),
            new OrderItemEntity(2L, 200L, "상품 B", "옵션 B", BigDecimal.valueOf(15.00), 3, BigDecimal.valueOf(45.00))
        );

        when(orderJpaRepository.findById(orderId)).thenReturn(Optional.of(orderEntity));
        when(orderItemJpaRepository.findByOrderId(orderId)).thenReturn(orderItemEntities);

        // when
        Optional<Order> result = orderRepository.findById(orderId);

        // then
        assertTrue(result.isPresent());
        assertEquals(orderEntity.getCustomerId(), result.get().getCustomerId());
        assertEquals(orderEntity.getTotalPrice(), result.get().getTotalPrice());
        assertEquals(orderItemEntities.size(), result.get().getOrderItems().size());
    }

    @Test
    @DisplayName("주문 조회 실패 - 존재하지 않는 주문")
    void findOrderByIdFail() {
        // given
        Long orderId = -1L;

        when(orderJpaRepository.findById(orderId)).thenReturn(Optional.empty());

        // when
        Optional<Order> result = orderRepository.findById(orderId);

        // then
        assertTrue(result.isEmpty());
    }

}