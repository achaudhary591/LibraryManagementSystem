i# Spark Library Management System (LMS) - Technical Documentation

## 1. Executive Summary

Spark LMS is a comprehensive Library Management System designed to streamline library operations including book cataloging, member management, and circulation processes. Built on modern Java technologies with Spring Boot, the application provides a robust, secure, and user-friendly platform for library staff to efficiently manage resources and serve library patrons.

## 2. Technology Stack

### Backend Technologies
- **Java 8**: Core programming language
- **Spring Boot 2.1.4**: Application framework with auto-configuration
- **Spring MVC**: Web framework for handling HTTP requests
- **Spring Data JPA**: Data access abstraction layer
- **Spring Security**: Authentication and authorization framework
- **Hibernate ORM**: Object-relational mapping for database operations
- **PostgreSQL**: Relational database management system

### Frontend Technologies
- **Thymeleaf**: Server-side Java template engine
- **Bootstrap**: Responsive UI framework
- **jQuery**: JavaScript library for DOM manipulation
- **HTML5/CSS3**: Standard web technologies

### Build and Deployment
- **Maven**: Dependency management and build tool
- **Spring Boot Embedded Tomcat**: Application server
- **Git**: Version control system

## 3. Application Architecture

Spark LMS follows a layered architecture pattern with clear separation of concerns:

### Presentation Layer
- **Controllers**: Handle HTTP requests and responses
- **Views**: Thymeleaf templates for rendering HTML
- **REST Controllers**: Provide API endpoints for programmatic access

### Business Layer
- **Services**: Implement business logic and transaction management
- **DTOs**: Data transfer objects for communication between layers

### Data Access Layer
- **Repositories**: Spring Data JPA interfaces for database operations
- **Entities**: JPA-annotated domain models representing database tables

### Cross-Cutting Concerns
- **Security**: Authentication, authorization, and access control
- **Configuration**: Application settings and bean definitions
- **Constants**: System-wide constants and enumerations

## 4. Domain Model

The application is built around these core entities:

### User
- Represents system users (Admin, Librarian)
- Stores authentication credentials and role information
- Manages access control to system functions

### Member
- Represents library patrons (Students, Parents, Others)
- Stores personal information and contact details
- Tracks borrowing history and status

### Category
- Represents book classifications or genres
- Organizes books into logical groupings
- Facilitates book discovery and management

### Book
- Represents physical library resources
- Stores bibliographic information (title, author, ISBN)
- Tracks availability status and location

### Issue
- Represents book lending transactions
- Links members to borrowed books
- Tracks issue dates, due dates, and return status

### IssuedBook
- Represents the many-to-many relationship between issues and books
- Tracks individual book status within an issue
- Manages return status for each book

## 5. Application Flow

### System Initialization
1. Application starts and establishes database connection
2. Hibernate creates/updates database schema based on entity mappings
3. `ApplicationStartup` listener checks for existing users
4. If no users exist, default admin and librarian accounts are created
5. Web server initializes and application becomes available

### User Authentication
1. Unauthenticated users are redirected to login page
2. User submits credentials (username/password)
3. Spring Security validates credentials against database records
4. Upon successful authentication, user is granted access based on role
5. Failed authentication returns to login page with error message

### Dashboard/Home Page
1. Displays summary statistics (total members, books, categories)
2. Shows counts of available and issued books
3. Provides quick access to key system functions
4. Adapts displayed information based on user role

### Member Management
1. List view displays all members with filtering options
2. Add/Edit forms capture member details with validation
3. Delete functionality with confirmation
4. Member type classification (Student, Parent, Other)

### Category Management
1. List view displays all categories
2. Add/Edit forms for category information
3. Delete functionality with validation to prevent orphaned books
4. Association with books for organizational purposes

### Book Management
1. List view with search and filter capabilities
2. Add/Edit forms for bibliographic information
3. Category assignment for organizational purposes
4. Status tracking (Available, Issued)
5. Delete functionality with validation

### Issue Management
1. New issue creation with member selection
2. Book selection with availability validation
3. Issue date recording and due date calculation
4. Return processing with status updates
5. History tracking for audit purposes

## 6. Security Implementation

### Authentication
- Form-based authentication with username/password
- BCrypt password encoding for secure credential storage
- Session-based user tracking
- Remember-me functionality for convenience

### Authorization
- Role-based access control (RBAC)
- Two primary roles: Admin and Librarian
- Function-level security using Spring Security annotations
- URL-based security constraints

### Security Configuration
- Custom security configuration in `SecurityConfiguration.java`
- Database-backed user authentication
- Custom login page and authentication flow
- Stateless API security for REST endpoints

## 7. Database Schema

### Users Table
- id (PK)
- username
- password (encrypted)
- display_name
- role
- active
- created_date
- last_modified_date

### Members Table
- id (PK)
- first_name
- last_name
- email
- phone
- type (Student, Parent, Other)
- created_date

### Categories Table
- id (PK)
- name
- short_name
- notes
- created_date

### Books Table
- id (PK)
- title
- tag
- authors
- publisher
- isbn
- status (Available, Issued)
- category_id (FK)
- created_date

### Issues Table
- id (PK)
- member_id (FK)
- issue_date
- return_date
- returned (0=No, 1=Yes)
- created_date

### Issued_Books Table
- id (PK)
- issue_id (FK)
- book_id (FK)
- returned (0=No, 1=Yes)
- returned_date

## 8. User Guide

### System Access
- Access the application at http://localhost:8080
- Login with provided credentials:
  - Admin: username=`admin`, password=`admin`
  - Librarian: username=`librarian`, password=`librarian`

### Dashboard Navigation
- The top navigation bar provides access to all major functions
- The dashboard displays summary statistics and quick access tiles
- User information and logout option are available in the top-right corner

### Managing Members
1. Click "Members" in the navigation menu
2. View the list of existing members
3. Click "Add Member" to create a new member
4. Fill in required information (name, contact details, type)
5. Click "Save" to create the member
6. Use the action buttons to edit or delete existing members

### Managing Categories
1. Click "Categories" in the navigation menu
2. View the list of existing categories
3. Click "Add Category" to create a new category
4. Enter category name, short name, and optional notes
5. Click "Save" to create the category
6. Use the action buttons to edit or delete existing categories

### Managing Books
1. Click "Books" in the navigation menu
2. View the list of existing books with filter options
3. Click "Add Book" to create a new book entry
4. Fill in book details (title, author, ISBN, etc.)
5. Select a category from the dropdown
6. Click "Save" to create the book entry
7. Use the action buttons to edit or delete existing books

### Issuing Books
1. Click "Issues" in the navigation menu
2. Click "New Issue" to start the issue process
3. Select a member from the dropdown or search
4. Select the books to be issued
5. Confirm the issue details
6. Click "Save" to complete the issue process
7. The system updates book status to "Issued"

### Returning Books
1. Click "Issues" in the navigation menu
2. View the list of current issues
3. Find the issue to process a return
4. Click the "Return" button
5. Confirm the return action
6. The system updates book status to "Available"

## 9. REST API Documentation

The application provides a RESTful API for programmatic access to core functions:

### Book API
- `GET /api/books`: List all books
- `GET /api/books/{id}`: Get book by ID
- `POST /api/books`: Create new book
- `PUT /api/books/{id}`: Update existing book
- `DELETE /api/books/{id}`: Delete book

### Category API
- `GET /api/categories`: List all categories
- `GET /api/categories/{id}`: Get category by ID
- `POST /api/categories`: Create new category
- `PUT /api/categories/{id}`: Update existing category
- `DELETE /api/categories/{id}`: Delete category

### Member API
- `GET /api/members`: List all members
- `GET /api/members/{id}`: Get member by ID
- `POST /api/members`: Create new member
- `PUT /api/members/{id}`: Update existing member
- `DELETE /api/members/{id}`: Delete member

### Issue API
- `GET /api/issues`: List all issues
- `GET /api/issues/{id}`: Get issue by ID
- `POST /api/issues`: Create new issue
- `PUT /api/issues/{id}/return`: Process return for issue

## 10. Installation and Deployment

### Prerequisites
- Java 8 or higher
- PostgreSQL 8.0 or higher
- Maven 3.x (for building from source)

### Database Setup
1. Create a PostgreSQL database named `libtrack`
2. Configure database credentials in `application.properties`
3. The application will automatically create the schema on first run

### Development Setup
1. Clone the repository
2. Configure database connection in `application.properties`
3. Run `mvn spring-boot:run` to start the application in development mode
4. Access the application at http://localhost:8080

### Production Deployment
1. Build the application: `mvn clean package`
2. Deploy the generated WAR file to a servlet container
3. Configure production database settings
4. Set appropriate logging levels
5. Configure HTTPS for secure access

## 11. Troubleshooting

### Common Issues

#### Database Connection Problems
- Verify database credentials in `application.properties`
- Ensure PostgreSQL service is running
- Check network connectivity to database server

#### Login Issues
- Verify user exists in the database
- Check password is correct
- Ensure user account is active

#### Book Issue Failures
- Verify member exists and is active
- Check book availability status
- Ensure no database constraints are violated

### Logging
- Application logs are available in the console output
- Configure log levels in `application.properties`
- For production, configure external logging

## 12. Future Enhancements

### Short-term Improvements
- Enhanced search capabilities
- Barcode integration for book processing
- Email notifications for due dates
- Report generation (usage statistics, overdue items)

### Long-term Roadmap
- Fine management system
- Online catalog for members
- Reservation system
- Mobile application for staff and members
- Integration with external library systems
- Digital content management

## 13. Conclusion

Spark LMS provides a comprehensive solution for library management needs with a clean, modern architecture. The system's modular design allows for easy extension and customization to meet specific library requirements. By leveraging Spring Boot and related technologies, the application delivers a robust, secure, and user-friendly experience for library staff.
