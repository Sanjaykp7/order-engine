package com.example.orderengine.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.orderengine.dto.ProductStockDTO;
import com.example.orderengine.entity.Inventory;
import com.example.orderengine.entity.Product;
import com.example.orderengine.repository.InventoryRepository;
import com.example.orderengine.repository.ProductRepository;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/products")
public class ProductController {

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private InventoryRepository inventoryRepository;

    @GetMapping
    public List<ProductStockDTO> getProductsWithStock() {
        List<Product> products = productRepository.findAll();
        
        return products.stream().map(product -> {
            Inventory inventory = inventoryRepository.findByProductId(product.getId());
            Integer stock = (inventory != null) ? inventory.getQuantity() : 0;
            return new ProductStockDTO(product.getId(), product.getName(), product.getPrice(), stock);
        }).collect(Collectors.toList());
    }
}
