# HR Management System - Claude Code Instructions

## Project Overview

This is a Spring Boot 4.0.0 HR Management System built with Java 17. The application manages employees, departments, positions, and payroll processing.

## Technology Stack

- **Framework**: Spring Boot 4.0.0
- **Language**: Java 17
- **Database**: PostgreSQL (runtime), H2 (testing)
- **ORM**: Spring Data JPA
- **Security**: Spring Security
- **Build Tool**: Maven
- **Key Libraries**: Lombok, Spring Validation

## Project Structure

```
src/main/java/com/hr_management_system/
├── config/          # Application configuration
├── department/      # Department management
├── employee/        # Employee management
├── payroll/         # Payroll processing
└── position/        # Position/role management
```

## Code Organization Patterns

### Follow Domain-Driven Design
Each domain module should contain:
- **Entity**: JPA entity with `@Entity` annotation
- **Repository**: Spring Data JPA repository extending `JpaRepository`
- **Service**: Business logic with `@Service` annotation
- **Controller**: REST endpoints with `@RestController` annotation
- **DTOs**: Request/response data transfer objects (if needed)

### Naming Conventions
- **Entities**: Singular nouns (e.g., `Employee`, `Department`)
- **Repositories**: `{Entity}Repository` (e.g., `EmployeeRepository`)
- **Services**: `{Entity}Service` (e.g., `EmployeeService`)
- **Controllers**: `{Entity}Controller` (e.g., `EmployeeController`)

## Development Guidelines

### Code Style
- Use Lombok annotations (`@Data`, `@Builder`, `@NoArgsConstructor`, `@AllArgsConstructor`) to reduce boilerplate
- Keep controllers thin - delegate business logic to services
- Use Spring Validation annotations (`@Valid`, `@NotNull`, etc.) for input validation
- Follow RESTful conventions for API endpoints

### Database
- PostgreSQL is the production database
- H2 is used for testing
- Use JPA annotations for entity relationships (`@OneToMany`, `@ManyToOne`, etc.)
- Apply proper cascade types and fetch strategies

### Security
- Spring Security is configured - consider authentication/authorization when adding endpoints
- Be mindful of sensitive data (passwords, payroll information)

### Testing
- Write unit tests for services
- Write integration tests for controllers
- Use H2 in-memory database for test scenarios

## Current Development Status

Based on recent commits:
- Payroll entity, repository, service, and controller are in progress
- Payment methods and payroll status enums are defined
- Core employee, department, and position modules exist

## Working with this Codebase

### When adding new features:
1. Read existing code to understand patterns
2. Follow the established structure (Entity → Repository → Service → Controller)
3. Use Lombok to minimize boilerplate
4. Add proper validation and error handling
5. Consider security implications

### When modifying existing code:
1. Understand the full context by reading related files
2. Maintain consistency with existing patterns
3. Update tests if behavior changes
4. Don't break existing functionality

### Git Workflow
- Create descriptive commit messages
- Stage specific files rather than using `git add .`
- Test changes before committing
- Ask before force-pushing or destructive operations/

## Common Commands

```bash
# Build the project
mvn clean install

# Run the application
mvn spring-boot:run

# Run tests
mvn test

# Package the application
mvn package
```

## Notes

- Always ensure database configurations are properly set in `application.properties` or `application.yml`
- When working with payroll, be extra careful with financial calculations and data integrity
- Consider transaction management for operations that modify multiple entities
