Transaction-Safe Order Engine

Project Overview

This is a simple backend project built using Spring Boot.

It simulates an order system where users can place orders for products.  
The main focus of this project is to handle:

- Transactions
- Concurrency (multiple users ordering at the same time)
- Idempotency (prevent duplicate orders)
- Rollback (if something fails)

-----------------------------

 Design Explanation (Simple)

1. Transaction Handling
The order process is wrapped inside a transaction using `@Transactional`.

This means:
- If everything is successful → order is saved
- If anything fails → everything is rolled back

---

2. Concurrency Control
We used Pessimistic Locking on inventory.

This ensures:
- Two users cannot buy the same product at the same time
- Prevents overselling of stock

-------------------

3. Idempotency
Each order request has a unique `idempotencyKey`.

This ensures:
- If the same request is sent twice
- The system returns the same order instead of creating a new one

--------------------------

 4. Rollback Mechanism
If:
- Stock is not available OR
- Payment fails

Then:
- Order is NOT saved
- Inventory is NOT changed

---------------------

Tech Stack
- Java 17
- Spring Boot
- Spring Data JPA
- HTML
- CSS
- JS
- MySQL

---------------------------

Steps to Run the Project

1. Clone the project
git clone https://github.com/Sanjaykp7/order-engine.git

2. Open in IDE
Open the project in Eclipse 

3. Setup Database
Create a MySQL database:

CREATE DATABASE order_engine_db;
4. Update application.properties

Make sure these are correct:

spring.datasource.url=jdbc:mysql://localhost:3306/order_engine_db
spring.datasource.username=root
spring.datasource.password=//use a password
spring.jpa.hibernate.ddl-auto=update
5. Run the application

Run:
right click on project and run as spring boot project
after see if server is running 

goto browser and search -- http://localhost:8080/

6. Test APIs
Use Postman:

Get Products
GET http://localhost:8080/products
Create Order
POST http://localhost:8080/orders

Sample Body:

{
  "userId": 1,
  "idempotencyKey": "test-123",
  "items": [
{ "productId": 1, "quantity": 1 }
  ]
}

Features Implemented
Order creation with transaction safety
Inventory management
Concurrency handling using locks
Idempotency support
Rollback on failure
