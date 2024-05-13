# Library Management System using Spring Boot

This project aims to develop a robust Library Management System (LMS) API using Spring Boot. The system enables librarians to efficiently manage books, patrons, and borrowing records. Below is a comprehensive guide to understand, implement, and evaluate the project.

# Project Description

The Library Management System API facilitates the management of books, patrons, and borrowing records. It provides RESTful endpoints to perform CRUD operations on books and patrons, as well as to manage book borrowings and returns.

## Getting Started
### Prerequisites
 - An IDE such as IntelliJ IDEA or Microsoft VS code

 ### Configuration
 Before running the application, make sure to configure JWT settings in the application.properties file.

 ##### - **Open src/main/resources/application.properties.**

 ##### - **Configure JWT settings**
        jwt.expiration = 600000

The application will start running at http://localhost:8090.

# Requirements
**Entities**

- **Book:** Represents a book with attributes such as ID, title, author, publication year, ISBN, etc.
- **Patron:** Represents a patron with details like ID, name, contact information, etc.
- **Borrowing Record:** Tracks the association between books and patrons, including borrowing and return dates.

## API Endpoints

### Book Management Endpoints

- `GET /api/books`: Retrieve a list of all books.
- `GET /api/books/{id}`: Retrieve details of a specific book by ID.
- `POST /api/books`: Add a new book to the library.
    - Required JSON Payload:
       ```json
        {
         "name": "Book Name",
         "author": "Author Name",
         "publicationYear": 2021,
         "isbn": "ISBN-122"
        }
       ```

- `PUT /api/books/{id}`: Update an existing book's information.
    - Required JSON Payload for Update:
       ```json
        {
         "name": "Updated Book Name",
         "author": "Updated Author Name",
         "publicationYear": 2022,
         "isbn": "Updated ISBN"
        }
       ```
- `DELETE /api/books/{id}`: Remove a book from the library.

### Patron Management Endpoints

- `GET /api/patrons`: Retrieve a list of all patrons.
- `GET /api/patrons/{id}`: Retrieve details of a specific patron by ID.
- `POST /api/patrons`: Add a new patron to the system.
    - Required JSON Payload:
       ```json
        {
         "name": "Patron Name",
         "address": "Patron Address",
         "phoneNumber": "Patron Phone Number",
         "email": "patron_email@example.com",
         "password": "password"
        }
       ```
- `PUT /api/patrons/{id}`: Update an existing patron's information.
    - Required JSON Payload for Update:
       ```json
        {
         "name": "Updated Patron Name",
         "address": "Updated Patron Address",
         "phoneNumber": "Updated Patron Phone Number",
         "email": "updated_patron_email@example.com",
         "password": "updated_password"
        }
       ```
- `DELETE /api/patrons/{id}`: Remove a patron from the system.

### Borrowing Endpoints
Users must be loggid into the system to do either borrow or return a book
- `POST /api/borrow/{bookId}/patron/{patronId}`: Allow a patron to borrow a book.
- `PUT /api/return/{bookId}/patron/{patronId}`: Record the return of a borrowed book by a patron.

## Data Storage

Database used is H2 Database to persist book, patron, and borrowing record details.

## Validation and Error Handling
Input validation for API requests, including validation of required fields and data formats, has been implemented to ensure data integrity and consistency. Additionally, exceptions are handled gracefully, and appropriate HTTP status codes and error messages are returned to provide meaningful feedback to clients.

## Security
For enhanced security measures, JWT-based authorization is implemented to protect the API endpoints. JSON Web Tokens (JWT) provide a stateless authentication mechanism by securely transmitting information between parties as a JSON object. This approach ensures that only authorized users can access the system's functionalities.

## Testing

Unit tests are crucial for ensuring the reliability and correctness of the Library Management System API. The following testing frameworks were utilized to validate the functionality of API endpoints:

1. **JUnit**: JUnit is a widely-used testing framework for Java that provides annotations to define test methods and assertions to verify expected outcomes.

2. **Mockito**: Mockito is a mocking framework that facilitates the creation of mock objects to simulate dependencies during testing.

3. **SpringBootTest**: SpringBootTest is an integration testing framework provided by Spring Boot. It allows testing of Spring Boot applications in a real environment by loading the complete application context. Integration tests using SpringBootTest were conducted to verify the interactions between different layers of the application and ensure that they function as expected.

These testing frameworks were instrumental in verifying the correctness and reliability of the API endpoints, ensuring that the Library Management System operates as intended under various scenarios and conditions.






