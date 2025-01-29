package com.example.minimart.order.infra;

import com.example.minimart.order.infra.entity.Order;
import org.springframework.data.jpa.repository.JpaRepository;

public interface OrderJpaRepository extends JpaRepository<Order, Long> {
}
