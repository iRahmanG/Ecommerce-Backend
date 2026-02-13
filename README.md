
#  E-Commerce Backend (Spring Boot)

A production-style E-Commerce backend built using **Spring Boot**, implementing clean architecture principles, DTO separation, transaction management, optimistic locking, and proper REST exception handling.

---

##  Tech Stack

* Java 17
* Spring Boot 3
* Spring Data JPA
* Hibernate
* MySQL / H2
* Maven
* REST APIs
* Git (feature-branch workflow)

---

##  Architecture Overview

This project follows a layered architecture:

```
Controller → DTO → Service → Repository → Entity → Database
```

### Key Design Principles Used

* Separation of concerns
* DTO pattern (no direct entity exposure)
* Transaction management
* Optimistic locking for concurrency control
* Global exception handling
* Clean order processing flow

---

#  Implemented Features

##  Cart System

* User cart creation
* Add products to cart
* Cart item management

---

##  Order Flow (Core Feature)

### Order Placement Logic:

1. Fetch user's cart
2. Validate product stock
3. Deduct inventory
4. Create Order
5. Create OrderItems
6. Simulate payment
7. Clear cart

All operations run inside a **transactional boundary** to ensure consistency.

---

##  Payment Simulation

* Basic payment simulation logic
* Payment status update during order placement
* Designed to be replaceable with real payment gateway

---

##  Optimistic Locking

* Implemented using `@Version`
* Prevents overselling during concurrent order placement
* Ensures data consistency under high traffic

---

##  DTO Layer

* Request DTOs for API input
* Response DTOs for clean API output
* Prevents exposing internal entity structure

---

##  Global Exception Handling

Implemented using:

```java
@RestControllerAdvice
```

Handles:

* Business exceptions (e.g., Out of stock → 400)
* System errors → 500

Provides clean JSON error responses.

---

#  API Endpoints

## Place Order

```
POST /api/orders
```

## Get All Orders

```
GET /api/orders
```

## Get Order By ID

```
GET /api/orders/{id}
```

---

#  Project Structure

```
com.example.ecommerce
│
├── controller
├── service
├── repository
├── entity
├── dto
├── exception
└── EcommerceApplication.java
```

---

#  How to Run

1. Clone the repository
2. Configure database in `application.properties`
3. Run:

```
mvn spring-boot:run
```

Application runs on:

```
http://localhost:9090
```

---

#  Future Improvements

* JWT Authentication & Authorization
* Role-based access (Admin/User)
* Real payment gateway integration
* Order status lifecycle (PLACED → SHIPPED → DELIVERED)
* Pagination & sorting
* Swagger/OpenAPI documentation
* Integration testing

---

#  Learning Outcomes

This project demonstrates:

* Real-world backend architecture
* Transaction management
* Concurrency handling
* REST API best practices
* Clean Git workflow using feature branches
* Production-style error handling

---

#  Author

Built as part of advanced backend engineering practice and architectural learning.

---
