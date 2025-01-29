package com.example.minimart.order.repository;

import com.example.minimart.order.repository.entity.OrderItem;
import org.springframework.data.jpa.repository.JpaRepository;

public interface OrderItemJpaRepository extends JpaRepository<OrderItem, Long> {
}
