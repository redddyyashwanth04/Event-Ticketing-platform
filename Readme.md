# 🎟️ Enterprise Event Booking & Ticketing Platform (Backend)

A production-ready, highly secure, and concurrency-safe backend REST API built with **Spring Boot 3** and **Spring Security 6**. This system powers a complete event management and ticketing lifecycle—from user registration and multi-tier event publishing to atomic ticket booking and secure, fraud-resistant gate QR pass validation.

---

## 🏗️ Core Backend Engineering & Architectural Fundamentals

This project was architected from first principles to demonstrate mastery over enterprise Java and backend design patterns:

1. **Stateless Authentication & JWT (JSON Web Tokens)**
    * **Concept:** Instead of traditional server-side HTTP sessions and cookies, the application implements token-based stateless security using the `jjwt` library.
    * **Why:** This decouples authentication, allowing the backend to scale horizontally across multiple instances behind a load balancer without sticky sessions.

2. **Custom Security Filter Chain & Request Interception**
    * **Concept:** Implements a custom `OncePerRequestFilter` (`JwtAuthenticationFilter`) injected **before** Spring Security's default username/password filter.
    * **Why:** Every incoming request header (`Authorization: Bearer <token>`) is intercepted, cryptographically verified, and mapped into Spring's `SecurityContextHolder` in a single execution cycle.

3. **The DTO (Data Transfer Object) Pattern & Bean Validation**
    * **Concept:** Strict separation between database persistence models (`Entity`) and data transport layers (`DTO`). Incoming JSON payloads are intercepted and validated using Jakarta Validation annotations (`@NotBlank`, `@Email`, `@Size`, `@Valid`).
    * **Why:** Protects sensitive database columns (such as password hashes and internal primary keys) from being exposed or leaked over the REST API, while catching malformed requests early with HTTP 400 Bad Request responses.

4. **ACID Transactions & Concurrency-Safe Inventory Management**
    * **Concept:** Critical financial operations (such as ticket purchasing and stock decrementing) are wrapped in Spring's `@Transactional` annotation.
    * **Why:** Prevents overselling race conditions under high concurrency. If stock validation passes but a database write fails midway, the entire transaction rolls back automatically, preserving data integrity.

5. **Method-Level Security via Spring Expression Language (SpEL)**
    * **Concept:** Fine-grained access control enforced directly on controller boundaries using `@PreAuthorize("hasRole('...')")` and `@PreAuthorize("hasAnyRole('...')")`.
    * **Why:** Moves security logic closer to business execution, ensuring strict role separation between `ROLE_ORGANIZER`, `ROLE_ATTENDEE`, and `ROLE_SCANNER`.

---

## 🛠️ Tech Stack

* **Language:** Java 17 (Modern OOP, Lambdas, Streams API)
* **Framework:** Spring Boot 3.x
* **Security:** Spring Security 6, JJWT (Java JSON Web Token)
* **Persistence:** Spring Data JPA, Hibernate, MySQL / PostgreSQL
* **Utilities:** Lombok (boilerplate reduction), Jakarta Validation
* **Build Tool:** Maven

---

## 📁 Project Directory Structure

```text
src/main/java/com/example/Ticketing_project/
│
├── config/              # Security and application configuration beans
│   └── SecurityConfig.java
├── controller/          # REST API endpoints (Entry points)
│   ├── AuthController.java
│   ├── EventController.java
│   ├── BookingController.java
│   └── ScanController.java
├── dto/                 # Data Transfer Objects & API payloads
│   ├── LoginRequest.java
│   ├── RegisterRequest.java
│   ├── JwtResponse.java
│   ├── EventRequest.java
│   ├── EventResponse.java
│   ├── BookingRequest.java
│   ├── BookingResponse.java
│   └── ScanResponse.java
├── entity/              # Database JPA models (Tables & Relationships)
│   ├── User.java
│   ├── Role.java
│   ├── Event.java
│   ├── TicketTier.java
│   ├── Booking.java
│   └── TicketPass.java
├── exception/           # Custom exception handling classes
│   ├── BadRequestException.java
│   └── ResourceNotFoundException.java
├── repository/          # Spring Data JPA persistence interfaces
│   ├── UserRepository.java
│   ├── RoleRepository.java
│   ├── EventRepository.java
│   ├── TicketTierRepository.java
│   ├── BookingRepository.java
│   └── TicketPassRepository.java
├── security/            # JWT filters and UserDetails services
│   ├── CustomUserDetailsService.java
│   ├── JwtAuthenticationFilter.java
│   └── JwtUtils.java
└── service/             # Core business logic & transactional engines
    ├── AuthService.java
    ├── EventService.java
    └── ScanService.java