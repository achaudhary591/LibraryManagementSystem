# LibTrack - Library Management System: Troubleshooting and Fixes

## Issues Fixed

### 1. Compilation Errors
- Added missing role constants in `Constants.java`:
  ```java
  public static final String ROLE_ADMIN = "ROLE_ADMIN";
  public static final String ROLE_LIBRARIAN = "ROLE_LIBRARIAN";
  public static final String ROLE_STUDENT = "ROLE_STUDENT";
  ```
- Added missing methods to `UserService`:
  - `getAllUsers()`
  - `getByUsername(String username)`
  - `findByUsername(String username)`
  - `createStudentUser(Member member, String username, String password)`
  - `addNew(User user)`
- Enhanced `User` model with `displayName` field and constructor

### 2. Bean Definition Conflicts
- Removed duplicate `WebMvcConfig` class (there were two versions in different packages)
- Removed duplicate REST controllers in `restcontroller` package that conflicted with those in `controller.rest`
- Added `spring.main.allow-bean-definition-overriding=true` to application.properties

### 3. PostgreSQL Reserved Keyword Issue
- Changed table name from `user` to `users` in the `User` entity to avoid PostgreSQL reserved keyword
- Updated security queries in application.properties to reference the new table name

### 4. Database Type Conversion
- Fixed column type issue by converting the `active` column in the `users` table from integer to boolean:
  ```sql
  ALTER TABLE users ALTER COLUMN active TYPE boolean USING CASE WHEN active=1 THEN true ELSE false END;
  ```

### 5. Thymeleaf Template Resolution
- Fixed login template resolution by changing the return value in `LoginController` from `/login` to `login` (removed the leading slash)

### 6. Missing Access Denied Page
- Created an `access-denied.html` template to handle 403 Forbidden errors
- Added proper styling and navigation back to home page
- Created an `AccessDeniedController` to handle the access-denied route

## Running the Application
The application should now build and run successfully. You can start it with:
```bash
java -jar target/libtrack-0.0.1-SNAPSHOT.jar
```

## Default Login Credentials
- **Admin**: Username: `admin`, Password: `admin`
- **Librarian**: Username: `librarian`, Password: `librarian`

## Next Steps
1. Test the application by accessing http://localhost:8080
2. Verify that user authentication works with the default credentials
3. Test book management, member management, and issue management features
