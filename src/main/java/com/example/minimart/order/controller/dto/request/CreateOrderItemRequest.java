package com.example.minimart.order.controller.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;

import java.math.BigDecimal;

@Schema(description = "주문 항목 요청")
public class CreateOrderItemRequest {

    @Schema(description = "상품 ID", example = "101", required = true)
    private Long productId;

    @Schema(description = "상품 이름", example = "Product A", required = true)
    private String productName;

    @Schema(description = "주문 수량", example = "2", required = true)
    private Integer quantity;

    @Schema(description = "상품 단가", example = "20.00", required = true)
    private BigDecimal price;

    @Schema(description = "옵션 정보", example = "Color: Red")
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
