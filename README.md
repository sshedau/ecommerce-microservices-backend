# 🛒 E-Commerce Microservices

<p align="center">
  <b>A production-oriented e-commerce backend built with Spring Boot Microservices</b>
</p>

<p align="center">
  <img src="https://img.shields.io/badge/Java-17-orange?style=for-the-badge&logo=openjdk" alt="Java 17"/>
  <img src="https://img.shields.io/badge/Spring%20Boot-4.x-brightgreen?style=for-the-badge&logo=springboot" alt="Spring Boot"/>
  <img src="https://img.shields.io/badge/Spring%20Cloud-Gateway-blue?style=for-the-badge&logo=spring" alt="Spring Cloud Gateway"/>
  <img src="https://img.shields.io/badge/PostgreSQL-16-336791?style=for-the-badge&logo=postgresql" alt="PostgreSQL"/>
  <img src="https://img.shields.io/badge/Redis-Cache-red?style=for-the-badge&logo=redis" alt="Redis"/>
  <img src="https://img.shields.io/badge/Docker-Compose-2496ED?style=for-the-badge&logo=docker" alt="Docker"/>
  <img src="https://img.shields.io/badge/JWT-Authentication-black?style=for-the-badge&logo=jsonwebtokens" alt="JWT"/>
</p>

---

## 📌 Overview

This project is a backend **E-Commerce application** designed using a **microservices architecture**.

It is a re-architecture of an earlier monolithic e-commerce application, where major business responsibilities were separated into independently deployable services.

The project demonstrates practical implementation of:

- 🧩 Microservice decomposition
- 🚪 API Gateway pattern
- 🔐 JWT-based authentication
- 🗄️ Database-per-service architecture
- ⚡ Redis integration
- 🐳 Docker containerization
- 🔄 Inter-service communication
- ⚙️ Environment-based configuration
- 📦 Maven-based Spring Boot services

---

## 🏗️ Architecture

```text
                              ┌───────────────┐
                              │    CLIENT     │
                              └───────┬───────┘
                                      │
                                      ▼
                         ┌──────────────────────┐
                         │     API GATEWAY      │
                         │        :8080         │
                         └──────────┬───────────┘
                                    │
                    ┌───────────────┼───────────────┐
                    │               │               │
                    ▼               ▼               ▼
           ┌──────────────┐ ┌──────────────┐ ┌──────────────┐
           │ USER SERVICE │ │   PRODUCT    │ │    ORDER     │
           │    :8081     │ │   SERVICE    │ │   SERVICE    │
           │              │ │    :8082     │ │    :8083     │
           └──────┬───────┘ └──────┬───────┘ └──────┬───────┘
                  │                │                │
                  ▼                ▼                ▼
           ┌──────────────┐ ┌──────────────┐ ┌──────────────┐
           │    USER DB   │ │  PRODUCT DB  │ │   ORDER DB   │
           │  PostgreSQL  │ │  PostgreSQL  │ │  PostgreSQL  │
           └──────────────┘ └──────────────┘ └──────────────┘
                  │                │                │
                  └────────────────┼────────────────┘
                                   │
                                   ▼
                              ┌──────────┐
                              │  REDIS   │
                              │  :6379   │
                              └──────────┘
```

---

## 🔹 Service Responsibilities

| Service | Port | Responsibility |
|---|---|---|
| 🚪 API Gateway | 8080 | Single entry point and request routing |
| 👤 User Service | 8081 | User management and authentication |
| 📦 Product Service | 8082 | Product management |
| 🛍️ Order Service | 8083 | Order management and order-related operations |
| 🗄️ User PostgreSQL | 5433 | User-service persistence |
| 🗄️ Product PostgreSQL | 5434 | Product-service persistence |
| 🗄️ Order PostgreSQL | 5435 | Order-service persistence |
| ⚡ Redis | 6379 | Caching / Redis-backed data |

---

## 🛠️ Technology Stack

| Technology | Purpose |
|---|---|
| ☕ Java 17 | Application runtime |
| 🌱 Spring Boot | Microservice development |
| 🚪 Spring Cloud Gateway | API Gateway and request routing |
| 🗄️ Spring Data JPA | Database access |
| 🔄 Hibernate | ORM |
| 🔐 Spring Security | Application security |
| 🎟️ JWT | Authentication and authorization |
| 🐘 PostgreSQL 16 | Persistent storage |
| ⚡ Redis | Caching / fast data access |
| 📦 Maven | Build and dependency management |
| 🐳 Docker | Containerization |
| 🧩 Docker Compose | Multi-container orchestration |

---

## 📁 Project Structure

```text
ecom_microservices/
│
├── api-gateway/
│   ├── src/
│   ├── Dockerfile
│   └── pom.xml
│
├── user-service/
│   ├── src/
│   ├── Dockerfile
│   └── pom.xml
│
├── product-service/
│   ├── src/
│   ├── Dockerfile
│   └── pom.xml
│
├── order-service/
│   ├── src/
│   ├── Dockerfile
│   └── pom.xml
│
├── docker-compose.yml
├── .env.example
├── .gitignore
└── README.md
```

---

## 🚪 API Gateway

The API Gateway runs on port 8080 and acts as the single entry point for client requests.

### Current Routes

| Route | Target Service |
|---|---|
| `/api/v1/auth/**` | User Service |
| `/api/v1/users/**` | User Service |
| `/api/v1/products/**` | Product Service |
| `/api/v1/orders/**` | Order Service |

### Example Request Flow

```text
Client
  │
  │ GET /api/v1/products/...
  ▼
API Gateway :8080
  │
  ▼
Product Service :8082
```

The gateway hides internal service locations from clients and provides a central point for routing and future cross-cutting concerns.

---

## 🔐 Authentication & JWT

JWT-based authentication is currently implemented in the backend services.

### Current Authentication Flow

```text
┌────────┐
│  User  │
└───┬────┘
    │
    │ Login
    ▼
┌────────────────┐
│  User Service  │
└───────┬────────┘
        │
        │ JWT
        ▼
┌──────────────┐
│    Client    │
└──────┬───────┘
       │
       │ Authorization: Bearer <token>
       ▼
┌────────────────────┐
│   Protected APIs   │
└────────────────────┘
```

### JWT Claims

JWT tokens contain claims such as:

- User ID
- User role
- Subject / email
- Issued-at time
- Expiration time

### Planned Improvement

JWT validation can later be centralized at the API Gateway, allowing the gateway to authenticate requests before forwarding them to downstream services.

---

## 🗄️ Database Architecture

The project follows the **Database-per-Service** pattern.

Each core service owns its own PostgreSQL database.

```text
User Service
     │
     ▼
  ┌─────────┐
  │ user_db │
  └─────────┘


Product Service
     │
     ▼
┌─────────────┐
│ product_db  │
└─────────────┘


Order Service
     │
     ▼
 ┌──────────┐
 │ order_db │
 └──────────┘
```

This provides logical data isolation and allows each service to evolve its persistence layer independently.

---

## ⚡ Redis

Redis is integrated as a fast caching/data layer where required.

Inside Docker Compose, services communicate with Redis through:

```text
redis:6379
```

### Docker Networking

When applications run inside Docker, use the Docker service name rather than `localhost` for container-to-container communication.

For example:

**Correct:**

```text
redis:6379
```

**Incorrect inside Docker:**

```text
localhost:6379
```

The same principle applies to PostgreSQL services:

```text
user-postgres:5432
product-postgres:5432
order-postgres:5432
```

---

## 🐳 Running with Docker

### 1. Clone the Repository

```bash
git clone <your-repository-url>
cd ecom_microservices
```

### 2. Configure Environment Variables

Create a local `.env` file in the root directory:

```env
DB_USERNAME=your_database_username
DB_PASSWORD=your_database_password
JWT_SECRET=your_jwt_secret
```

A `.env.example` file should be included as a template:

```env
DB_USERNAME=
DB_PASSWORD=
JWT_SECRET=
```

### 3. Build the Containers

```bash
docker compose build
```

### 4. Start the Application

```bash
docker compose up -d
```

### 5. Check Container Status

```bash
docker compose ps
```

### 6. View Service Logs

```bash
docker compose logs --tail=200 user-service
docker compose logs --tail=200 product-service
docker compose logs --tail=200 order-service
docker compose logs --tail=200 api-gateway
```

### 7. Stop the Application

```bash
docker compose down
```

### 8. Rebuild After Code Changes

```bash
docker compose build --no-cache
docker compose up -d
```

---

## 💻 Running Without Docker

Each service can also be run independently using Maven.

From an individual service directory:

```bash
mvn spring-boot:run
```

Or build a JAR:

```bash
mvn clean package
```

Then run:

```bash
java -jar target/<application>.jar
```

For local execution, PostgreSQL and Redis must be available and the service configuration must point to the appropriate hosts and ports.

---

## 🧪 Testing the Gateway

After starting the containers, requests can be sent through the API Gateway.

Example:

```bash
curl http://localhost:8080/api/v1/users/hello
curl http://localhost:8080/api/v1/products/hello
curl http://localhost:8080/api/v1/orders/hello
```

The exact available endpoints depend on the controllers implemented in each service.

---

## 🔄 Microservice Communication

The services communicate over the Docker network using service names rather than container IP addresses.

For example:

```text
                    ┌────────────────┐
                    │  Order Service │
                    └───────┬────────┘
                            │
                  ┌─────────┴─────────┐
                  │                   │
                  ▼                   ▼
         ┌────────────────┐   ┌─────────────────┐
         │  User Service  │   │ Product Service │
         └────────────────┘   └─────────────────┘
```

Docker Compose provides a shared network:

```text
ecommerce-network
```

This allows services to discover each other using their Docker Compose service names.

---

## 🎯 Why Microservices?

The original application was based on a monolithic architecture.

It was redesigned into independent services to demonstrate:

- ✅ Separation of business responsibilities
- ✅ Independent service deployment
- ✅ Database ownership per service
- ✅ API Gateway pattern
- ✅ Docker containerization
- ✅ Inter-service communication
- ✅ Redis integration
- ✅ JWT authentication
- ✅ Environment-based configuration
- ✅ Independent scalability of services

---

## 🚀 Future Improvements

Planned improvements include:

- 🔐 Centralized JWT validation in API Gateway
- 🔒 Service-to-service authentication
- 📚 OpenAPI / Swagger documentation
- 📋 Centralized logging
- ❤️ Health checks and monitoring
- 🧪 Unit and integration testing
- ⚙️ CI/CD with GitHub Actions
- 🔒 HTTPS and production reverse proxy
- 🔄 Retries, timeouts, and circuit breakers
- ☁️ Cloud deployment
- 🔧 Production-grade configuration management

---

## 🧠 Learning Goals

This project provides hands-on experience with:

```text
Spring Boot
     │
     ▼
Spring Cloud Gateway
     │
     ▼
Microservices
     │
     ├─────────────────┐
     │                 │
     ▼                 ▼
PostgreSQL           Redis
     │
     ▼
Spring Data JPA
     │
     ▼
JWT + Spring Security
     │
     ▼
Docker + Docker Compose
     │
     ▼
Cloud Deployment
```

---

## 📈 Architecture Evolution

This project represents the transition from a monolithic architecture to a microservices architecture.

```text
                    BEFORE

              ┌─────────────────┐
              │    Monolith     │
              │                 │
              │ User            │
              │ Product         │
              │ Order           │
              │ Authentication  │
              └────────┬────────┘
                       │
                       ▼
                 Single Database
```

```text
                    AFTER

                       Client
                         │
                         ▼
                  ┌─────────────┐
                  │ API Gateway │
                  └──────┬──────┘
                         │
          ┌──────────────┼──────────────┐
          │              │              │
          ▼              ▼              ▼
     User Service   Product Service  Order Service
          │              │              │
          ▼              ▼              ▼
       user_db        product_db       order_db
          │              │              │
          └──────────────┼──────────────┘
                         │
                         ▼
                       Redis
```

This architecture provides clearer service boundaries, independent persistence, and the ability to evolve and deploy individual services separately.

---

## 🌐 Deployment

The backend is deployed on Render using a microservices architecture.

### API Gateway
[https://api-gateway-nvvj.onrender.com](https://api-gateway-nvvj.onrender.com)

### Microservices

| Service | Deployment |
|---|---|
| User Service | [Render URL](https://user-service-evgy.onrender.com) |
| Product Service | [Render URL](https://product-service-a26j.onrender.com) |
| Order Service | [Render URL](https://order-service-d13w.onrender.com) |

### Infrastructure

- **PostgreSQL:** Neon
- **Redis:** Upstash
- **Deployment:** Render
- **Containerization:** Docker

## 👨‍💻 Author

**Sujal Sunil Hedau**

E-Commerce Microservices Backend

A practical microservices-based e-commerce backend developed as a progression from a monolithic architecture to a distributed system.

<p align="center">
  ⭐ If you find this project useful, consider giving the repository a star.
</p>
