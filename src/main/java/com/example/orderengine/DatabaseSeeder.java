package com.example.orderengine;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import com.example.orderengine.entity.Inventory;
import com.example.orderengine.entity.Product;
import com.example.orderengine.entity.User;
import com.example.orderengine.repository.InventoryRepository;
import com.example.orderengine.repository.ProductRepository;
import com.example.orderengine.repository.UserRepository;

@Component
public class DatabaseSeeder implements CommandLineRunner {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private InventoryRepository inventoryRepository;

    @Override
    public void run(String... args) throws Exception {
        
        // Only seed data if database is completely empty
        if (userRepository.count() == 0) {
            System.out.println("Seeding database with initial testing data...");

            // Create a test user
            User user = new User("John Doe");
            user = userRepository.save(user);

            // Create a test product
            Product product = new Product("Laptop", 1200.50);
            product = productRepository.save(product);

            // Give the product an inventory stock of 10
            Inventory inventory = new Inventory(product.getId(), 10);
            inventoryRepository.save(inventory);

            System.out.println("==========================================");
            System.out.println("Test Data Generated Successfully!");
            System.out.println("User ID: " + user.getId());
            System.out.println("Product ID: " + product.getId());
            System.out.println("Starting Inventory Stock: " + inventory.getQuantity());
            System.out.println("==========================================");
        }
    }
}
