# 🛒 Final Capstone Project – Video Game Store

This project is a **full-stack backend e-commerce API** built with **Java, Spring Boot, JDBC, and MySQL**. It was completed as a multi-phase capstone and focuses on clean architecture, secure REST APIs, and real-world backend patterns.

The application supports:

* Product and category management
* Advanced product search and filtering
* Secure user authentication and authorization
* Persistent, user-based shopping carts

---

## 🧠 Project Architecture

* **Language:** Java
* **Framework:** Spring Boot
* **Database:** MySQL
* **Data Access:** JDBC + DAO Pattern
* **Security:** Spring Security + JWT
* **Testing:** Postman

Layered architecture:

* **Controller Layer** – REST endpoints
* **DAO Layer** – Database access logic
* **Model Layer** – Domain objects
* **Security Layer** – Authentication & authorization

---

## 🚀 Phase 1 – Categories API

### Objective

Build and expose REST endpoints to manage product categories while enforcing admin-only access for write operations.

### Endpoints Implemented

| Method | Endpoint           | Access |
| ------ | ------------------ | ------ |
| GET    | `/categories`      | Public |
| GET    | `/categories/{id}` | Public |
| POST   | `/categories`      | ADMIN  |
| PUT    | `/categories/{id}` | ADMIN  |
| DELETE | `/categories/{id}` | ADMIN  |

### Approach

* Implemented a **CategoriesController** using `@RestController`
* Used **constructor injection** to inject DAO dependencies
* Applied `@PreAuthorize` to protect admin-only routes
* Implemented full CRUD logic in `MySqlCategoryDao`

### Key Takeaway

> Phase 1 reinforced how clean separation between controllers and DAOs simplifies debugging and testing.

---

## 🛠 Phase 2 – Product Search & Bug Fixes

### Objective

Fix existing bugs in the product search and update functionality.

### Bug 1 – Incorrect Search Results

**Problem:**

* Search filters for `minPrice`, `maxPrice`, and `category` returned incorrect or unexpected results.

**Fix:**

* Rewrote SQL logic to properly handle optional parameters
* Used sentinel values and conditional SQL filters
* Ensured correct comparison operators (`>=`, `<=`)

### Bug 2 – Duplicate Products on Update

**Problem:**

* Updating a product inserted new rows instead of updating existing ones

**Root Cause:**

```java
productDao.create(product); // ❌ wrong
```

**Fix:**

```java
productDao.update(id, product); // ✅ correct
```

### Key Takeaway

> A single incorrect DAO method call can silently corrupt data. Defensive coding and testing are essential.

---

## 🛒 Phase 3 – Shopping Cart (Authenticated Feature)

### Objective

Implement a persistent shopping cart tied to authenticated users.

### Endpoints Implemented

| Method | Endpoint              | Description                  |
| ------ | --------------------- | ---------------------------- |
| GET    | `/cart`               | Retrieve current user’s cart |
| POST   | `/cart/products/{id}` | Add or increment product     |
| PUT    | `/cart/products/{id}` | Update product quantity      |
| DELETE | `/cart`               | Clear cart                   |

### Approach

* Used `Principal` to identify the logged-in user
* Retrieved `userId` via `UserDao`
* Stored cart data in a `shopping_cart` database table
* Built cart aggregation logic using `ShoppingCart` and `ShoppingCartItem` models

### Cart JSON Structure

```json
{
  "items": {
    "15": {
      "product": { "productId": 15, "name": "Example Product" },
      "quantity": 2,
      "lineTotal": 129.98
    }
  },
  "total": 129.98
}
```

### Key Takeaway

> This phase tied together security, data access, and business logic into a single cohesive feature.

---

## ⭐ Interesting Code Highlight – Using `@Repository` for the DAO Layer

````java
@Repository
public class MySqlShoppingCartDao extends MySqlDaoBase
        implements ShoppingCartDao {

    private final ProductDao productDao;

    public MySqlShoppingCartDao(DataSource dataSource, ProductDao productDao) {
        super(dataSource);
        this.productDao = productDao;
    }
}
`````
Why this code is important

This snippet highlights an intentional architectural decision: using **`@Repository` instead of `@Component`** for database access classes.
While both annotations register a Spring Bean, **`@Repository`** is specifically designed for the **data access layer**. By using it here, Spring is able to:
- Automatically translate low-level `SQLException` errors into Spring’s unchecked `DataAccessException` hierarchy
- Clearly communicate the role of this class to other developers
- Align the project with Spring’s recommended layered architecture

This choice improves **maintainability**, **debugging**, and **readability**, especially as applications grow in complexity.


---

## 🧩 Why I Used `@Repository` Instead of `@Component`

Although both annotations register a Spring Bean, I intentionally used `@Repository` for DAO classes.

### Reasons:

* `@Repository` is **semantically correct** for database access layers
* Enables **automatic exception translation** from SQL exceptions to Spring’s `DataAccessException`
* Improves readability and communicates intent to other developers

```java
@Repository
public class MySqlShoppingCartDao implements ShoppingCartDao {
    // JDBC logic
}
```

> Using the right annotation improves maintainability and aligns with Spring best practices.

---

## 📚 What I Learned

* How to design REST APIs with real-world constraints
* How to debug security and routing issues effectively
* Why DAO patterns still matter even in modern frameworks
* How small mapping mistakes can cause major data bugs
* How to think like a backend engineer, not just a student

---

## ✅ Final Thoughts

This project strengthened my understanding of **backend architecture, security, and persistence**. Each phase built upon the last, resulting in a production-style API that mirrors real-world e-commerce systems.

---

**Author:** Markus Williams
**Tech Stack:** Java • Spring Boot • MySQL • JDBC • JWT
