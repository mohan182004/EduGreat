# EduGreat — Course Platform Backend API

A **backend-only** REST API service for a learning platform where users can browse courses, search content, enroll in courses, and track their learning progress by marking subtopics as completed.

> **Live Deployment:** [https://edugreat-production.up.railway.app/swagger-ui/index.html](https://edugreat-production.up.railway.app/swagger-ui/index.html#/)

---

## What It Is

EduGreat is a backend service built with **Spring Boot** and **PostgreSQL** that powers a course-based learning platform. There is no frontend — the entire API is accessible and testable through the **Swagger UI** interface deployed publicly on Railway.

The service handles:
- **Public course browsing** — anyone can view available courses with full topic/subtopic details
- **Full-text search** — search across course titles, descriptions, topic names, subtopic names, and content
- **User authentication** — register and login with JWT-based stateless auth
- **Course enrollment** — authenticated users can enroll in courses
- **Progress tracking** — mark individual subtopics as completed and view completion percentage

---

## What It Does

### Public Endpoints (No Authentication Required)

| Method | Endpoint | Description |
|--------|----------|-------------|
| `GET` | `/api/courses` | List all courses with topic and subtopic counts |
| `GET` | `/api/courses/{courseId}` | Full course detail — topics, subtopics, and markdown content |
| `GET` | `/api/search?q={query}` | Search across all course content (case-insensitive partial match) |
| `POST` | `/api/auth/register` | Register a new user with email and password |
| `POST` | `/api/auth/login` | Login and receive a JWT token |

### Authenticated Endpoints (JWT Required)

| Method | Endpoint | Description |
|--------|----------|-------------|
| `POST` | `/api/courses/{courseId}/enroll` | Enroll in a course |
| `POST` | `/api/subtopics/{subtopicId}/complete` | Mark a subtopic as completed (idempotent) |
| `GET` | `/api/enrollments/{enrollmentId}/progress` | View progress — completed subtopics and completion % |

### Authentication Flow
1. **Register** → `POST /api/auth/register` with email and password
2. **Login** → `POST /api/auth/login` — returns a JWT token
3. **Use Token** → Include `Authorization: Bearer <token>` header in authenticated requests
4. In Swagger UI: Click **Authorize** → paste `Bearer <your-token>` → all authenticated endpoints become accessible

---

## How It Does It

### Architecture & Approach

The project follows a **layered architecture** with clear separation of concerns:

```
Controller → Service → Repository → Database
     ↓           ↓          ↓
    DTOs    Business    JPA/Hibernate
             Logic
```

- **Controllers** handle HTTP request/response mapping and input validation
- **Services** contain all business logic (enrollment checks, progress calculation, search)
- **Repositories** use Spring Data JPA with custom JPQL queries for search
- **Entities** map directly to PostgreSQL tables with proper relationships and constraints

### Key Design Decisions

1. **Stateless JWT Authentication** — No server-side sessions. Every request is authenticated via a JWT token in the `Authorization` header. The token contains the user's email and is validated on each request through a custom `JwtAuthenticationFilter`.

2. **Profile-Based Configuration** — The app supports two Spring profiles:
   - `h2` — In-memory H2 database for local development (zero setup)
   - `postgres` — PostgreSQL for production, configured entirely via environment variables

3. **Seed Data on Startup** — A `DataSeeder` component runs on application start and populates the database with 3 courses (Physics, Math, Computer Science), each containing 3 topics with 3 subtopics (27 subtopics total). Subtopic content is rich Markdown text. Data is loaded from a `courses.json` file bundled in resources.

4. **Search Implementation** — Search is implemented using a JPQL query that performs case-insensitive partial matching (`LIKE %query%`) across course titles, descriptions, topic titles, subtopic titles, and subtopic content. Results are grouped by course with match context.

5. **Idempotent Progress Tracking** — Marking a subtopic as complete is idempotent — calling it multiple times won't create duplicate records. Progress is calculated as `(completedSubtopics / totalSubtopics) * 100`.

6. **Global Exception Handling** — A `@ControllerAdvice` class catches all custom exceptions (`NotFoundException`, `ConflictException`, `UnauthorizedException`, `ForbiddenException`) and returns consistent JSON error responses.

### Domain Model

```
Course (1) ──── (N) Topic (1) ──── (N) Subtopic
                                          │
User (1) ──── (N) Enrollment              │
   │              (user + course)         │
   │                                      │
   └──── (N) SubtopicProgress ────────────┘
              (user + subtopic, completed, completedAt)
```

- A **Course** has many **Topics**, each with many **Subtopics**
- A **User** can **enroll** in multiple courses (unique constraint: one enrollment per user per course)
- A **User** tracks progress by marking **Subtopics** as completed (unique constraint: one progress record per user per subtopic)

---

## Tech Stack

| Technology | Purpose |
|------------|---------|
| **Java 17+** | Language |
| **Spring Boot 3.2.2** | Application framework |
| **Spring Security** | Authentication & authorization |
| **JWT (jjwt 0.12.3)** | Stateless token-based auth |
| **Spring Data JPA / Hibernate** | ORM & database access |
| **PostgreSQL** | Production database |
| **H2** | Local development database |
| **springdoc-openapi 2.3.0** | Swagger UI & OpenAPI docs |
| **Maven** | Build tool |
| **Railway** | Cloud deployment |

---

## Project Structure

```
src/main/java/com/courseplatform/
├── CoursePlatformApplication.java       # Entry point
├── config/
│   ├── SecurityConfig.java              # CORS, CSRF, endpoint permissions, JWT filter
│   ├── OpenApiConfig.java               # Swagger UI configuration
│   └── DataSeeder.java                  # Loads courses.json into DB on startup
├── controller/
│   ├── AuthController.java              # Register & Login
│   ├── CourseController.java            # Browse courses & Enroll
│   ├── SearchController.java            # Search endpoint
│   ├── SubtopicController.java          # Mark subtopics complete
│   └── EnrollmentController.java        # View enrollment progress
├── service/
│   ├── AuthService.java                 # Auth business logic
│   ├── CourseService.java               # Course & search logic
│   └── EnrollmentService.java           # Enrollment & progress logic
├── entity/
│   ├── Course.java, Topic.java, Subtopic.java
│   ├── User.java, Enrollment.java, SubtopicProgress.java
├── dto/                                 # 11 request/response DTOs
├── repository/                          # 6 Spring Data JPA repositories
├── security/
│   ├── JwtTokenProvider.java            # Token generation & validation
│   ├── JwtAuthenticationFilter.java     # Per-request token filter
│   └── JwtAuthEntryPoint.java           # 401 error handler
└── exception/
    ├── GlobalExceptionHandler.java      # Centralized error handling
    ├── NotFoundException.java
    ├── ConflictException.java
    ├── UnauthorizedException.java
    └── ForbiddenException.java

src/main/resources/
├── application.properties               # Common config (active profile, JPA)
├── application-postgres.properties      # PostgreSQL + env var config
├── application-h2.properties            # H2 local dev config
└── courses.json                         # Seed data (3 courses, 9 topics, 27 subtopics)
```

---

## Running Locally

### Prerequisites
- Java 17+ installed
- Maven (or use the included Maven wrapper `mvnw`)

### With H2 (no database setup needed)
```bash
# Set active profile to h2 in application.properties
# spring.profiles.active=h2

./mvnw clean package -DskipTests
java -jar target/course-platform-0.0.1-SNAPSHOT.jar
```

### With PostgreSQL
```bash
# Create a PostgreSQL database named 'courseplatform'
# Set environment variables or use defaults in application-postgres.properties

export DATABASE_URL=jdbc:postgresql://localhost:5432/courseplatform
export DB_USERNAME=postgres
export DB_PASSWORD=yourpassword
export JWT_SECRET=your-base64-encoded-secret

./mvnw clean package -DskipTests
java -jar target/course-platform-0.0.1-SNAPSHOT.jar
```

Then open: [http://localhost:8080/swagger-ui.html](http://localhost:8080/swagger-ui.html)

---

## Seed Data

The application comes pre-loaded with 3 courses:

| Course | Topics | Subtopics |
|--------|--------|-----------|
| Introduction to Physics | Kinematics, Dynamics, Energy | 9 subtopics with Markdown content |
| Mathematics Fundamentals | Algebra, Geometry, Calculus | 9 subtopics with Markdown content |
| Computer Science Basics | Programming, Data Structures, Algorithms | 9 subtopics with Markdown content |

Each subtopic contains detailed Markdown content including headings, formulas, bullet points, and code examples.

---

## Deployment

The application is deployed on **Railway** with:
- PostgreSQL database provisioned via Railway's PostgreSQL plugin
- Environment variables for database credentials and JWT configuration
- `server.forward-headers-strategy=framework` to handle HTTPS behind Railway's reverse proxy
- CORS enabled for all origins to allow Swagger UI API testing

**Live URL:** [https://edugreat-production.up.railway.app/swagger-ui/index.html](https://edugreat-production.up.railway.app/swagger-ui/index.html#/)
