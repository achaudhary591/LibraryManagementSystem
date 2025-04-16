# LibTrack - Library Management System

![LibTrack Logo](https://img.shields.io/badge/LibTrack-Library%20Management%20System-blue)
![Spring Boot](https://img.shields.io/badge/Spring%20Boot-3.2.x-brightgreen)
![Java](https://img.shields.io/badge/Java-17-orange)
![PostgreSQL](https://img.shields.io/badge/PostgreSQL-Latest-blue)
![License](https://img.shields.io/badge/License-MIT-green)

A comprehensive Library Management System developed with Spring Boot that provides a complete solution for managing library resources, members, and book circulation.

## Table of Contents
- [Overview](#overview)
- [Features](#features)
- [Technology Stack](#technology-stack)
- [System Requirements](#system-requirements)
- [Setup Instructions](#setup-instructions)
- [Project Structure](#project-structure)
- [User Roles and Permissions](#user-roles-and-permissions)
- [API Documentation](#api-documentation)
- [Known Issues and Fixes](#known-issues-and-fixes)
- [Future Enhancements](#future-enhancements)
- [Contributing](#contributing)
- [License](#license)

## Overview

LibTrack is a full-featured Library Management System designed to streamline the operations of libraries of all sizes. It provides an intuitive interface for librarians to manage books, members, and circulation processes, while also offering a user-friendly experience for library members to browse and check out books.

## Features

### Dashboard
- Visual statistics of library operations
- Quick access to key functions
- Summary of recent activities

### Member Management
- Add, edit, and delete library members
- Categorize members (Student, Parent, Other)
- Track member details and borrowing history
- Create student accounts with login credentials

### Category Management
- Create and manage book categories/genres
- Organize books by subject matter
- Track books by category

### Book Management
- Add, edit, and delete books
- Assign books to categories
- Track book status (Available, Issued)
- Search books by various criteria (title, author, ISBN)

### Issue Management
- Issue books to members
- Set due dates for returns
- Process book returns
- Track overdue books
- View issue history

### User Management
- Role-based access control (Admin, Librarian, Student)
- User authentication and authorization
- User profile management

## Technology Stack

### Backend
- **Java 17**: Core programming language
- **Spring Boot 3.2.x**: Application framework
- **Spring Security 6**: Authentication and authorization
- **Spring Data JPA**: Data access layer
- **Hibernate**: ORM for database operations
- **PostgreSQL**: Relational database
- **OpenAPI/Swagger**: API documentation

### Frontend
- **Thymeleaf**: Server-side Java template engine
- **Bootstrap**: Responsive UI framework
- **HTML/CSS/JavaScript**: Frontend technologies
- **jQuery**: JavaScript library for DOM manipulation

### Build Tools
- **Maven**: Dependency management and build automation

## System Requirements

- **Java**: JDK 17 or higher
- **PostgreSQL**: 10.0 or higher
- **Maven**: 3.6 or higher
- **Memory**: 2GB RAM minimum, 4GB recommended
- **Storage**: 500MB for application, database size depends on library data volume

## Setup Instructions

### Prerequisites
1. Install Java 17 or higher
2. Install PostgreSQL 10.0 or higher
3. Install Maven 3.6 or higher

### Database Setup
1. Create a PostgreSQL database named `libtrack`:
   ```sql
   CREATE DATABASE libtrack;
   ```
2. No additional manual database setup is required - the application will automatically create necessary tables and sequences

### Application Setup
1. Clone the repository:
   ```bash
   git clone https://github.com/yourusername/libtrack.git
   cd libtrack
   ```

2. Configure database connection in `src/main/resources/application.properties`:
   ```properties
   spring.datasource.url=jdbc:postgresql://localhost:5432/libtrack
   spring.datasource.username=your_postgres_username
   spring.datasource.password=your_postgres_password
   ```

3. Build the application:
   ```bash
   mvn clean package
   ```

4. Run the application:
   ```bash
   java -jar target/libtrack-0.0.1-SNAPSHOT.jar
   ```

5. Access the application at `http://localhost:8080`

### Default Login Credentials
- **Admin**: Username: `admin`, Password: `admin`
- **Librarian**: Username: `librarian`, Password: `librarian`

The application automatically creates these default users when it starts up for the first time.

## Project Structure

```
libtrack/
├── src/
│   ├── main/
│   │   ├── java/com/spark/lms/
│   │   │   ├── common/          # Constants and utility classes
│   │   │   ├── config/          # Configuration classes (OpenAPI, etc.)
│   │   │   ├── configuration/   # Security and web configuration
│   │   │   ├── controller/      # MVC controllers
│   │   │   │   └── rest/        # REST API controllers
│   │   │   ├── dto/             # Data Transfer Objects
│   │   │   ├── exception/       # Exception handling
│   │   │   ├── model/           # Entity classes
│   │   │   ├── repository/      # Data access interfaces
│   │   │   ├── service/         # Business logic layer
│   │   │   └── util/            # Utility classes
│   │   ├── resources/
│   │   │   ├── static/          # Static resources (CSS, JS)
│   │   │   ├── templates/       # Thymeleaf templates
│   │   │   ├── application.properties  # Application configuration
│   │   │   └── schema.sql       # Database initialization script
│   └── test/                    # Test classes
└── pom.xml                      # Maven configuration
```

## User Roles and Permissions

### Admin
- Full access to all system features
- Manage users, members, books, categories, and issues
- View system statistics and reports

### Librarian
- Manage members, books, categories, and issues
- Process book issuance and returns
- View library statistics

### Student
- View available books
- View personal borrowing history
- Update personal profile

## API Documentation

The application provides REST endpoints for programmatic access and includes OpenAPI/Swagger documentation:

- Access the API documentation at: `http://localhost:8080/swagger-ui.html`

Key API endpoints:
- `/api/books`: Book management
- `/api/categories`: Category management
- `/api/members`: Member management
- `/api/issues`: Issue management

## Known Issues and Fixes

### Java 23 Compatibility
When running with Java 23, tests may fail due to Byte Buddy compatibility issues. Solutions:

1. Add the JVM flag to Maven Surefire plugin (already implemented):
   ```xml
   <plugin>
     <groupId>org.apache.maven.plugins</groupId>
     <artifactId>maven-surefire-plugin</artifactId>
     <configuration>
       <argLine>-Dnet.bytebuddy.experimental=true</argLine>
     </configuration>
   </plugin>
   ```

2. Alternatively, use Java 17 or 21 which are fully compatible.

### Return Date Display Issue
Fixed an issue where the student view would throw an error when viewing returned books due to:
- Template referencing `returnDate` instead of `returnedDate`
- Return date not being set when books were returned

### PostgreSQL Reserved Keywords
Table name `user` was changed to `users` to avoid conflicts with PostgreSQL reserved keywords.

## Future Enhancements

- **Fine Calculation**: Implement automatic fine calculation for overdue books
- **Email Notifications**: Send notifications for due dates and overdue books
- **Barcode/QR Integration**: Add support for barcode/QR code scanning
- **Mobile Application**: Develop a companion mobile app
- **Online Reservation**: Allow members to reserve books online
- **External Catalog Integration**: Connect with external book catalogs
- **Enhanced Reporting**: Add more detailed reporting and analytics
- **Self-Service Returns**: Allow students to mark books as returned (pending librarian approval)
- **Book Reviews**: Add functionality for members to review books
- **Digital Content**: Support for e-books and digital resources

## Contributing

Contributions are welcome! Please feel free to submit a Pull Request.

1. Fork the repository
2. Create your feature branch (`git checkout -b feature/amazing-feature`)
3. Commit your changes (`git commit -m 'Add some amazing feature'`)
4. Push to the branch (`git push origin feature/amazing-feature`)
5. Open a Pull Request

## License

This project is licensed under the MIT License - see the LICENSE file for details.

---

Developed with ❤️ by [Akshya_Chaudhary]
