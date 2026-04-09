package com.example.orderengine.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.example.orderengine.entity.Order;

@Repository
public interface OrderRepository extends JpaRepository<Order, Long> {

    // Find order by idempotency key to prevent duplicate processing
    Optional<Order> findByIdempotencyKey(String idempotencyKey);
}
