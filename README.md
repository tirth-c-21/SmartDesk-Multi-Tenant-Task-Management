# SmartDesk — Multi-Tenant Task Management API

![CI/CD](https://github.com/tirth-c-21/SmartDesk-Multi-Tenant-Task-Management/actions/workflows/deploy.yml/badge.svg)

A production-grade backend REST API for managing tasks across teams with role-based access control, JWT authentication, and multi-tenant workspace isolation.

---

## 🧩 What Problem Does It Solve?

Small teams, startups, and agencies need a lightweight way to manage tasks with proper access control — without the complexity and cost of tools like Jira. SmartDesk provides a clean backend API to handle exactly this: multiple organizations, role-based permissions, task lifecycle tracking, and automated notifications.

---

## ⚙️ Tech Stack

|------------------|-------------------------------------|
| Layer            | Technology 			 |
|------------------|-------------------------------------|
| Language  	   | Java 17                             |
| Framework	   | Spring Boot 3                       |
| Security  	   | Spring Security + JWT (JJWT 0.12.7) |
| ORM       	   | Spring Data JPA + Hibernate         |
| Database     	   | PostgreSQL                          |
| Containerization | Docker + Docker Compose             |
| CI/CD 	   | GitHub Actions                      |
| Cloud 	   | AWS EC2                             |
| Build Tool 	   | Maven                               |
|------------------|-------------------------------------|

---

## 🏗️ Architecture Overview

```
Client (Postman / Frontend)
           │
           ▼
    ┌─────────────────┐
    │  Spring Boot API │
    │                 │
    │  JWT Auth Filter│  ← validates token on every request
    │  Security Config│  ← role-based access control
    │  Controllers    │  ← receives and returns HTTP requests
    │  Services       │  ← all business logic lives here
    │  Repositories   │  ← database operations via JPA
    └────────┬────────┘
             │
      ┌──────▼──────┐
      │  PostgreSQL  │
      │             │
      │  users       │
      │  workspaces  │
      │  tasks       │
      │  audit_logs  │
      └─────────────┘
```

---

## 🔐 Features

### Authentication & Authorization
- User registration with BCrypt password encoding
- JWT-based stateless authentication
- Role-based access control: `ADMIN` / `MANAGER` / `MEMBER`
- Custom Access Denied handler for 403 responses

### Workspace (Multi-Tenancy) 
- Each organisation gets an isolated workspace
- Users belong to one workspace and cannot access data from another
- ADMIN creates workspaces and manages members

### Task Management 
- Full task lifecycle: `TODO → IN_PROGRESS → DONE`
- Task assignment, priority levels, and deadline tracking
- Overdue task detection

### Notifications 
- Email alerts on task assignment via JavaMailSender
- Daily digest of overdue tasks using Spring `@Scheduled`

### Audit Log 
- Every task status change is recorded with timestamp and actor

## 🌐 Live Demo
Base URL: `http://13.211.64.93:2104`
> Note: EC2 may be stopped to save costs.

## 🐳 Running with Docker
docker-compose up --build

---

## 📁 Package Structure

```
com.demoproject.SmartDesk
├── authcontroller/       ← /signin, /register endpoints
├── config/               ← Security and Scheduler configuration
├── DTO/                  ← Request objects (UserLoginForm, UserRegisterForm)
├── model/                ← JPA Entities (SmartDeskUser, Workspace, Task)
├── repo/                 ← Spring Data JPA Repositories
├── responses/            ← Response objects (LoginResponse, ErrorResponse)
├── role/                 ← Role enum (ADMIN, MANAGER, MEMBER)
├── security/             ← JWT utilities and Auth filter
└── service/              ← Business logic (UserService, CustomUserDetailsService)
```

---

## 🚀 Getting Started

### Prerequisites
- Java 17+
- Maven 3.8+
- PostgreSQL
- Docker (optional)

### 1. Clone the repository
```bash
git clone https://github.com/your-username/SmartDesk.git
cd SmartDesk
```

### 2. Configure the database

Create a PostgreSQL database named `SmartDeskDatabase`, then update `application.properties`:

```properties
spring.datasource.url=jdbc:postgresql://localhost:5432/SmartDeskDatabase
spring.datasource.username=your_db_username
spring.datasource.password=your_db_password

jwt.secret=your_secret_key_here
jwt.expiration=3600000
```

> ⚠️ Never commit `application.properties` with real credentials. It is listed in `.gitignore`.

### 3. Run the application
```bash
mvn spring-boot:run
```

App runs on `http://localhost:2104`

---

## 📬 API Endpoints


|Method|        Endpoint           |      Access    |        Description          |
|------|---------------------------|----------------|-----------------------------|
|      |			   |		    |				  |
| POST | `/public/register`        | Public         | Register user               |
| POST | `/public/signin`          | Public         | Login and receive JWT token |
| POST | `/workspace/create`       | ADMIN          | Create workspace            |
| POST | `/workspace/add-user`     | ADMIN/MANAGER  | Add user to workspace       |
| POST | `/task/create`            | ADMIN/MANAGER  | Create task                 |
| PUT  | `/task/assign`            | ADMIN/MANAGER  | Assign task to member       |
| PUT  | `/task/status`            | ALL roles      | Update task status          |
| GET  | `/task/{id}/audit`        | ADMIN/MANAGER  | Get audit history           |

### Request Examples

**Register**
```json
POST /register
{
  "username": "tirthendu",
  "email": "tirth@gmail.com",
  "password": "pass@123",
  "role": "MEMBER"
}
```
> Role must be one of: `ADMIN`, `MANAGER`, `MEMBER`

**Sign In**
```json
POST /signin
{
  "username": "tirthendu",
  "password": "pass@123"
}
```

**Sign In Response**
```json
{
  "username": "tirthendu",
  "roles": "[ROLE_MEMBER]",
  "postLoginmessage": "LOGIN SUCCESS!!",
  "token": "eyJhbGciOiJIUzI1NiJ9..."
}
```

### Accessing Protected Endpoints
Pass the token in the `Authorization` header:
```
Authorization: Bearer <your_token_here>
```

---

## 🐳 Running with Docker 

```bash
docker-compose up --build
```

---

## 🎯 About This Project
Built as a personal project to demonstrate real-world backend engineering skills — 
multi-tenancy, JWT security, role-based access control, audit logging, 
email notifications, Docker containerization, CI/CD and AWS EC2 deployment.

---

## 👨‍💻 Author

**Tirthendu Chakraborty**  
Senior Systems Engineer @Infosys | Java Backend Developer  
📧 tirthenduchakraborty2104@gmail.com  
🔗 [LinkedIn](https://www.linkedin.com/in/tirthendu-chakraborty-584462223/)
