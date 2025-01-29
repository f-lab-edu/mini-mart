package com.example.minimart.order.repository;

import com.example.minimart.order.repository.entity.Order;
import org.springframework.data.jpa.repository.JpaRepository;

public interface OrderJpaRepository extends JpaRepository<Order, Long> {
}
