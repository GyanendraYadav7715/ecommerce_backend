# Database Design

This document covers the structure of the data persistence layer. The project uses MySQL as its relational database and Hibernate (via Spring Data JPA) as its Object-Relational Mapper (ORM).

## Core Entities & Relationships

The database is structured around several key domain entities essential for an E-Commerce platform.

### User Management
- **`User`**: The central entity representing a person interacting with the system.
  - **Relationships**:
    - `One-to-Many` with **`Address`**: A user can have multiple shipping addresses.
    - `One-to-One` with **`Profile`**: A user has a single extended profile with additional details.
    - `One-to-One` with **`Cart`**: A user has exactly one active shopping cart at any given time.
    - `One-to-Many` with **`Order`**: A user can place multiple historic orders.
    - `One-to-One` with **`RefreshToken`**: Used to manage long-lived authentication sessions.

### Product Catalog
- **`Category`**: Represents a grouping of products (e.g., Electronics, Clothing).
  - **Relationships**:
    - `One-to-Many` with **`Product`**: A category contains multiple products.
- **`Product`**: Represents an individual item for sale (price, inventory count, name, description).
  - **Relationships**:
    - `Many-to-One` with **`Category`**: A product belongs to a specific category.

### Shopping Cart
- **`Cart`**: Represents an active shopping session.
  - **Relationships**:
    - `One-to-One` with **`User`**: Belongs to a specific user.
    - `One-to-Many` with **`CartItem`**: Contains multiple disparate items.
- **`CartItem`**: An intermediary entity linking a Cart to a specific Product along with a chosen quantity.
  - **Relationships**:
    - `Many-to-One` with **`Cart`**.
    - `Many-to-One` with **`Product`**.

### Order Processing
- **`Order`**: A historical record of a completed checkout. Includes an `OrderStatus` enum (e.g., PENDING, PAID, SHIPPED).
  - **Relationships**:
    - `Many-to-One` with **`User`**.
    - `One-to-Many` with **`OrderItem`**: A snapshot of the products purchased.
- **`OrderItem`**: Similar to a CartItem but acts as an immutable ledger entry.
  - **Relationships**:
    - `Many-to-One` with **`Order`**.
    - `Many-to-One` with **`Product`**. (Stores the historical price at the time of purchase in case the product price changes later).

## Primary and Foreign Keys
- **Primary Keys**: Every table uses a single `BIGINT` (mapped to `Long` in Java) auto-incremented primary key, commonly labeled `id`.
- **Foreign Keys**: Relationships mandate strict referential integrity.
  - E.g., The `addresses` table has a `user_id` column acting as a foreign key pointing to `users(id)`. This prevents deleting a user without handling their associated addresses first (unless Cascade deletes are configured).

## ORM Mapping Considerations
- **Lazy Loading**: `FetchType.LAZY` should be (and typically is) preferred on `@OneToMany` and `@ManyToOne` relationships to prevent the "N+1 select" performance problem.
- **Cascading**: Operations on the root entity (like `Order`) can cascade down to its children (`OrderItem`) using `cascade = CascadeType.ALL`, meaning saving an Order automatically saves its Items.

## Database Schema Design Observations (Potential Flaws)
Based on common Spring Boot E-commerce projects:
1. **Price Data Types**: If `price` fields are modeled as `Double` or `Float` in the entities, this is a bad schema design. Financial data must be modeled as `BigDecimal` in Java and `DECIMAL(19,4)` in SQL to prevent floating-point precision loss.
2. **Soft Deletes**: Standard e-commerce structures usually employ "Soft Deletes" (e.g., a `deleted_at` timestamp or `is_deleted` boolean) for Products and Users. Hard deleting a Product breaks historic `OrderItem` references if the foreign keys are strictly enforced without `SET NULL`.
3. **Auditing**: Missing `created_at` and `updated_at` timestamps on critical tables (like Orders and Users) is a common oversight. JPA provides `@CreatedDate` and `@LastModifiedDate` for this exact purpose.
