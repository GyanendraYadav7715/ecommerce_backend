# Code Review

This document provides a professional code review of the existing Spring Boot codebase, pointing out deviations from industry standards, potential bugs, bad practices, and security/performance issues.

## 1. Bad Exception Handling & `Optional` Usage
**File**: `AuthController.java` (login method)
**What is wrong**:
```java
var user = userRepository.findByEmail(request.getEmail()).orElseThrow();
```
**Why it is bad**: Calling `orElseThrow()` without an argument throws a generic `NoSuchElementException`. This will typically result in a `500 Internal Server Error` instead of a `404 Not Found` or `401 Unauthorized`.
**How to fix it**: Always provide a specific business exception that the `@ControllerAdvice` can catch and map to a proper HTTP status code.
**Improved Code**:
```java
var user = userRepository.findByEmail(request.getEmail())
    .orElseThrow(() -> new ResourceNotFoundException("User not found with email: " + request.getEmail()));
```

## 2. Unidiomatic `Optional` Handling
**File**: `OrderService.java` (getOrder method)
**What is wrong**:
```java
var order = orderRepository.getOrderWithItmes(orderId).orElse(null);
if(order == null){
    throw new OrderNotFoundException();
}
```
**Why it is bad**: This defeats the entire purpose of the `Optional<T>` API, which is designed to avoid explicit `null` checks. It is verbose and non-idiomatic Java.
**How to fix it**: Chain the `.orElseThrow()` method directly.
**Improved Code**:
```java
var order = orderRepository.getOrderWithItmes(orderId)
    .orElseThrow(() -> new OrderNotFoundException("Order with ID " + orderId + " not found"));
```

## 3. Naming Convention Violations
**File**: `AuthService.java` & `OrderService.java`
**What is wrong**: 
Method named `getcurrentUser()`.
```java
var user = authService.getcurrentUser();
```
**Why it is bad**: Java strictly follows `camelCase` for method names. The "c" in current should be capitalized. Minor details like this reflect code quality and professionalism.
**How to fix it**: Rename the method to `getCurrentUser()`.

## 4. Tight Coupling & Magic Strings
**File**: `AuthController.java`
**What is wrong**:
```java
var cookie = new Cookie("refreshToken", refreshToken.toString());
cookie.setPath("/auth/refresh");
```
**Why it is bad**: Hardcoding string literals ("refreshToken", "/auth/refresh") across multiple files makes the code brittle. If the API version changes (e.g., to `/api/v2/auth/refresh`), you have to hunt down these strings.
**How to fix it**: Extract these into `public static final String` constants or read them from `application.yaml` via `@Value` or `@ConfigurationProperties`.

## 5. Security Issues: Missing Validation & Method Security
**General Observation**:
**What is wrong**: While DTOs have `@Valid`, the actual entity boundaries and ID verifications are sometimes lacking. For example, ensuring that a user can only perform actions on their own `Cart` or `Order`.
**Why it is bad**: It leads to IDOR (Insecure Direct Object Reference) vulnerabilities where User A can guess User B's Order ID and view their purchase history.
**How to fix it**: `OrderService` handles this manually: `if(!order.isPlacedBy(user)) throw accessDenied;`. A more robust Spring way is to use Method Security:
```java
@PreAuthorize("@securityService.isOwner(#orderId, authentication.principal)")
public OrderDto getOrder(Long orderId) { ... }
```

## 6. Global Exception Handling (Good, but needs review)
The project has an `exceptions/` folder. Ensure there is a `@ControllerAdvice` or `@RestControllerAdvice` class that catches:
1. `MethodArgumentNotValidException` (Returns 400 Bad Request with field errors).
2. `ResourceNotFoundException` (Returns 404).
3. `AccessDeniedException` (Returns 403 Forbidden).
4. Generic `Exception` (Returns 500, but logs the stack trace privately).

## 7. Performance Issue: N+1 Selects Problem
**General Note on `OrderRepository` & others**:
**What is wrong**: If a Service calls `orderRepository.findAll()` and then maps each order to a DTO which requires `order.getItems()`, Hibernate will execute 1 query to get the orders, and N queries to get the items for each order.
**How to fix it**: The codebase seems to be aware of this, as seen in `getOrderWithItmes(orderId)` (note the typo in "Items"). Ensure you use `JOIN FETCH` or `@EntityGraph` in the repository:
```java
@Query("SELECT o FROM Order o LEFT JOIN FETCH o.items WHERE o.id = :id")
Optional<Order> getOrderWithItems(@Param("id") Long id);
```
