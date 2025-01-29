    package com.example.minimart.order.infra.entity;

    import jakarta.persistence.*;

    import java.math.BigDecimal;
    import java.time.LocalDateTime;
    import java.util.Objects;

    @Table(name = "orders")
    @Entity
    public class OrderEntity {

        @Id
        @GeneratedValue(strategy = GenerationType.IDENTITY)
        private Long id;

        @Column(nullable = false)
        private Long customerId;

        @Column(nullable = false, precision = 19, scale = 2)
        private BigDecimal totalPrice;

        @Enumerated(EnumType.STRING)
        @Column(nullable = false)
        private OrderStatus status;

        @Column(nullable = false, updatable = false)
        private LocalDateTime createdAt;

        @Column(nullable = false)
        private LocalDateTime updatedAt;

        @Column(nullable = false)
        private boolean deleted = false;

        protected OrderEntity() {}

        public OrderEntity(Long customerId, BigDecimal totalPrice, OrderStatus status) {
            this.customerId = customerId;
            this.totalPrice = totalPrice;
            this.status = status;
        }

        public OrderEntity(Long id, Long customerId, BigDecimal totalPrice, OrderStatus status) {
            this.id = id;
            this.customerId = customerId;
            this.totalPrice = totalPrice;
            this.status = status;
        }

        @PrePersist
        protected void onCreate() {
            this.createdAt = LocalDateTime.now();
            this.updatedAt = LocalDateTime.now();
        }

        @PreUpdate
        protected void onUpdate() {
            this.updatedAt = LocalDateTime.now();
        }

        public Long getId() {
            return id;
        }

        public Long getCustomerId() {
            return customerId;
        }

        public BigDecimal getTotalPrice() {
            return totalPrice;
        }

        public OrderStatus getStatus() {
            return status;
        }

        public LocalDateTime getCreatedAt() {
            return createdAt;
        }

        public LocalDateTime getUpdatedAt() {
            return updatedAt;
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;
            OrderEntity order = (OrderEntity) o;

            return Objects.equals(id, order.id);
        }

        @Override
        public int hashCode() {
            return Objects.hash(id);
        }

        @Override
        public String toString() {
            return "Order{" +
                "id=" + id +
                ", customerId=" + customerId +
                ", totalPrice=" + totalPrice +
                ", status=" + status +
                ", createdAt=" + createdAt +
                ", updatedAt=" + updatedAt +
                ", deleted=" + deleted +
                '}';
        }

    }
