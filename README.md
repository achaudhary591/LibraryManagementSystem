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
- [Screenshots](#screenshots)
- [API Documentation](#api-documentation)
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

## Project Structure

```
libtrack/
├── src/
│   ├── main/
│   │   ├── java/com/spark/lms/
│   │   │   ├── common/          # Constants and utility classes
│   │   │   ├── configuration/   # Security and web configuration
│   │   │   ├── controller/      # MVC controllers
│   │   │   ├── model/           # Entity classes
│   │   │   ├── repository/      # Data access interfaces
│   │   │   ├── service/         # Business logic layer
│   │   │   └── LibTrackApplication.java  # Main application class
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

## Database Schema

The application uses the following key entities:

1. **User**: System users (Admin, Librarian, Student)
2. **Member**: Library members (Students, Parents, Others)
3. **Category**: Book categories/genres
4. **Book**: Library books with metadata
5. **Issue**: Records of book issuance
6. **IssuedBook**: Many-to-many relationship between Issue and Book

## API Documentation

The application provides REST endpoints for programmatic access:

- `/api/books`: Book management
- `/api/categories`: Category management
- `/api/members`: Member management
- `/api/issues`: Issue management

These endpoints support standard CRUD operations and can be used for integration with other systems.

## Troubleshooting

### Common Issues

1. **Database Connection Issues**
   - Verify database credentials in application.properties
   - Ensure PostgreSQL service is running

2. **Application Won't Start**
   - Check Java version (must be 17 or higher)
   - Verify port 8080 is not in use by another application

3. **Login Problems**
   - Default credentials: admin/admin
   - Check if users exist in the database

## Future Enhancements

- Fine calculation and payment tracking
- Email notifications for due dates
- Barcode/QR code integration
- Mobile application
- Online reservation system
- Integration with external catalogs
- Enhanced reporting and analytics

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
