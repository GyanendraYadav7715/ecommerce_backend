# Spring Boot REST API E-Commerce Starter

## Project Overview
This project is a fully-featured, backend REST API designed for E-Commerce applications. Built with Spring Boot, it handles user authentication, product catalog management, shopping cart operations, checkout processing, and secure payments via Stripe. It serves as a comprehensive starting point or learning resource for building enterprise-grade backend systems.

## Problem this project solves
Building a secure, scalable e-commerce backend from scratch requires handling complex domains (like orders and payments) and integrating crucial security layers. This project solves that by providing a pre-configured, structured architecture that handles:
- Stateless JWT-based Authentication
- Structured Entity Relationships (Users, Orders, Carts, Products)
- Secure Payment Gateway Integration (Stripe)
- Standardized DTO mappings and global exception handling

## High Level Architecture
The application uses a strict **Layered (N-Tier) Architecture**:
- **Presentation Layer**: REST Controllers route HTTP requests and handle validation.
- **Business Layer**: Services execute core domain logic, managing transactions and calling external APIs.
- **Data Access Layer**: Spring Data JPA Repositories interface with the MySQL database.
- **Security Layer**: Spring Security filters intercept requests to validate JWTs.

*Data Flow*: Request -> Controller -> DTO -> Service -> Mapper -> Entity -> Repository -> MySQL Database.

## Folder Structure
- `src/main/java/com/codewithmosh/store/`
  - `config/`: Application configurations (Security, CORS, Stripe).
  - `controllers/`: HTTP REST Endpoints.
  - `dtos/`: Data Transfer Objects for Request/Response payloads.
  - `entities/`: JPA Domain Models mapping to database tables.
  - `exceptions/`: Custom exceptions and global `@ControllerAdvice` handlers.
  - `filter/`: Security and Request interception filters.
  - `mappers/`: MapStruct interfaces for converting Entities to DTOs.
  - `repositories/`: Spring Data JPA Data Access interfaces.
  - `services/`: Core business logic implementation.
  - `validations/`: Custom Bean Validation logic.

## Technology Stack
- **Java 21**: The latest LTS Java release.
- **Spring Boot 3.4.1**: the core framework.
- **Spring Security & JJWT**: Authentication and Authorization.
- **Spring Data JPA & Hibernate**: ORM for Database interaction.
- **MySQL**: Relational Database Management System.
- **Flyway**: Database Migration and version control.
- **MapStruct**: Type-safe Bean mapping.
- **Lombok**: Boilerplate code reduction.
- **Stripe SDK**: Payment Processing.
- **Springdoc OpenAPI (Swagger)**: Interactive API Documentation.

## Dependencies Explanation
Detailed explanations of every dependency can be found in `learning/dependencies.md`.
Key highlights: 
- `spring-boot-starter-web`: For REST controllers and Tomcat.
- `spring-boot-starter-data-jpa`: For repository abstraction over JPA.
- `spring-boot-starter-validation`: For checking DTO integrity before processing.

## Setup Instructions
1. **Prerequisites**: Ensure you have Java 21, Maven, and MySQL installed globally or running via Docker.
2. **Clone the Repository**:
   ```bash
   git clone <repository-url>
   cd spring-api-starter
   ```
3. **Environment Setup**:
   Create a `.env` file in the root directory (copy from `.env.example`) and populate the keys:
   ```env
   JWT_SECRET=your_super_secret_jwt_key
   ACTOKEXP=3600000
   RETOKEXP=864000000
   STRIPE_SECRET_KEY=sk_test_yourkey
   STRIPE_WEBHOOK_SECRET_KEY=whsec_yourkey
   ```
4. **Database Setup**:
   Ensure MySQL is running with a root user password `7539` (or update `application.yaml` and `.env` accordingly). Flyway will automatically create the `store_api` database schema upon startup.

## How to run the project

### Option 1: Using Docker (Recommended)
You can run the entire application (API + MySQL Database) using a single command with Docker Compose:
```bash
docker-compose up -d --build
```
This will automatically:
1. Start a MySQL 8 container setting up the `store_api` database.
2. Build the Spring Boot application.
3. Start the API container on port 8080 once the database is healthy.

To stop the containers, run:
```bash
docker-compose down
```

### Option 2: Using Maven Wrapper (Local run)
Ensure you have MySQL running locally with the credentials matching your `.env` or `application.yaml`. Then use the Maven Wrapper to build and run the application:
```bash
./mvnw spring-boot:run
```

Once running (via Docker or Local), the server will start on `http://localhost:8080`.
You can access the Swagger UI Documentation at `http://localhost:8080/swagger-ui.html`.

## Example Requests/Responses

### POST `/api/v1/auth/register`
**Request**:
```json
{
  "name": "Jane Doe",
  "email": "jane@example.com",
  "password": "Password123!"
}
```
**Response (200 OK)**:
```json
{
  "accessToken": "eyJhbGciOiJIUzI1NiIsIn...",
  "refreshToken": "def50200..."
}
```

### GET `/api/v1/products`
**Response (200 OK)**:
```json
[
  {
    "id": 1,
    "name": "Wireless Headphones",
    "price": 99.99,
    "categoryId": 2
  }
]
```

## Future Improvements
- **Caching**: Integrate Redis to cache the product catalog for faster reads.
- **Asynchronous Processing**: Use RabbitMQ or Kafka to handle Order Confirmation Emails and Stripe Webhook events asynchronously.
- **Microservices**: Break down the monolithic architecture into a microservices pattern (Auth Service, Product Service, Order Service) using Spring Cloud if the application scales significantly.
- **Dockerization**: Add a `Dockerfile` and `docker-compose.yml` to package the application and MySQL together for 1-click deployments.

---
### **Note**: 
To learn the depth of this project, architecture, code-reviews, and industry standards, explore the `/learning` directory in the root of the repository.
