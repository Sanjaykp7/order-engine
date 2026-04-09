package com.example.orderengine.service;

import java.util.Optional;
import java.util.Random;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.orderengine.dto.OrderItemRequest;
import com.example.orderengine.dto.OrderRequestDTO;
import com.example.orderengine.entity.Inventory;
import com.example.orderengine.entity.Order;
import com.example.orderengine.entity.OrderItem;
import com.example.orderengine.entity.OrderStatus;
import com.example.orderengine.repository.InventoryRepository;
import com.example.orderengine.repository.OrderItemRepository;
import com.example.orderengine.repository.OrderRepository;

@Service
public class OrderService {

    @Autowired
    private OrderRepository orderRepository;

    @Autowired
    private OrderItemRepository orderItemRepository;

    @Autowired
    private InventoryRepository inventoryRepository;

    private final Random random = new Random();

    @Transactional
    public Order placeOrder(OrderRequestDTO request) {
        
        System.out.println("Order started: " + request.getIdempotencyKey());

        if (request.getItems() == null || request.getItems().isEmpty()) {
            throw new RuntimeException("Order must contain at least one item");
        }

        Optional<Order> existingOrder = orderRepository.findByIdempotencyKey(request.getIdempotencyKey());
        if (existingOrder.isPresent()) {
            return existingOrder.get();
        }
        
        Order order = new Order();
        order.setUserId(request.getUserId());
        order.setIdempotencyKey(request.getIdempotencyKey());
        order.setCreatedAt(java.time.LocalDateTime.now());
        order.setStatus(OrderStatus.INIT);
        
        try {
            order = orderRepository.save(order);
        } catch (org.springframework.dao.DataIntegrityViolationException dive) {
            return orderRepository.findByIdempotencyKey(request.getIdempotencyKey()).get();
        }

        try {
            order.setStatus(OrderStatus.PROCESSING);
            order = orderRepository.save(order);

            for (OrderItemRequest itemReq : request.getItems()) {
                
                if (itemReq.getQuantity() <= 0) {
                    throw new RuntimeException("Invalid quantity");
                }

                Inventory inventory = inventoryRepository.findByProductIdForUpdate(itemReq.getProductId());
                
                if (inventory == null) {
                    throw new RuntimeException("Product inventory not found for ID: " + itemReq.getProductId());
                }

                if (inventory.getQuantity() < itemReq.getQuantity()) {
                    throw new RuntimeException("Out of stock");
                }

                inventory.setQuantity(inventory.getQuantity() - itemReq.getQuantity());
                inventoryRepository.save(inventory);

                OrderItem orderItem = new OrderItem();
                orderItem.setOrderId(order.getId());
                orderItem.setProductId(itemReq.getProductId());
                orderItem.setQuantity(itemReq.getQuantity());
                orderItemRepository.save(orderItem);
            }

            if (!simulatePayment()) {
                throw new RuntimeException("Payment failed, rolling back order");
            }

            order.setStatus(OrderStatus.SUCCESS);
            System.out.println("Order success");
            return orderRepository.save(order);

        } catch (Exception e) {
            System.out.println("Order failed: " + e.getMessage());
            throw e;
        }
    }

    public Order getOrder(Long id) {
        return orderRepository.findById(id)
            .orElseThrow(() -> new RuntimeException("Order not found with ID: " + id));
    }

    private boolean simulatePayment() {
        return random.nextBoolean(); 
    }
}
