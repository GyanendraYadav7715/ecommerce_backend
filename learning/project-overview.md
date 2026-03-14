# Project Overview

## What the Project Does
This project is an E-Commerce REST API built with Spring Boot. It provides all the necessary backend functionalities to run an online store, including user authentication, product catalog management, shopping cart operations, order processing, and secure payment integration via Stripe.

## Core Idea of the System
The core idea is to expose a set of secure, stateless RESTful endpoints that a frontend application (like a React or Angular SPA) can consume. It handles the business logic, data persistence, and third-party integrations (like payment gateways), ensuring that the frontend only needs to worry about presenting the user interface.

## Main Components
- **Authentication & Authorization**: Manages user registration, login, and secure access using JSON Web Tokens (JWT) and Spring Security.
- **Product Management**: Allows users to browse products and categories.
- **Cart Management**: Enables users to add/remove items to/from their shopping session.
- **Checkout & Order Processing**: Handles the conversion of a cart into a finalized order, communicating with Stripe for payment processing.
- **User Profile**: Manages user-specific data like addresses and personal details.

## Data Flow
1. **Client**: Sends an HTTP request (e.g., `POST /api/v1/cart`) with a payload and a JWT in the Authorization header.
2. **Security Filter**: Intercepts the request to validate the JWT. If valid, it sets the authentication context.
3. **Controller**: Receives the validated request, validates the incoming DTOs, and passes the data to the Service layer.
4. **Service**: Contains the core business logic. It may coordinate multiple Repositories or external services (e.g., Stripe) to achieve the desired outcome.
5. **Repository**: Interacts with the MySQL database using Spring Data JPA to fetch or persist Entities.
6. **Response**: The Service returns data (often mapped back to DTOs) to the Controller, which constructs an `ResponseEntity` (typically JSON) to send back to the Client.

## Service Responsibilities
- **UserService & AuthService**: Handle user lifecycle, password hashing, and token issuance/validation.
- **ProductService**: Manages the catalog, filtering, and retrieval of products.
- **CartService**: Manages temporary shopping sessions for users.
- **CheckoutService & PaymentGateway**: Coordinates payment intent creation and verifies successful transactions.
- **OrderService**: Manages the final state of a user's purchase and tracking status.

## How Different Modules Communicate
The architecture strictly follows a layered approach. Controllers exclusively talk to Services. Services talk to Repositories and sometimes to other Services (though this should be carefully managed to avoid circular dependencies). Data is transferred between layers using Data Transfer Objects (DTOs) to decouple the internal database schema (Entities) from the external API contracts. MapStruct is used to facilitate this conversion seamlessly.
