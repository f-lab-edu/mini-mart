package com.example.minimart.order.infra;

import com.example.minimart.order.domain.Order;
import com.example.minimart.order.domain.OrderItem;
import com.example.minimart.order.domain.OrderRepository;
import com.example.minimart.order.infra.entity.OrderEntity;
import com.example.minimart.order.infra.entity.OrderItemEntity;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Repository
public class OrderRepositoryImpl implements OrderRepository {

    private final OrderJpaRepository orderJpaRepository;
    private final OrderItemJpaRepository orderItemJpaRepository;

    public OrderRepositoryImpl(OrderJpaRepository orderJpaRepository, OrderItemJpaRepository orderItemJpaRepository) {
        this.orderJpaRepository = orderJpaRepository;
        this.orderItemJpaRepository = orderItemJpaRepository;
    }

    @Transactional
    @Override
    public Order save(Order order) {
        try {
            // Order Domain -> Entity
            OrderEntity orderEntity = new OrderEntity(
                order.getCustomerId(),
                order.getTotalPrice(),
                order.getStatus()
            );

            // Save Order Entity
            OrderEntity savedOrderEntity = orderJpaRepository.save(orderEntity);
            Long orderId = savedOrderEntity.getId();

            // OrderItem Domain -> Entity
            List<OrderItemEntity> orderItemEntities = order.getOrderItems().stream()
                .map(item -> new OrderItemEntity(
                    orderId,
                    item.getProductId(),
                    item.getProductName(),
                    item.getProductOption(),
                    item.getUnitPrice(),
                    item.getQuantity(),
                    item.getTotalPrice()
                ))
                .collect(Collectors.toList());

            // Save OrderItem Entities
            final List<OrderItemEntity> savedOrderItemEntities = orderItemJpaRepository.saveAll(orderItemEntities);

            // Order, OrderItem Entity -> Domain
            return toOrderDomain(savedOrderEntity, savedOrderItemEntities);
        } catch (Exception e) {
            throw new RuntimeException("주문 저장 중 오류 발생", e);
        }
    }

    @Override
    public Optional<Order> findById(Long id) {
        Optional<OrderEntity> orderEntity = orderJpaRepository.findById(id);
        if (orderEntity.isEmpty()) {
            return Optional.empty();
        }

        List<OrderItemEntity> orderItems = orderItemJpaRepository.findByOrderId(id);

        return Optional.of(toOrderDomain(orderEntity.get(), orderItems));
    }

    @Override
    public List<Order> findAll() {
        List<OrderEntity> orderEntities = orderJpaRepository.findAll();

        return orderEntities.stream()
            .map(order -> {
                List<OrderItemEntity> orderItems = orderItemJpaRepository.findByOrderId(order.getId());

                return toOrderDomain(order, orderItems);
            })
            .collect(Collectors.toList());
    }

    private Order toOrderDomain(OrderEntity orderEntity, List<OrderItemEntity> orderItemEntities) {
        List<OrderItem> orderItems = orderItemEntities.stream()
            .map(this::toOrderItemDomain)
            .collect(Collectors.toList());

        return new Order(
            orderEntity.getId(),
            orderEntity.getCustomerId(),
            orderEntity.getTotalPrice(),
            orderEntity.getStatus(),
            orderEntity.getCreatedAt(),
            orderItems
        );
    }

    private OrderItem toOrderItemDomain(OrderItemEntity itemEntity) {
        return new OrderItem(
            itemEntity.getId(),
            itemEntity.getProductId(),
            itemEntity.getProductName(),
            itemEntity.getProductOption(),
            itemEntity.getUnitPrice(),
            itemEntity.getQuantity()
        );
    }

}
