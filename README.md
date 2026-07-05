# Employee Management System

![Java](https://img.shields.io/badge/Java-17-orange?logo=openjdk&logoColor=white)
![Spring Boot](https://img.shields.io/badge/Spring%20Boot-3.3.5-brightgreen?logo=springboot&logoColor=white)
![Maven](https://img.shields.io/badge/Build-Maven-C71A36?logo=apachemaven&logoColor=white)
![License](https://img.shields.io/badge/License-MIT-blue.svg)

A console-based Employee Management System built with **Java 17** and **Spring Boot**, demonstrating a clean, layered architecture with JSON file persistence, input validation, and business-rule enforcement.

## Description

This project is an interactive command-line application for managing employee records — adding, listing, searching, updating, and removing employees through a simple text menu.

It was built as a hands-on exercise in applying **Clean Code**, **SOLID principles**, and **Clean Architecture** to a small, real-world CRUD domain: from a plain in-memory prototype, through JSON file persistence with Jackson, to a fully Spring Boot–managed application with dependency injection.

Rather than a toy script, it is structured the way a production service would be — with a domain model, a persistence abstraction, a business/service layer, and a presentation layer, each with clear responsibilities and its own test coverage.

## Features

- **Interactive console menu** — add, list, search, update, and remove employees
- **Domain validation** — name, position, and department cannot be blank; email must be well-formed; salary cannot be negative
- **Business rule enforcement** — no two employees may share the same email address; operations on unknown IDs are rejected with clear errors
- **JSON file persistence** — employees are automatically loaded on startup and saved after every change, powered by Jackson
- **Layered, testable architecture** — domain model, repository, service, and presentation layers are fully decoupled via interfaces
- **Dependency injection** — wired together with Spring Boot (`@Service`, `@Repository`, `@Component`, constructor injection)
- **Custom exception hierarchy** — precise, catchable error types instead of generic runtime exceptions
- **Unit tested** — JUnit 5 test suite covering the domain model, repository, and service layers

## Technologies Used

| Technology | Purpose |
|---|---|
| [Java 17](https://openjdk.org/projects/jdk/17/) | Core language |
| [Spring Boot 3.3.5](https://spring.io/projects/spring-boot) | Dependency injection, application bootstrap, configuration |
| [Maven](https://maven.apache.org/) | Build and dependency management |
| [Jackson](https://github.com/FasterXML/jackson) | JSON serialization/deserialization for file persistence |
| [JUnit 5](https://junit.org/junit5/) | Unit testing |

## Project Structure

```
EmployeeManagementSystem/
├── src/
│   ├── main/
│   │   ├── java/com/pedrodias/
│   │   │   ├── app/            # Application entry point and console menu (presentation layer)
│   │   │   │   ├── Main.java
│   │   │   │   └── ConsoleMenuRunner.java
│   │   │   ├── model/          # Domain model (immutable, self-validating)
│   │   │   │   └── Employee.java
│   │   │   ├── repository/     # Persistence layer (interface + JSON file implementation)
│   │   │   │   ├── EmployeeRepository.java
│   │   │   │   ├── FileEmployeeRepository.java
│   │   │   │   └── EmployeeRecord.java
│   │   │   ├── service/        # Business rules and use cases
│   │   │   │   └── EmployeeService.java
│   │   │   └── exception/      # Custom exception hierarchy
│   │   │       ├── EmployeeManagementException.java
│   │   │       ├── EmployeeNotFoundException.java
│   │   │       ├── DuplicateEmployeeException.java
│   │   │       ├── InvalidEmployeeDataException.java
│   │   │       └── EmployeeStorageException.java
│   │   └── resources/
│   │       └── application.properties
│   └── test/
│       └── java/com/pedrodias/
│           ├── model/
│           ├── repository/
│           └── service/
├── pom.xml
└── README.md
```

## How to Run Locally

### Prerequisites

- [JDK 17](https://adoptium.net/) or later
- [Maven 3.9+](https://maven.apache.org/download.cgi)

### Clone the repository

```bash
git clone https://github.com/pedrordias6/EmployeeManagementSystem.git
cd EmployeeManagementSystem
```

### Build

```bash
mvn clean package
```

### Run

```bash
mvn spring-boot:run
```

Or run the packaged jar directly:

```bash
java -jar target/EmployeeManagementSystem-1.0-SNAPSHOT.jar
```

The application runs in the terminal and presents an interactive menu:

```
=== Employee Management System ===
1. Add Employee
2. List Employees
3. Search Employee
4. Update Employee
5. Remove Employee
6. Exit
Choose an option:
```

Employee data is persisted to `employees.json` in the working directory by default. The file path can be overridden via `application.properties`:

```properties
employee.storage.file-path=employees.json
```

### Run the tests

```bash
mvn test
```

## Future Improvements

- [ ] Expose employee operations through a REST API (Spring Web)
- [ ] Replace JSON file storage with a relational database (PostgreSQL/H2 + Spring Data JPA)
- [ ] Add pagination and filtering for large employee lists
- [ ] Add authentication and role-based access control
- [ ] Containerize the application with Docker
- [ ] Set up a CI pipeline (GitHub Actions) for automated build and test
- [ ] Build a web or desktop front end

## License

This project is licensed under the [MIT License](LICENSE).
