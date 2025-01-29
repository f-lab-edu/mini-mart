package com.example.minimart.order.application;

import com.example.minimart.api.order.dto.request.CreateOrderItemRequest;
import com.example.minimart.api.order.dto.request.CreateOrderRequest;
import com.example.minimart.order.domain.Order;
import com.example.minimart.order.domain.OrderRepository;
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
    private OrderRepository orderRepository;

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
        Order mockOrder = mock(Order.class);
        when(orderRepository.save(any(Order.class))).thenReturn(mockOrder);

        Order createdOrder = orderService.createOrder(validRequest);

        assertNotNull(createdOrder);
        verify(orderRepository).save(any(Order.class));
    }

    @Test
    @DisplayName("주문 항목이 없는 경우 예외 발생")
    void createOrderWithoutItemsThrowsException() {
        CreateOrderRequest invalidRequest = new CreateOrderRequest(1L, BigDecimal.valueOf(20), Collections.emptyList());

        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class,
                () -> orderService.createOrder(invalidRequest));

        assertEquals("주문 항목은 최소 1개 이상이어야 합니다.", exception.getMessage());
    }

    @Test
    @DisplayName("주문 조회 - 존재하는 주문")
    void getOrderSuccess() {
        Order mockOrder = mock(Order.class);
        when(orderRepository.findById(1L)).thenReturn(Optional.of(mockOrder));

        Order foundOrder = orderService.getOrder(1L);

        assertNotNull(foundOrder);
        verify(orderRepository).findById(1L);
    }

    @Test
    @DisplayName("주문 조회 - 존재하지 않는 주문 예외 발생")
    void getOrderNotFoundThrowsException() {
        when(orderRepository.findById(1L)).thenReturn(Optional.empty());

        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class,
                () -> orderService.getOrder(1L));

        assertEquals("주문을 찾을 수 없습니다. 주문 ID: 1", exception.getMessage());
    }

    @Test
    @DisplayName("저장된 주문이 없는 경우 빈 리스트 반환")
    void listOrdersReturnsEmptyList() {
        when(orderRepository.findAll()).thenReturn(Collections.emptyList());

        List<Order> orders = orderService.listOrders();

        assertTrue(orders.isEmpty());
        verify(orderRepository).findAll();
    }

    @Test
    @DisplayName("고객 ID가 0이거나 음수일 경우 예외 발생")
    void validateCustomerIdThrowsException() {
        CreateOrderRequest request = new CreateOrderRequest(0L, BigDecimal.TEN, List.of(
            new CreateOrderItemRequest(1L, "Product A", 1, BigDecimal.TEN, "Option")
        ));

        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class,
            () -> orderService.createOrder(request));

        assertEquals("고객 ID는 0보다 커야 합니다.", exception.getMessage());
    }

    @Test
    @DisplayName("총 가격이 음수인 경우 예외 발생")
    void validateTotalPriceThrowsException() {
        CreateOrderRequest request = new CreateOrderRequest(1L, BigDecimal.valueOf(-10), List.of(
            new CreateOrderItemRequest(1L, "Product A", 1, BigDecimal.TEN, "Option")
        ));

        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class,
            () -> orderService.createOrder(request));

        assertEquals("총 주문 금액은 0보다 크거나 같아야 합니다.", exception.getMessage());
    }

    @Test
    @DisplayName("상품 ID가 0이거나 음수일 경우 예외 발생")
    void validateProductIdThrowsException() {
        CreateOrderRequest request = new CreateOrderRequest(1L, BigDecimal.TEN, List.of(
            new CreateOrderItemRequest(0L, "Product A", 1, BigDecimal.TEN, "Option")
        ));

        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class,
            () -> orderService.createOrder(request));

        assertEquals("상품 ID는 0보다 커야 합니다.", exception.getMessage());
    }

    @Test
    @DisplayName("상품 이름이 비어 있는 경우 예외 발생")
    void validateProductNameThrowsException() {
        CreateOrderRequest request = new CreateOrderRequest(1L, BigDecimal.TEN, List.of(
            new CreateOrderItemRequest(1L, "", 1, BigDecimal.TEN, "Option")
        ));

        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class,
            () -> orderService.createOrder(request));

        assertEquals("상품 이름은 비어 있을 수 없습니다.", exception.getMessage());
    }

    @Test
    @DisplayName("단가가 0 이하인 경우 예외 발생")
    void validateUnitPriceThrowsException() {
        CreateOrderRequest request = new CreateOrderRequest(1L, BigDecimal.TEN, List.of(
            new CreateOrderItemRequest(1L, "Product A", 1, BigDecimal.ZERO, "Option")
        ));

        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class,
            () -> orderService.createOrder(request));

        assertEquals("단가는 0보다 커야 합니다.", exception.getMessage());
    }

    @Test
    @DisplayName("수량이 0 이하인 경우 예외 발생")
    void validateQuantityThrowsException() {
        CreateOrderRequest request = new CreateOrderRequest(1L, BigDecimal.TEN, List.of(
            new CreateOrderItemRequest(1L, "Product A", 0, BigDecimal.TEN, "Option")
        ));

        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class,
            () -> orderService.createOrder(request));

        assertEquals("수량은 0보다 커야 합니다.", exception.getMessage());
    }

}