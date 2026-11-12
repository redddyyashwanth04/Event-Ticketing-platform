# 🎫 Event Booking & Ticketing Platform (Backend)

An enterprise-grade, secure, and high-concurrency Event Booking and Ticketing backend built with **Spring Boot** and **Spring Security (JWT)**. This platform supports multi-tier event pricing, atomic inventory management, stateless authentication, role-based access control, and a digital gate-scanning pass validation system.

---

## 🚀 Key Features

* **Stateless JWT Authentication:** Secure token-based authentication with custom filters (`OncePerRequestFilter`), ensuring zero server-side session overhead.
* **Role-Based Access Control (RBAC):** Fine-grained method-level security using `@PreAuthorize` supporting three distinct roles: `ROLE_ATTENDEE`, `ROLE_ORGANIZER`, and `ROLE_SCANNER`.
* **Multi-Tier Event Management:** Organizers can create events with dynamic ticket pricing structures (e.g., VIP, General Admission) and stock capacities.
* **Atomic & Concurrency-Safe Bookings:** Transactional inventory management that prevents overselling during high-traffic booking windows.
* **Digital Ticket Pass Generation:** Automatically generates unique, scannable UUID-based QR pass tokens for every purchased ticket.
* **Gate-Pass Validation & Anti-Fraud:** Staff scanning endpoints that verify entry passes and prevent double-entry / ticket reuse frauds.

---

## 🛠️ Tech Stack

* **Language:** Java 17
* **Framework:** Spring Boot 3+
* **Security:** Spring Security, JJWT (Java JSON Web Token), BCrypt Password Hashing
* **Persistence & Database:** Spring Data JPA, Hibernate, MySQL
* **Utilities & Validation:** Lombok, Jakarta Validation (`jakarta.validation`)
* **Build Tool:** Maven

---

## 📁 Project Directory Structure

```text
src/
└── main/
    ├── java/
    │   └── com/example/Ticketing_project/
    │       ├── config/         # Spring Security & App configuration
    │       ├── controller/     # REST Controllers (Auth, Events, Bookings, Scanner)
    │       ├── dto/            # Data Transfer Objects & Validation models
    │       ├── entity/         # JPA Database Entities (User, Role, Event, TicketTier, Booking, TicketPass)
    │       ├── exception/      # Custom exception handlers & global error management
    │       ├── repository/     # Spring Data JPA Repositories
    │       ├── security/       # JWT Utils, Filters, and CustomUserDetailsService
    │       └── service/        # Core Business Logic Services
    └── resources/
        └── application.properties