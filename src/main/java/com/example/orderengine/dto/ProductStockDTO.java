package com.example.orderengine.dto;

public class ProductStockDTO {
    private Long productId;
    private String name;
    private Double price;
    private Integer stock;

    public ProductStockDTO(Long productId, String name, Double price, Integer stock) {
        this.productId = productId;
        this.name = name;
        this.price = price;
        this.stock = stock;
    }

    public Long getProductId() { return productId; }
    public String getName() { return name; }
    public Double getPrice() { return price; }
    public Integer getStock() { return stock; }
}
