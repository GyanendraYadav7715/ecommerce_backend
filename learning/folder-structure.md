# Folder Structure

This document explains the organization of the source code within `src/main/java/com/codewithmosh/store/` and best practices for each directory.

## Root Package (`com.codewithmosh.store`)
Contains the main Spring Boot application class (`StoreApplication.java`).

### `config/`
- **Purpose**: Holds configuration classes and beans.
- **Responsibility**: Configuring external libraries, security settings (Spring Security filter chains), CORS policies, and other application-wide settings (e.g., Stripe API key setup, OpenAPI/Swagger configuration).
- **Best Practices**: Keep configurations isolated. If a configuration file gets too large, split it by concern (e.g., `SecurityConfig`, `OpenApiConfig`).

### `controllers/`
- **Purpose**: The entry point for incoming HTTP requests (REST API endpoints).
- **Responsibility**: Mapping HTTP methods and routes (e.g., `@GetMapping("/api/users")`), validating incoming requests, calling down to the `services/` layer, and returning proper HTTP responses (with `ResponseEntity`).
- **Best Practices**: Controllers should be "thin". They should not contain business logic. They simply delegate work to services and handle HTTP concerns (status codes, headers).

### `dtos/` (Data Transfer Objects)
- **Purpose**: Objects used to encapsulate data and send it from one subsystem of an application to another.
- **Responsibility**: Carrying request payloads (e.g., `UserRegistrationDTO`) and response payloads. They prevent exposing internal database structures (Entities) directly to the client.
- **Best Practices**: Separate inputs and outputs (e.g., `CreateProductRequest` vs `ProductResponse`). Apply validation annotations (`@NotNull`, `@Size`) here rather than on Entities.

### `entities/`
- **Purpose**: Represents the domain models of the application, mapping to database tables.
- **Responsibility**: Defining the schema, relationships (One-to-Many, Many-to-Many), and constraints of the data stored in the database. Annotated with JPA annotations (`@Entity`, `@Table`, `@Id`).
- **Best Practices**: Entities should strictly model data and relationships. Avoid putting business logic or DTO mapping logic inside entities to keep them pure.

### `exceptions/`
- **Purpose**: Custom exception classes and global error handlers.
- **Responsibility**: Defining application-specific errors (e.g., `ResourceNotFoundException`) and a `@ControllerAdvice` to intercept exceptions and map them to unified HTTP response formats (like `ProblemDetail` or custom error objects).
- **Best Practices**: Always use domain-specific exceptions rather than generic `RuntimeException` to provide clearer error messages to the client.

### `filter/`
- **Purpose**: Intercepts HTTP requests early in the pipeline.
- **Responsibility**: Implementing filters like JWT validation (`JwtAuthenticationFilter`) that run before the request hits the Controller.
- **Best Practices**: Keep filters lightweight, as they run on every request. Only handle cross-cutting concerns (security, logging, MDC context).

### `mappers/`
- **Purpose**: Interfaces or classes that map data between Data Transfer Objects (DTOs) and Entities.
- **Responsibility**: Converting a `Product` entity to a `ProductDTO` or vice versa. This project uses [MapStruct](https://mapstruct.org/) which auto-generates efficient mapping code at compile-time.
- **Best Practices**: Keep mappers isolated from business logic. Inject them into Services, not Controllers.

### `repositories/`
- **Purpose**: The Data Access Layer (DAL).
- **Responsibility**: Extending `JpaRepository` or `CrudRepository` to provide database interactions without writing boilerplate SQL. They handle CRUD operations and custom query methods.
- **Best Practices**: Repositories should only deal with database logic. Do not put business rules here.

### `services/`
- **Purpose**: The core application logic.
- **Responsibility**: Implementing business rules, calculating values, coordinating transactions (using `@Transactional`), and wiring together different repositories or other services.
- **Best Practices**: Services should be the "thickest" part of the application. They are designed as interfaces with implementations (`UserService` vs `UserServiceImpl`), though simple applications might just use classes directly.

### `validations/`
- **Purpose**: Custom validation logic.
- **Responsibility**: Defining custom Bean Validation annotations (e.g., `@UniqueEmail`) and their corresponding validator classes that implement `ConstraintValidator`.
- **Best Practices**: Reusable validation logic should live here rather than cluttering controllers or services.
