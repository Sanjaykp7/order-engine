package com.example.orderengine.dto;

import java.util.List;

public class OrderRequestDTO {
    private Long userId;
    private String idempotencyKey;
    private List<OrderItemRequest> items;

    public OrderRequestDTO() {
    }

    public OrderRequestDTO(Long userId, String idempotencyKey, List<OrderItemRequest> items) {
        this.userId = userId;
        this.idempotencyKey = idempotencyKey;
        this.items = items;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public String getIdempotencyKey() {
        return idempotencyKey;
    }

    public void setIdempotencyKey(String idempotencyKey) {
        this.idempotencyKey = idempotencyKey;
    }

    public List<OrderItemRequest> getItems() {
        return items;
    }

    public void setItems(List<OrderItemRequest> items) {
        this.items = items;
    }
}
