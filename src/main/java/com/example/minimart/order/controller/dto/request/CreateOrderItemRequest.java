package com.example.minimart.order.controller.dto.request;

import java.math.BigDecimal;

public class CreateOrderItemRequest {

    private Long productId;
    private String productName;
    private Integer quantity;
    private BigDecimal price;
    private String option;

    protected CreateOrderItemRequest() {
    }

    public CreateOrderItemRequest(
        Long productId,
        String productName,
        Integer quantity,
        BigDecimal price,
        String option
    ) {
        this.productId = productId;
        this.productName = productName;
        this.quantity = quantity;
        this.price = price;
        this.option = option;
    }

    public Long getProductId() {
        return productId;
    }

    public String getProductName() {
        return productName;
    }

    public Integer getQuantity() {
        return quantity;
    }

    public BigDecimal getPrice() {
        return price;
    }

    public String getOption() {
        return option;
    }

}
