# Architecture

This project follows a classic **Layered (N-Tier) Architecture** commonly used in enterprise Spring Boot applications.

## Layered Architecture Style

In layered architecture, components are organized according to their specific roles and responsibilities. The flow of dependency is strictly top-down.

1. **Presentation Layer (Controllers)**: Handles HTTP requests, input validation, and HTTP responses.
2. **Business Layer (Services)**: Contains all business logic, coordination, and transaction boundaries.
3. **Data Access Layer (Repositories)**: Responsible for database interaction and data retrieval.

### Why Layered Architecture?
- **Separation of Concerns**: Each layer has a distinct responsibility. Changing the database doesn't impact the REST API logic, and vice versa.
- **Testability**: Services can be tested in isolation by mocking Repositories. Controllers can be tested by mocking Services.
- **Maintainability**: It provides a predictable structure that developers are instantly familiar with.

## Request Flow

1. An HTTP request enters the application.
2. It hits **Filters** (e.g., `JwtAuthenticationFilter`) which validate security credentials.
3. The request reaches the **Controller**. The Controller maps JSON payloads to DTOs and handles Bean Validation (`@Valid`).
4. The Controller invokes a method on the **Service**, passing the DTOs or basic data types.
5. The Service executes business rules. If it needs data, it calls the **Repository**.
6. The Repository accesses the MySQL Database, fetching or saving an **Entity**.
7. The Repository returns the Entity to the Service.
8. The Service uses a **Mapper** (MapStruct) to convert the Entity back into a response DTO.
9. The Controller receives the Response DTO and wraps it in a `ResponseEntity` with the appropriate HTTP status code.
10. The HTTP response is returned to the client.

## Dependency Direction

Dependencies always flow *inwards* and *downwards*.

- **Controllers** depend on **Services** and **DTOs**. (Never on Repositories directly).
- **Services** depend on **Repositories**, **Entities**, **DTOs**, and **Mappers**. (Never on Controllers or HTTP objects like `HttpServletRequest`).
- **Repositories** depend on **Entities** and the Spring Data Framework. (Never on Services or DTOs).
- **Entities** should ideally be plain Java objects (POJOs) with JPA annotations, depending on nothing else.

*Note: Following this dependency direction ensures that the business logic (Services) remains decoupled from the web layer (HTTP specifics), making it easier to adopt new frameworks or add alternative entry points (like a message consumer instead of a REST endpoint).*
