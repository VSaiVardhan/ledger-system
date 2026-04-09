# Financial Ledger System

A back-end focused **Financial Ledger & Double-Entry Transaction Engine**, with light-weight front-end, built using **Spring Boot and MySQL**, implementing core accounting principles like debit-credit balancing and running balance tracking.

🔗 [Click here to try the live demo](https://ledger-system-df4y.onrender.com)

---

## Features

- Account creation and management
- Double-entry transaction system (Debit = Credit)
- Running balance calculation (ledger-style)
- Transaction history with entry drill-down
- Date-based filtering for statements
- Monthly summary (debit, credit, net change)
- Transaction reversal (non-destructive)
- Swagger API documentation
- Minimal frontend UI for demonstration
- Cloud deployment (Render + Railway)

---

## Tech Stack

### Backend
- Java Spring Boot
- Spring Data JPA (Hibernate)
- MySQL

### Frontend
- HTML, CSS, JavaScript

### Tools & Deployment
- Maven
- Postman
- Swagger (OpenAPI)
- Render (Backend hosting)
- Railway (Cloud database)

---

## API Endpoints (Sample)

### Accounts
- `POST /accounts` → Create account
- `GET /accounts` → Get all accounts
- `GET /accounts/{id}/statement` → Account statement
- `GET /accounts/{id}/summary` → Monthly summary

### Transactions
- `POST /transactions/double` → Create double-entry transaction
- `POST /transactions/{id}/reverse` → Reverse transaction
- `GET /transactions` → Transaction history
- `GET /transactions/{id}/entries` → Get entries of transaction

---

## Core Concept

This system follows **double-entry accounting**:

- Every transaction has:
  - At least one **DEBIT**
  - At least one **CREDIT**
- Total Debit = Total Credit
- Ensures financial consistency

---

## Example

```json
{
  "description": "Salary received",
  "date": "2026-04-10",
  "entries": [
    {
      "accountId": 1,
      "type": "DEBIT",
      "amount": 50000
    },
    {
      "accountId": 2,
      "type": "CREDIT",
      "amount": 50000
    }
  ]
}
```

---

## How to Run the Project

### Prerequisites

Should have the following installed:

- Java 21+
- Maven
- MySQL
- Git

### Clone the Repository

```bash
git clone https://github.com/VSaiVardhan/ledger-system.git
```

### Setup MySQL Database

Open MySQL and run:

```sql
CREATE DATABASE ledger_db;
```

### Configure Application Properties

Update `src/main/resources/application.properties`:

```bash
spring.datasource.url=jdbc:mysql://localhost:3306/ledger_db
spring.datasource.username=YOUR_USERNAME
spring.datasource.password=YOUR_PASSWORD
```

### Run the Application

Using Maven:

```bash
mvn spring-boot:run
```

### Access the Application

- **API Base:** http://localhost:8080  
- **Swagger UI:** http://localhost:8080/swagger-ui.html  
- **Frontend UI:** http://localhost:8080/index.html

> This project supports both local setup and cloud deployment. 

---

## Project Structure

```text
ledger-system/
│
├── controller/        # REST Controllers (API layer)
├── service/           # Business logic layer
├── repository/        # Data access layer (JPA)
├── model/             # Entity classes (Account, Transaction, Entry)
├── dto/               # Data Transfer Objects
├── exception/         # Custom exception handling
│
├── resources/
│   ├── application.properties
│   ├── static/        # Frontend (HTML, CSS, JS)
│           
├── pom.xml            # Maven configuration
│
└── README.md
```

### Architecture Pattern
This project follows a layered architecture:
Controller → Service → Repository → Database

---

## Author
 
[VSaiVardhan](https://github.com/VSaiVardhan)