# Trading App Backend

The backend of the Trading App is built with **Spring Boot** and is designed to power a secure, scalable, and real-time mobile trading platform.

It handles authentication, trading operations, REST APIs, WebSocket communication, database management, and containerized deployment using Docker Compose.

---

## Technologies Used

### Backend
- **Java**
- **Spring Boot**
- **Spring Web**
- **Spring Security**
- **Spring Data JPA**
- **Spring Data MongoDB**
- **REST API**
- **WebSocket**

### Database
- **MySQL** – for structured relational data such as users, orders, transactions, and balances
- **MongoDB** – for flexible document-based storage

### Database Migration
- **Flyway** – for version-controlled database schema migrations

### DevOps / Deployment
- **Docker**
- **Docker Compose**
- **Environment Variables (.env)**

---

## Security

The backend includes security-focused design and practices such as:

- **Spring Security** for securing endpoints
- **Authentication and authorization**
- **Protected API routes**
- **Password hashing / encryption**
- **Secure secret management using environment variables**
- **CORS configuration**
- **Request validation**
- **Password reset support**
- **OAuth/social login support** for providers like Google and Apple

### Security Best Practices
- Never hardcode secrets in source code
- Create your own `.env` file locally
- Do not commit real credentials to GitHub
- Use strong passwords and JWT secrets
- Restrict allowed origins in production
- Use HTTPS in production deployments

---

## Environment Variables

Create your own `.env` file in the backend root directory.

Example:

```env
AWS_ACCESS_KEY_ID=tempaccesskey
AWS_SECRET_ACCESS_KEY=tempsecretkey
AWS_REGION=us-east-1


# MySQL
MYSQL_HOST=db
MYSQL_PORT=3306
MYSQL_DATABASE=tradingdb
MYSQL_URL=jdbc:mysql://db:3306/tradingdb
MYSQL_USERNAME=tradingserveruser
MYSQL_PASSWORD=Password1!
MYSQL_ROOT_PASSWORD=Password1!
SPRING_JPA_PROPERTIES_HIBERNATE_DIALECT=org.hibernate.dialect.MySQLDialect

# JWT Authentication
JWT_SECRET=tradingserverSecretKeyForDevOnly
JWT_SERVER_ACCESS_EXPIRATION=2592000000
JWT_REFRESH_EXPIRATION=604800000
JWT_EXPIRATION=86400000
JWT_RESET_PASSWORD_EXPIRATION=86400000


# Mongo DB
MONGO_DATABASE=tradingdb
MONGO_URL=mongodb://mongodb:27017


# EMAIL
MAIL_HOST=smtp.elasticemail.com
MAIL_PORT=2525
MAIL_USERNAME=shovon2464@gmail.com
MAIL_PASSWORD=secretkkey

# FINHUB
FINHUB_API=secretkey