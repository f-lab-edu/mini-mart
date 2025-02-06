-- orders 테이블
CREATE TABLE orders (
    order_id BIGINT AUTO_INCREMENT PRIMARY KEY,
    customer_id BIGINT NOT NULL,
    total_price DECIMAL(19, 2) NOT NULL,
    status ENUM('PENDING', 'CONFIRMED', 'SHIPPED', 'DELIVERED', 'CANCELLED') NOT NULL,
    created_at DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
    deleted BOOLEAN NOT NULL DEFAULT FALSE,
    INDEX idx_orders_customer_id (customer_id),
    INDEX idx_orders_status (status)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

-- order_items 테이블
CREATE TABLE order_items (
     order_item_id BIGINT AUTO_INCREMENT PRIMARY KEY,
     order_id BIGINT NOT NULL,
     product_id BIGINT NOT NULL,
     product_name VARCHAR(255) NOT NULL,
     product_option VARCHAR(255),
     unit_price DECIMAL(19, 2) NOT NULL,
     quantity INT NOT NULL,
     total_price DECIMAL(19, 2) NOT NULL,
     created_at DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
     updated_at DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
     deleted BOOLEAN NOT NULL DEFAULT FALSE,
     INDEX idx_order_items_order_id (order_id),
     INDEX idx_order_items_product_id (product_id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

