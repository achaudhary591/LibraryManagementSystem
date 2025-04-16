# LibTrack API Documentation

This document provides comprehensive documentation for the LibTrack Library Management System REST API.

## Authentication

All API endpoints require authentication. The application uses Basic Authentication.

### Authentication Headers

```
Authorization: Basic base64(username:password)
```

## API Endpoints

### Book Management

#### Get All Books

```
GET /rest/book/list
```

**Response:**
```json
[
  {
    "id": 1,
    "title": "Spring Boot in Action",
    "tag": "TECH-001",
    "authors": "Craig Walls",
    "publisher": "Manning",
    "isbn": "9781617292545",
    "yearOfPublication": 2016,
    "numOfCopies": 5,
    "status": 1
  },
  // More books...
]
```

#### Get Book by ID

```
GET /rest/book/{id}
```

**Response:**
```json
{
  "id": 1,
  "title": "Spring Boot in Action",
  "tag": "TECH-001",
  "authors": "Craig Walls",
  "publisher": "Manning",
  "isbn": "9781617292545",
  "yearOfPublication": 2016,
  "numOfCopies": 5,
  "status": 1
}
```

#### Search Books

```
GET /rest/book/search?q={query}
```

**Response:**
```json
[
  {
    "id": 1,
    "title": "Spring Boot in Action",
    "tag": "TECH-001",
    "authors": "Craig Walls",
    "publisher": "Manning",
    "isbn": "9781617292545",
    "yearOfPublication": 2016,
    "numOfCopies": 5,
    "status": 1
  },
  // More matching books...
]
```

### Member Management

#### Get All Members

```
GET /rest/member/list
```

**Response:**
```json
[
  {
    "id": 1,
    "firstName": "John",
    "lastName": "Doe",
    "email": "john.doe@example.com",
    "phone": "1234567890",
    "type": "Student"
  },
  // More members...
]
```

#### Get Member by ID

```
GET /rest/member/{id}
```

**Response:**
```json
{
  "id": 1,
  "firstName": "John",
  "lastName": "Doe",
  "email": "john.doe@example.com",
  "phone": "1234567890",
  "type": "Student"
}
```

#### Search Members

```
GET /rest/member/search?q={query}
```

**Response:**
```json
[
  {
    "id": 1,
    "firstName": "John",
    "lastName": "Doe",
    "email": "john.doe@example.com",
    "phone": "1234567890",
    "type": "Student"
  },
  // More matching members...
]
```

### Issue Management

#### Save New Issue

```
POST /rest/issue/save
```

**Request Parameters:**
- `member`: Member ID
- `books`: Comma-separated list of book IDs

**Response:**
```
success
```

#### Return All Books in an Issue

```
GET /rest/issue/{id}/return/all
```

**Response:**
```
successful
```

#### Return Selected Books in an Issue

```
POST /rest/issue/{id}/return
```

**Request Parameters:**
- `ids`: Comma-separated list of issued book IDs to return

**Response:**
```
successful
```

### User Management

#### Get All Users

```
GET /rest/user/list
```

**Response:**
```json
[
  {
    "id": 1,
    "username": "admin",
    "displayName": "Administrator",
    "role": "ADMIN",
    "active": true
  },
  // More users...
]
```

#### Get User by Username

```
GET /rest/user/{username}
```

**Response:**
```json
{
  "id": 1,
  "username": "admin",
  "displayName": "Administrator",
  "role": "ADMIN",
  "active": true
}
```

## Status Codes and Error Handling

### Success Codes

- `200 OK`: The request was successful
- `201 Created`: A new resource was successfully created

### Error Codes

- `400 Bad Request`: The request was invalid or cannot be served
- `401 Unauthorized`: Authentication failed or user doesn't have permissions
- `404 Not Found`: The requested resource does not exist
- `500 Internal Server Error`: An error occurred on the server

### Error Response Format

```json
{
  "timestamp": "2025-04-16T12:30:45.123+00:00",
  "status": 400,
  "error": "Bad Request",
  "message": "Invalid input data",
  "path": "/rest/book/save"
}
```

## Data Models

### Book

| Field | Type | Description |
|-------|------|-------------|
| id | Long | Unique identifier |
| title | String | Book title |
| tag | String | Unique tag/call number |
| authors | String | Book authors |
| publisher | String | Publisher name |
| isbn | String | ISBN number |
| yearOfPublication | Integer | Publication year |
| numOfCopies | Integer | Number of copies |
| category | Category | Book category |
| status | Integer | 1=Available, 2=Issued |

### Member

| Field | Type | Description |
|-------|------|-------------|
| id | Long | Unique identifier |
| firstName | String | First name |
| middleName | String | Middle name |
| lastName | String | Last name |
| email | String | Email address |
| phone | String | Phone number |
| type | String | Member type (Student, Parent, Other) |

### Issue

| Field | Type | Description |
|-------|------|-------------|
| id | Long | Unique identifier |
| member | Member | Member who borrowed books |
| issuedBooks | List<IssuedBook> | Books issued in this transaction |
| issueDate | Date | Date when books were issued |
| status | Integer | 0=Not Returned, 1=Returned |

### IssuedBook

| Field | Type | Description |
|-------|------|-------------|
| id | Long | Unique identifier |
| issue | Issue | Parent issue |
| book | Book | Book that was issued |
| returned | Integer | 0=Not Returned, 1=Returned |
| returnedDate | Date | Date when book was returned |

### User

| Field | Type | Description |
|-------|------|-------------|
| id | Long | Unique identifier |
| username | String | Username for login |
| password | String | Encrypted password |
| displayName | String | Display name |
| role | String | User role (ADMIN, LIBRARIAN, STUDENT) |
| active | Boolean | Whether user is active |

## Pagination

For endpoints that return collections, pagination is supported with the following query parameters:

- `page`: Page number (0-based)
- `size`: Page size
- `sort`: Sort field and direction (e.g., `sort=lastName,asc`)

Example:
```
GET /rest/book/list?page=0&size=10&sort=title,asc
```

## Rate Limiting

API rate limiting is applied to prevent abuse:

- Anonymous requests: 60 requests per hour
- Authenticated requests: 1000 requests per hour

Rate limit headers are included in responses:
```
X-RateLimit-Limit: 1000
X-RateLimit-Remaining: 999
X-RateLimit-Reset: 1618584000
```

## Versioning

The current API version is v1. The version is included in the URL path:

```
/api/v1/books
```

## Changelog

### Version 1.0.0 (2025-04-16)
- Initial API release

## Support

For API support, please contact:
- Email: api-support@libtrack.com
- Documentation: https://libtrack.com/api-docs
