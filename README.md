# HR Management System

A RESTful API for managing employees, departments, positions, and payroll, built with Spring Boot 4.0 and Java 17.

## Tech Stack

- **Spring Boot 4.0** (Web MVC, Data JPA, Security, Validation, Actuator)
- **Java 17**
- **PostgreSQL** (production) / **H2** (development & testing)
- **Lombok** for boilerplate reduction
- **SpringDoc OpenAPI** for API documentation
- **Maven** build tool

## Getting Started

### Prerequisites

- Java 17+
- Maven 3.9+
- PostgreSQL (for production use)

### Run with H2 (default)

```bash
mvn spring-boot:run
```

The app starts on `http://localhost:8080` with an in-memory H2 database.

### Run with PostgreSQL

Configure your database in `application.properties` or via environment variables, then:

```bash
mvn spring-boot:run
```

### Build & Test

```bash
mvn clean install   # build + run all tests
mvn test            # run tests only
mvn package         # package as JAR
```

## Authentication

All `/api/v1/**` endpoints require HTTP Basic authentication.

| Default Credential | Value | Environment Variable |
|--------------------|-------|----------------------|
| Username | `admin` | `ADMIN_USERNAME` |
| Password | `password123` | `ADMIN_PASSWORD` |

## API Endpoints

Base path: `/api/v1`

### Employees `/employees`

| Method | Path | Description |
|--------|------|-------------|
| POST | `/employees` | Create employee |
| GET | `/employees/{id}` | Get employee by ID |
| GET | `/employees` | List employees (paginated) |
| PUT | `/employees/{id}` | Update employee |
| DELETE | `/employees/{id}` | Delete employee |

### Departments `/departments`

| Method | Path | Description |
|--------|------|-------------|
| POST | `/departments` | Create department |
| GET | `/departments/{id}` | Get department by ID |
| GET | `/departments` | List departments (paginated) |
| PUT | `/departments/{id}` | Update department |
| DELETE | `/departments/{id}` | Delete department |

### Positions `/positions`

| Method | Path | Description |
|--------|------|-------------|
| POST | `/positions` | Create position |
| GET | `/positions/{id}` | Get position by ID |
| GET | `/positions` | List positions (paginated) |
| PUT | `/positions/{id}` | Update position |
| DELETE | `/positions/{id}` | Delete position |

### Payroll `/payroll`

| Method | Path | Description |
|--------|------|-------------|
| POST | `/payroll` | Create payroll record |
| GET | `/payroll/{id}` | Get payroll by ID |
| GET | `/payroll` | List payrolls (paginated) |
| PUT | `/payroll/{id}` | Update payroll |
| DELETE | `/payroll/{id}` | Delete payroll |
| GET | `/payroll/employee/{employeeId}` | Get payrolls by employee |
| GET | `/payroll/status/{status}` | Get payrolls by status |

All list endpoints support pagination via `page` and `size` query parameters (default size: 20).

## Domain Model

### Employee
- Personal info (name, email, phone, date of birth)
- Assigned to one **Position** (many-to-one)
- Belongs to multiple **Departments** (many-to-many)
- Roles: `EMPLOYEE`, `MANAGER`, `TEAM_LEADER`
- Salary validated against position's min/max range

### Department
- Types: `HR`, `FINANCE`, `ENGINEERING`, `MARKETING`, `SALES`, `CUSTOMER_SUPPORT`, `LEGAL`, `OPERATIONS`
- Has a department head (an employee)
- Contains multiple employees

### Position
- Title with min/max salary range

### Payroll
- Linked to an employee
- **Gross salary** auto-calculated: `baseSalary + overtimePay + bonuses + commissions`
- **Net salary** auto-calculated: `grossSalary - incomeTax - socialSecurity - healthInsurance - pensionContribution`
- Payment methods: `DEPOSIT`, `CHECK`, `CASH`, `PAYPAL`, `WIRE`, `OTHER`
- Statuses: `PENDING`, `ONGOING`, `COMPLETE`, `FAILED`

## API Documentation

Swagger UI is available at `http://localhost:8080/swagger-ui.html` (no authentication required).

## Additional Endpoints

| Path | Description | Auth Required |
|------|-------------|---------------|
| `/h2-console` | H2 database console | No |
| `/swagger-ui.html` | Swagger UI | No |
| `/v3/api-docs` | OpenAPI JSON spec | No |
| `/actuator/health` | Health check | No |
| `/actuator/info` | App info | No |
| `/actuator/metrics` | Metrics | No |

## Configuration

### CORS

Allowed origins default to `http://localhost:3000`. Override with the `ALLOWED_ORIGINS` environment variable.

### Environment Variables

| Variable | Description | Default |
|----------|-------------|---------|
| `ADMIN_USERNAME` | Auth username | `admin` |
| `ADMIN_PASSWORD` | Auth password | `password123` |
| `ALLOWED_ORIGINS` | CORS allowed origins | `http://localhost:3000` |

## Project Structure

```
src/main/java/com/hr_management_system/
├── config/             # Security, CORS, OpenAPI, auditing, exception handling
├── employee/           # Employee entity, repo, service, controller, DTOs
├── department/         # Department entity, repo, service, controller, DTOs
├── position/           # Position entity, repo, service, controller, DTOs
└── payroll/            # Payroll entity, repo, service, controller, DTOs
```
