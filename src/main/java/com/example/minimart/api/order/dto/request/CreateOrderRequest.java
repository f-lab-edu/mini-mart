package com.example.minimart.api.order.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;

import java.math.BigDecimal;
import java.util.List;

@Schema(description = "주문 생성 요청")
public class CreateOrderRequest {

    @Schema(description = "고객 ID", example = "1", required = true)
    private Long customerId;

    @Schema(description = "주문 총 금액", example = "100.00", required = true)
    private BigDecimal totalPrice;

    @Schema(description = "주문 항목 리스트", required = true)
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
