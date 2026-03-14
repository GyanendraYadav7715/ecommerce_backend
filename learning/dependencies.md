# Dependencies

This project relies on several key libraries defined in `pom.xml`. Understanding these dependencies is crucial for navigating the codebase and grasping the tools Spring Boot leverages under the hood.

## Core Spring Boot Starters

### `spring-boot-starter-web`
- **Why it is used**: Provides the core components for building RESTful APIs using Spring MVC.
- **Problem it solves**: Auto-configures an embedded Tomcat server, sets up Jackson for JSON serialization/deserialization, and wires `DispatcherServlet` to route HTTP requests to Controllers.
- **Usage**: Enables annotations like `@RestController`, `@GetMapping`, `@PostMapping`, and `@RequestBody`.

### `spring-boot-starter-data-jpa`
- **Why it is used**: Provides Spring Data JPA, integrating Hibernate as the default ORM provider.
- **Problem it solves**: Eliminates boilerplate JDBC and SQL code. Allows developers to interact with the database using Java objects (Entities) and Repository interfaces.
- **Usage**: Enables `@Entity`, `@Table`, and the `JpaRepository` interface used across the `repositories/` package.

### `spring-boot-starter-security`
- **Why it is used**: Secures the application.
- **Problem it solves**: Protects endpoints against unauthorized access, handles password hashing, and manages Cross-Origin Resource Sharing (CORS).
- **Usage**: Configured in the `config/` package. It intercepts all incoming requests to ensure they are authenticated (e.g., via JWTs).

### `spring-boot-starter-validation`
- **Why it is used**: Provides Bean Validation API (Hibernate Validator).
- **Problem it solves**: Ensures incoming data (DTOs) meets specific criteria before processing it, preventing bad data from entering the business logic or database.
- **Usage**: Enables annotations like `@NotBlank`, `@Size`, and `@Email` on DTO fields, combined with `@Valid` on Controller method parameters.

## Database & Migrations

### `mysql-connector-j`
- **Why it is used**: The official JDBC driver for MySQL.
- **Problem it solves**: Allows Spring Boot to connect to the MySQL database engine specified in `application.yaml`.

### `flyway-core` & `flyway-mysql`
- **Why it is used**: Database migration tool.
- **Problem it solves**: Treats database schemas as code. It tracks which SQL scripts have been executed, ensuring the database schema evolves consistently across all environments.
- **Usage**: Executes scripts (like `V1__init.sql`) on startup to create tables like `users` and `addresses`.

## Utility Libraries

### `lombok`
- **Why it is used**: A boilerplate-reduction library.
- **Problem it solves**: Automatically generates standard Java methods at compile time based on annotations, keeping Entity and DTO classes clean.
- **Usage**: Used extensively via `@Getter`, `@Setter`, `@NoArgsConstructor`, `@AllArgsConstructor`, and `@Builder`.

### `mapstruct`
- **Why it is used**: DTO-to-Entity mapper.
- **Problem it solves**: Generates type-safe mapping code at compile-time instead of relying on slow reflection at runtime (like ModelMapper).
- **Usage**: Used in the `mappers/` package. Interfaces annotated with `@Mapper` get implementations auto-generated during the Maven build phase.

## Security & Payment

### `jjwt-api`, `jjwt-impl`, `jjwt-jackson`
- **Why it is used**: Java JWT specification.
- **Problem it solves**: Creates, parses, and validates JSON Web Tokens required for stateless authentication.
- **Usage**: Used in `JwtService` to sign tokens upon login and parse tokens in the `JwtAuthenticationFilter` on subsequent requests.

### `stripe-java`
- **Why it is used**: The official Stripe SDK.
- **Problem it solves**: Provides easy, type-safe ways to create Payment Intents, Sessions, and Webhook signature verification for processing real payments.
- **Usage**: Used in the `StripePaymentGateway` to coordinate checkout processes.

## Miscellanous

### `springdoc-openapi-starter-webmvc-ui`
- **Why it is used**: Swagger/OpenAPI 3 integration.
- **Problem it solves**: Automatically generates interactive API documentation from Controller annotations, making it easy to test endpoints from the browser.

### `spring-dotenv`
- **Why it is used**: Loads environment variables from a `.env` file into Spring's environment.
- **Problem it solves**: Keeps sensitive credentials (like database passwords, Stripe secrets, JWT keys) out of the `application.yaml` and source control.
