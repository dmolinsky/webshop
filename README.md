# Spring Webshop Project

This is a full-featured webshop application built using **Spring MVC**, **Thymeleaf**, and **Java**, developed as part of a backend Java developer course at [ITHS](https://www.iths.se/). The project was created to demonstrate practical knowledge of modern Java backend development, REST API integration, and MVC architecture in a real-world e-commerce context.

## Features

- **User Authentication**
  - Registration and login with role-based access control (`USER`, `ADMIN`)
  
- **Product Catalog**
  - Browse all products
  - Filter/search by category and name
  - View individual product pages with images

- **Shopping Cart**
  - Add, remove, and update product quantities
  - Proceed to checkout and place an order

- **Order Management**
  - Orders stored with status tracking (e.g., `PENDING`)
  - Order confirmation emails sent on checkout

- **Admin Panel**
  - Manage product inventory (add products with images)
  - View and update all customer orders

- **REST API (CRUD)**
  - Create, read, update, and delete products via `/api/products`
  - Example: `GET /api/products`, `POST /api/products`, etc.

## Technologies Used

- Java 21
- Spring Boot 3.4.4
- spring-boot-starter-web – REST API and routing
- spring-boot-starter-thymeleaf – Server-side rendering
- spring-boot-starter-data-jpa – Database integration with JPA
- spring-boot-starter-validation – Form and DTO validation
- spring-boot-starter-mail – Sends email confirmations
- Jakarta Validation – For validating user input
- MySQL Connector/J – Connects to a MySQL database
- Lombok (optional) – Reduces boilerplate code in models
- jbcrypt – For secure password hashing
- JUnit & Mockito – For unit testing and mocking

## 🎯 Project Goals

This project was built to:

- Practice implementing a web application using Spring MVC with both frontend (Thymeleaf) and backend logic.
- Demonstrate understanding of MVC, REST, dependency injection, and validation in Java.
- Prepare for LIA (internship) by producing a complete, well-structured portfolio-ready project.

## 🚀 How to Run

1. Clone the repository:
   ```bash
   git clone https://github.com/dmolinsky/webshop.git
