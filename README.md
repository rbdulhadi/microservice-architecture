# Room Sensors Microservice System

A distributed microservice architecture for managing room sensors and their measurements, featuring JWT-based authentication, service discovery, and API gateway routing.

## System Overview

This system provides a RESTful API for managing sensors and collecting temperature and humidity measurements. The architecture implements a microservice pattern with service discovery, load balancing, centralized configuration, and JWT-based security.

## Architecture Components

### 1. Core Services

- **Room Sensors Service** (`sensor-service`): Main microservice for sensor and measurement management
  - Port: `8080`
  - Features: Sensor CRUD operations, measurement tracking, user management, JWT authentication

- **Eureka Server** (`eureka-server`): Service registry for instance discovery
  - Port: `8761`
  - Features: Service registration, health monitoring, load balancing

- **API Gateway** (`api-gateway`): Single entry point with routing and load balancing
  - Port: `8081`
  - Features: Request routing, load balancing, Swagger UI aggregation

- **Config Server** (`config-server`): Centralized configuration management
  - Features: Environment-specific configurations, dynamic updates

### 2. Key Features

- **JWT Authentication**: Secure API access with role-based authorization
- **Service Discovery**: Automatic service registration and discovery via Eureka
- **Load Balancing**: Distributed requests across multiple service instances
- **API Documentation**: OpenAPI 3.0 with Swagger UI
- **High Availability**: Multiple service instances with health monitoring

## Technology Stack

- **Java 21**
- **Spring Boot 3.2.5**
- **Spring Cloud 2023.0.0**
- **Spring Cloud Gateway**: API routing and load balancing
- **Netflix Eureka**: Service discovery
- **Spring Data JPA**: Data persistence
- **H2 Database**: Development database (file-based)
- **PostgreSQL**: Production database support
- **Spring Security**: Authentication and authorization
- **JWT (JJWT)**: Token-based authentication
- **SpringDoc OpenAPI**: API documentation
- **Maven**: Build and dependency management

## Project Structure

```
microservice-architecture/
├── sensor-service/              # Main business service
│   └── src/main/java/com/roomsensors/roomsensors/
│       ├── controller/          # REST API endpoints
│       ├── service/             # Business logic
│       ├── repository/          # Data access layer
│       ├── model/               # Domain entities (Sensor, Measurement, User, Role)
│       ├── dto/                 # Data transfer objects
│       ├── config/              # Security and OpenAPI configuration
│       ├── filter/              # JWT authentication filter
│       └── exception/           # Global exception handling
├── api-gateway/                 # API Gateway service
│   └── src/main/java/com/roomsensors/gateway/
├── eureka-server/               # Service registry
│   └── src/main/java/com/roomsensors/eureka/
├── config-server/               # Configuration server
│   └── src/main/java/com/roomsensors/config/
└── pom.xml                      # Parent POM
```

## Quick Start

### Prerequisites

- Java 21
- Maven 3.6+

### Running Locally

**Important**: Start services in the following order:

1. **Start Eureka Server:**
   ```bash
   cd eureka-server
   mvn spring-boot:run
   ```
   Wait for Eureka to start (usually takes 10-20 seconds)
   - Dashboard: http://localhost:8761

2. **Start Config Server** (if using):
   ```bash
   cd config-server
   mvn spring-boot:run
   ```

3. **Start API Gateway:**
   ```bash
   cd api-gateway
   mvn spring-boot:run
   ```
   - Gateway URL: http://localhost:8081

4. **Start Room Sensors Service:**
   ```bash
   cd sensor-service
   mvn spring-boot:run
   ```
   - Direct service URL: http://localhost:8080
   - Through gateway: http://localhost:8081/api/...

### Running Multiple Service Instances

To test load balancing, start multiple instances of the sensor-service:

```bash
# Terminal 1 - Instance 1 (default port 8080)
cd sensor-service
mvn spring-boot:run

# Terminal 2 - Instance 2 (port 8082)
cd sensor-service
mvn spring-boot:run -Dspring-boot.run.arguments="--server.port=8082"
```

## API Documentation

### Accessing Swagger UI

- **Through API Gateway**: http://localhost:8081/swagger-ui.html
- **Direct from Service**: http://localhost:8080/swagger-ui.html
- **API Docs JSON**: http://localhost:8081/v3/api-docs

### Authentication

Most endpoints require JWT authentication. First, register a user and then login to get a JWT token.

#### 1. Register User
```http
POST /api/users/register
Content-Type: application/json

{
  "username": "admin",
  "password": "password123"
}
```

#### 2. Login
```http
POST /api/auth/login
Content-Type: application/json

{
  "username": "admin",
  "password": "password123"
}
```

**Response:**
```json
{
  "token": "eyJhbGciOiJIUzUxMiJ9...",
  "username": "admin"
}
```

#### 3. Using the Token
Include the token in the Authorization header for protected endpoints:
```http
Authorization: Bearer eyJhbGciOiJIUzUxMiJ9...
```

### API Endpoints

#### Authentication Endpoints

**Login**
```http
POST /api/auth/login
Content-Type: application/json

{
  "username": "string",
  "password": "string"
}
```

#### User Management Endpoints

**Register User** (Public)
```http
POST /api/users/register
Content-Type: application/json

{
  "username": "string",
  "password": "string"
}
```

**Get All Users** (Requires: `ROLE_READ_WRITE`)
```http
GET /api/users
Authorization: Bearer {token}
```

**Get User by ID** (Requires: `ROLE_READ_WRITE`)
```http
GET /api/users/{id}
Authorization: Bearer {token}
```

**Update User Role** (Requires: `ROLE_READ_WRITE`)
```http
PUT /api/users/{id}/{role}
Authorization: Bearer {token}
```
Roles: `ROLE_READ_ONLY`, `ROLE_READ_WRITE`

**Delete User** (Requires: `ROLE_READ_WRITE`)
```http
DELETE /api/users/{id}
Authorization: Bearer {token}
```

#### Sensor Management Endpoints

**Get All Sensors** (Requires: `ROLE_READ_ONLY` or `ROLE_READ_WRITE`)
```http
GET /api/sensors
Authorization: Bearer {token}
```

**Get Sensor by ID** (Requires: `ROLE_READ_ONLY` or `ROLE_READ_WRITE`)
```http
GET /api/sensors/{id}
Authorization: Bearer {token}
```

**Create Sensor** (Requires: `ROLE_READ_WRITE`)
```http
POST /api/sensors
Content-Type: application/json
Authorization: Bearer {token}

{
  "name": "Living Room Sensor",
  "location": "Living Room",
  "active": true,
  "type": "INDOOR"
}
```

**Update Sensor** (Requires: `ROLE_READ_WRITE`)
```http
PUT /api/sensors/{id}
Content-Type: application/json
Authorization: Bearer {token}

{
  "name": "Updated Sensor Name",
  "location": "Updated Location",
  "active": true,
  "type": "OUTDOOR"
}
```

**Delete Sensor** (Requires: `ROLE_READ_WRITE`)
```http
DELETE /api/sensors/{id}
Authorization: Bearer {token}
```

#### Measurement Endpoints

**Get All Measurements** (Requires: `ROLE_READ_ONLY` or `ROLE_READ_WRITE`)
```http
GET /api/measurements
Authorization: Bearer {token}
```

**Get Measurement by ID** (Requires: `ROLE_READ_ONLY` or `ROLE_READ_WRITE`)
```http
GET /api/measurements/{id}
Authorization: Bearer {token}
```

**Create Measurement** (Requires: `ROLE_READ_WRITE`)
```http
POST /api/measurements
Content-Type: application/json
Authorization: Bearer {token}

{
  "sensorId": 1,
  "timestamp": "2024-01-15T10:30:00",
  "temperature": 22.5,
  "humidity": 45.2
}
```

**Get Measurements by Sensor** (Requires: `ROLE_READ_ONLY` or `ROLE_READ_WRITE`)
```http
GET /api/measurements/sensor/{sensorId}
Authorization: Bearer {token}
```

**Delete Measurement** (Requires: `ROLE_READ_WRITE`)
```http
DELETE /api/measurements/{id}
Authorization: Bearer {token}
```

### Data Models

#### Sensor
- `id` (Long): Auto-generated ID
- `name` (String): Sensor name
- `location` (String): Sensor location
- `active` (Boolean): Active status
- `type` (SensorType): Sensor type enum (`OUTDOOR`, `INDOOR`, `WATER`)

#### Measurement
- `id` (Long): Auto-generated ID
- `sensorId` (Long): Reference to sensor
- `timestamp` (LocalDateTime): Measurement timestamp
- `temperature` (BigDecimal): Temperature reading
- `humidity` (BigDecimal): Humidity reading

#### User
- `id` (Long): Auto-generated ID
- `username` (String): Unique username
- `password` (String): Encrypted password
- `role` (Role): User role (`ROLE_READ_ONLY`, `ROLE_READ_WRITE`)

## Security

### Authentication Flow

1. User registers or logs in via `/api/auth/login`
2. System returns a JWT token
3. Client includes token in `Authorization: Bearer {token}` header
4. JWT filter validates token on each request
5. Spring Security enforces role-based access control

### Role-Based Access Control

- **ROLE_READ_ONLY**: Can read sensors and measurements
- **ROLE_READ_WRITE**: Can read and modify all resources, manage users

### Public Endpoints

- `POST /api/auth/login` - User login
- `POST /api/users/register` - User registration
- `GET /swagger-ui/**` - Swagger UI
- `GET /v3/api-docs/**` - API documentation
- `GET /webjars/**` - Static resources

## Database Configuration

### Development (H2)

The service uses an H2 file-based database for development:
- Database file: `/data/demo`
- H2 Console: http://localhost:8080/h2-console
  - JDBC URL: `jdbc:h2:file:/data/demo`
  - Username: `sa`
  - Password: (empty)

### Production (PostgreSQL)

To use PostgreSQL in production, update `application.properties`:
```properties
spring.datasource.url=jdbc:postgresql://localhost:5432/roomsensors
spring.datasource.username=your_username
spring.datasource.password=your_password
spring.jpa.database-platform=org.hibernate.dialect.PostgreSQLDialect
```

## Service Discovery & Load Balancing

### Eureka Dashboard

Access the Eureka dashboard at http://localhost:8761 to:
- View registered services
- Monitor service health
- See service instances and their status

### Load Balancing

The API Gateway automatically distributes requests across multiple service instances using round-robin load balancing. Start multiple instances of `sensor-service` to see load balancing in action.

## Monitoring & Health Checks

### Actuator Endpoints

- **Health**: http://localhost:8080/actuator/health
- **Metrics**: http://localhost:8080/actuator/metrics
- **Info**: http://localhost:8080/actuator/info

### Service Health

All services register with Eureka and report their health status. Unhealthy instances are automatically removed from the load balancer.

## Configuration

### Service Ports

- Eureka Server: `8761`
- API Gateway: `8081`
- Sensor Service: `8080` (default), can run on other ports
- Config Server: (configured in config-server)

### JWT Configuration

JWT settings are configured in `sensor-service/src/main/resources/application.properties`:
```properties
jwt.secret=YourSecretKeyHere
jwt.expiration=86400000  # 24 hours in milliseconds
```

**Important**: Change the JWT secret in production!

## Troubleshooting

### Common Issues

1. **Services not registering with Eureka**
   - Ensure Eureka Server is running first
   - Check `eureka.client.service-url.defaultZone` in application.properties
   - Verify network connectivity

2. **Swagger UI shows "No operations defined"**
   - Ensure sensor-service is running and registered with Eureka
   - Check that `/v3/api-docs` is accessible through the gateway
   - Verify security configuration allows Swagger endpoints

3. **JWT Authentication fails**
   - Verify token is included in `Authorization: Bearer {token}` header
   - Check token expiration
   - Ensure JWT secret matches between requests

4. **Database connection issues**
   - For H2: Ensure write permissions for `/data/demo` directory
   - For PostgreSQL: Verify database is running and credentials are correct

5. **Gateway routing issues**
   - Verify service is registered in Eureka
   - Check gateway route configuration
   - Ensure service name matches exactly (case-sensitive)

### Logs

View service logs:
```bash
# Sensor Service logs
tail -f sensor-service/logs/application.log

# Gateway logs
tail -f api-gateway/logs/application.log
```

## Development

### Building the Project

```bash
# Build all modules
mvn clean install

# Build specific module
cd sensor-service
mvn clean install
```

### Running Tests

```bash
# Run all tests
mvn test

# Run tests for specific module
cd sensor-service
mvn test
```

## License

This project is licensed under the MIT License - see the LICENSE.md file for details.
