package com.example.minimart.order.repository.entity;

public enum OrderStatus {

    PENDING("Pending", "주문이 접수되었으나 아직 확인되지 않았습니다."),
    CONFIRMED("Confirmed", "주문이 확인되어 처리 중입니다."),
    SHIPPED("Shipped", "주문이 고객에게 발송되었습니다."),
    DELIVERED("Delivered", "주문이 고객에게 전달되었습니다."),
    CANCELLED("Cancelled", "주문이 취소되었습니다.");

    private String name;
    private String description;

    OrderStatus(String name, String description) {
        this.name = name;
        this.description = description;
    }

    public String getName() {
        return name;
    }

    public String getDescription() {
        return description;
    }

}
