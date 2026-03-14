# Best Practices for Spring Boot Development

This document serves as a standard reference for industry best practices when developing RESTful APIs with Spring Boot.

## 1. Controller Design
- **Keep Controllers Thin**: A Controller's only job is to handle the HTTP Request/Response cycle. It should parse input, call a Service, and return an HTTP status code. No business logic!
- **Use ResponseEntity**: Always wrap your response DTOs in `ResponseEntity<T>` or `ResponseEntity.ok(dto)` so you have precise control over HTTP Status codes and headers.
- **Plural Nouns for REST Paths**: Use `/api/v1/users` instead of `/api/v1/user` or `/api/v1/getUser`.

## 2. Service Design
- **Interfaces**: Code against interfaces (e.g., `UserService`) rather than implementations (`UserServiceImpl`) to promote loose coupling and easier mocking.
- **Single Responsibility Principle (SRP)**: A Service should only handle logic for its designated domain. Avoid creating a "God Class" that manages Users, Products, and Carts simultaneously.
- **Transaction Boundaries**: Annotate methods that modify data (insert/update/delete) with `@Transactional`. Place this on the Service method, not the Controller, so a failure in the middle of a business workflow properly rolls back the database.

## 3. DTO vs Entity
- **Never expose Entities directly to the API**: Returning JPA Entities (like `User`) directly from a Controller can lead to recursive JSON infinite loops (e.g., User -> Address -> User) and securely leak sensitive data (like password hashes).
- **MapStruct**: Use libraries like MapStruct to auto-generate mapping code. Do not write manual getter/setters for mapping unless highly complex.

## 4. Validation
- **Fail Early**: Validate as early as possible. Use Bean Validation annotations (`@NotNull`, `@Size`, `@Email`) on DTO fields.
- **@Valid**: Attach `@Valid` to the `@RequestBody` parameter in your Controller. Spring will automatically evaluate it and throw a `MethodArgumentNotValidException` before the Controller code even executes.

## 5. Exception Handling
- **Global Handler**: Use a single class annotated with `@RestControllerAdvice`. This acts as a global catch-all interceptor for exceptions.
- **Custom Exceptions**: Create specific exceptions (e.g., `CartEmptyException`, `UserNotFoundException`) mapping to specific HTTP status codes.
- **RFC 7807**: Use Spring 3's built-in `ProblemDetail` specification to standardize how error JSON looks across all APIs.

## 6. Configuration Management
- **Environment Variables**: Never commit secrets (DB passwords, Stripe keys, JWT secrets) to source control. Use `application.yaml` combined with `${JWT_SECRET}` and supply those at runtime using `.env` files or Docker environment variables.
- **Spring Profiles**: Group configurations by environment. Use `application-dev.yaml` for local development, `application-test.yaml` for CI/CD, and `application-prod.yaml` for production.

## 7. Security Best Practices
- **Statelessness**: REST APIs must be stateless. Do not use HTTP Sessions. Use short-lived Access Tokens (JWT) and long-lived Refresh Tokens.
- **CORS Configuration**: Explicitly define exactly which origins (e.g., `http://localhost:3000`) are allowed to interface with your API via `CorsConfigurationSource`.
- **Method Security**: Use `@PreAuthorize` on Service methods to ensure that the currently authenticated user actually has the geometric rights to modify a specific record. 

## 8. Logging
- **Use Slf4j**: Do not use `System.out.println()`. Use a logger instance: `private static final Logger log = LoggerFactory.getLogger(UserService.class);` or Lombok's `@Slf4j` annotation.
- **Log Levels**: Use `log.debug()` for development data, `log.info()` for significant business events (e.g., "Order created"), and `log.error()` heavily for exceptions. 
