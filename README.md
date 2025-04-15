# LibTrack - Library Management System

A comprehensive Library Management System developed in Spring Boot.

## What does it offer?
It allows users to manage Members, Categories, Books and Book Issuance.

## Technology Stack
- Java 17
- Spring Boot 3.2.x
- Spring Security 6
- Spring Data JPA
- Thymeleaf
- PostgreSQL
- Maven

## Setup project
### Setup database
Project requires PostgreSQL database. The version of the database is preferred to be 10.0 or later.

### Setup code
1. Clone the repository
2. Configure database connection in `application.properties`
3. Build the application using Maven: `mvn clean package`
4. Run the application: `java -jar target/libtrack-0.0.1-SNAPSHOT.jar`
5. Access the application at `http://localhost:8080`

### Login
For login you may use username as 'admin' and password as 'admin'.

## Features
- User Management (Admin, Librarian)
- Member Management
- Category Management
- Book Management
- Issue Management
- Dashboard with statistics
