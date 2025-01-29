package com.example.minimart.order.application;

import com.example.minimart.api.order.dto.request.CreateOrderItemRequest;
import com.example.minimart.api.order.dto.request.CreateOrderRequest;
import com.example.minimart.order.domain.Order;
import com.example.minimart.order.domain.OrderItem;
import com.example.minimart.order.domain.OrderRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class OrderService {

    private OrderRepository orderRepository;

    public OrderService(OrderRepository orderRepository) {
        this.orderRepository = orderRepository;
    }

    public Order getOrder(Long id) {
        return orderRepository.findById(id)
            .orElseThrow(() -> new IllegalArgumentException("주문을 찾을 수 없습니다. 주문 ID: " + id));
    }

    @Transactional
    public Order createOrder(CreateOrderRequest request) {
        try {
            validateCustomerId(request.getCustomerId());
            validateTotalPrice(request.getTotalPrice());
            validateOrderItems(request.getItems());

            // Request DTO -> OrderItem Domain
            List<OrderItem> orderItems = request.getItems().stream()
                .map(item -> {
                    validateProductId(item.getProductId());
                    validateProductName(item.getProductName());
                    validateUnitPrice(item.getPrice());
                    validateQuantity(item.getQuantity());

                    return OrderItem.create(
                        item.getProductId(),
                        item.getProductName(),
                        item.getOption(),
                        item.getPrice(),
                        item.getQuantity()
                    );
                })
                .collect(Collectors.toList());

            // Request DTO -> Order Domain
            Order order = Order.create(
                request.getCustomerId(),
                request.getTotalPrice(),
                orderItems
            );

            return orderRepository.save(order);
        } catch (IllegalArgumentException e) {
            throw e;
        } catch (Exception e) {
            throw new RuntimeException("주문 저장 중 오류 발생 - 고객 ID: " + request.getCustomerId(), e);
        }
    }

    public List<Order> listOrders() {
        return orderRepository.findAll();
    }

    private void validateCustomerId(Long customerId) {
        if (customerId == null || customerId <= 0) {
            throw new IllegalArgumentException("고객 ID는 0보다 커야 합니다.");
        }
    }

    private void validateTotalPrice(BigDecimal totalPrice) {
        if (totalPrice == null || totalPrice.compareTo(BigDecimal.ZERO) < 0) {
            throw new IllegalArgumentException("총 주문 금액은 0보다 크거나 같아야 합니다.");
        }
    }

    private void validateOrderItems(List<CreateOrderItemRequest> items) {
        if (items == null || items.isEmpty()) {
            throw new IllegalArgumentException("주문 항목은 최소 1개 이상이어야 합니다.");
        }
    }

    private void validateProductId(Long productId) {
        if (productId == null || productId <= 0) {
            throw new IllegalArgumentException("상품 ID는 0보다 커야 합니다.");
        }
    }

    private void validateProductName(String productName) {
        if (productName == null || productName.trim().isEmpty()) {
            throw new IllegalArgumentException("상품 이름은 비어 있을 수 없습니다.");
        }
    }

    private void validateUnitPrice(BigDecimal unitPrice) {
        if (unitPrice == null || unitPrice.compareTo(BigDecimal.ZERO) <= 0) {
            throw new IllegalArgumentException("단가는 0보다 커야 합니다.");
        }
    }

    private void validateQuantity(int quantity) {
        if (quantity <= 0) {
            throw new IllegalArgumentException("수량은 0보다 커야 합니다.");
        }
    }

}
