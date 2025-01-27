package com.example.minimart.order.controller.dto.request;

import java.math.BigDecimal;
import java.util.List;

public class CreateOrderRequest {

    private Long customerId;

    private BigDecimal totalPrice;

    private List<CreateOrderItemRequest> items;

    protected CreateOrderRequest() {
    }

    public CreateOrderRequest(
        Long customerId,
        BigDecimal totalPrice,
        List<CreateOrderItemRequest> items
    ) {
        this.customerId = customerId;
        this.totalPrice = totalPrice;
        this.items = items;
    }

    public Long getCustomerId() {
        return customerId;
    }

    public BigDecimal getTotalPrice() {
        return totalPrice;
    }

    public List<CreateOrderItemRequest> getItems() {
        return items;
    }

}
