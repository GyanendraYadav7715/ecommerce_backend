# API Flow Example

To fully understand how a request propagates through the layers of this Spring Boot application, let's walk through a common API scenario: **Fetching a Order by ID**.

## Step 1: Client Request
An external client (like a Web App) makes an HTTP GET request to the API.

```http
GET /api/v1/orders/123
Authorization: Bearer <valid-jwt-token>
```

## Step 2: Filter Chain (Security)
Before reaching the controller, the request passes through the Spring Security filter chain.
1. `JwtAuthenticationFilter` reads the `Authorization` header.
2. It extracts and validates the token signature.
3. It extracts the `userId` or `username` from the token.
4. It sets the `SecurityContext`, authenticating the user for this specific request.

## Step 3: Controller (`OrderController.java`)
The request is routed to the appropriate handler method based on the `@GetMapping("/api/v1/orders/{id}")` annotation.

```java
@GetMapping("/{id}")
public ResponseEntity<OrderResponseDTO> getOrderById(@PathVariable Long id) {
    // Controller delegates immediately to the Service layer
    OrderResponseDTO order = orderService.getOrderById(id);
    return ResponseEntity.ok(order); // Wraps in HTTP 200 OK
}
```
*Responsibility here*: Route routing, extracting path variables, returning HTTP status codes.

## Step 4: Service (`OrderService.java`)
The Service executes business logic and defines the transaction boundary (if `@Transactional` is used).

```java
public OrderResponseDTO getOrderById(Long id) {
    // 1. Fetch from Database via Repository
    Order orderEntity = orderRepository.findById(id)
        .orElseThrow(() -> new ResourceNotFoundException("Order not found with id: " + id));

    // 2. Perform business checks (e.g., ensure the order belongs to the currently logged in user)
    // SecurityUtils.verifyOwnership(orderEntity.getUserId());

    // 3. Map Entity to DTO
    return orderMapper.toDto(orderEntity);
}
```
*Responsibility here*: Handling "Not Found" logic, verifying permissions, bridging Entity concepts to DTO responses.

## Step 5: Repository (`OrderRepository.java`)
The Service calls the Repository, which is a Spring Data JPA interface.

```java
public interface OrderRepository extends JpaRepository<Order, Long> {
    // Spring Data automatically generates the SQL for findById()
}
```
*Responsibility here*: Transpiling the Java method call into real SQL (`SELECT * FROM orders WHERE id = 123`) and returning the `Order` Entity instance.

## Step 6: Mapper (`OrderMapper.java`)
The Service passes the retrieved Entity to a MapStruct mapper.

```java
@Mapper(componentModel = "spring")
public interface OrderMapper {
    OrderResponseDTO toDto(Order order);
}
```
*Responsibility here*: Transforming the database-centric `Order` object into the client-facing `OrderResponseDTO` without exposing internal fields like `version` or `deletedAt`.

## Step 7: Response
The Controller receives the DTO and serializes it into JSON using Jackson.

```json
{
  "id": 123,
  "status": "PROCESSING",
  "totalPrice": 149.99,
  "createdAt": "2023-11-20T10:00:00Z"
}
```
The client receives the JSON data and a `200 OK` status code.
