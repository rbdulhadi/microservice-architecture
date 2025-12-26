# Implementation Summary

## ✅ Completed Features

### 1. Core Entities (30% - Persistent Storage)
- ✅ **Sensor Entity**: Name, Location, Active (Boolean), Type (OUTDOOR, INDOOR, WATER)
- ✅ **Measurement Entity**: Sensor (FK), Timestamp, Temperature, Humidity
- ✅ **User Entity**: Username, Email, Password, Role (READ_ONLY, READ_WRITE)
- ✅ Database schema with proper relationships and constraints

### 2. RESTful Web Service (30% - CRUD Operations)
- ✅ **Sensor Controller**: Full CRUD operations
  - GET /api/sensors - Get all sensors
  - GET /api/sensors/{id} - Get sensor by ID
  - POST /api/sensors - Create sensor
  - PUT /api/sensors/{id} - Update sensor
  - DELETE /api/sensors/{id} - Delete sensor
  - GET /api/sensors/type/{type} - Get sensors by type
  - GET /api/sensors/active - Get active sensors

- ✅ **Measurement Controller**: Full CRUD operations
  - GET /api/measurements - Get all measurements
  - GET /api/measurements/{id} - Get measurement by ID
  - POST /api/measurements - Create measurement
  - GET /api/measurements/sensor/{sensorId} - Get measurements by sensor
  - GET /api/measurements/sensor/{sensorId}/range - Get measurements by time range
  - GET /api/measurements/sensor/{sensorId}/averages - Get average measurements
  - DELETE /api/measurements/{id} - Delete measurement

### 3. Swagger UI (10%)
- ✅ OpenAPI 3.0 configuration
- ✅ Swagger UI accessible at /swagger-ui.html
- ✅ API documentation with annotations
- ✅ JWT authentication support in Swagger UI

### 4. User Account Management (20%)
- ✅ User registration endpoint (POST /api/users/register)
- ✅ User CRUD operations (GET, PUT, DELETE)
- ✅ User authentication with JWT tokens
- ✅ Password encryption with BCrypt

### 5. User Permissions (10%)
- ✅ **READ_ONLY Role**: Can read sensors and measurements (GET operations)
- ✅ **READ_WRITE Role**: Can read and write sensors and measurements (GET, POST, PUT, DELETE)
- ✅ Method-level security with @PreAuthorize annotations
- ✅ JWT-based authentication and authorization

## Architecture Components

- ✅ API Gateway (Spring Cloud Gateway) - Port 8081
- ✅ Service Registry (Eureka Server) - Port 8761
- ✅ Config Server - Port 8888
- ✅ Two instances of sensor service - Ports 8080 and 8082
- ✅ Load balancing through API Gateway

## Test Commands

### 1. Register a User (READ_WRITE)
```bash
curl -X POST http://localhost:8081/api/users/register \
  -H "Content-Type: application/json" \
  -d '{
    "username": "admin",
    "email": "admin@example.com",
    "password": "admin123",
    "role": "READ_WRITE"
  }'
```

### 2. Register a User (READ_ONLY)
```bash
curl -X POST http://localhost:8081/api/users/register \
  -H "Content-Type: application/json" \
  -d '{
    "username": "reader",
    "email": "reader@example.com",
    "password": "reader123",
    "role": "READ_ONLY"
  }'
```

### 3. Login and Get JWT Token
```bash
curl -X POST http://localhost:8081/api/auth/login \
  -H "Content-Type: application/json" \
  -d '{
    "username": "admin",
    "password": "admin123"
  }'
```

Response will contain a JWT token. Save it for subsequent requests.

### 4. Create a Sensor (Requires READ_WRITE)
```bash
curl -X POST http://localhost:8081/api/sensors \
  -H "Content-Type: application/json" \
  -H "Authorization: Bearer YOUR_JWT_TOKEN" \
  -d '{
    "name": "Living Room Sensor",
    "location": "Living Room",
    "active": true,
    "type": "INDOOR"
  }'
```

### 5. Get All Sensors (Requires READ_ONLY or READ_WRITE)
```bash
curl -X GET http://localhost:8081/api/sensors \
  -H "Authorization: Bearer YOUR_JWT_TOKEN"
```

### 6. Get Sensor by ID
```bash
curl -X GET http://localhost:8081/api/sensors/1 \
  -H "Authorization: Bearer YOUR_JWT_TOKEN"
```

### 7. Update Sensor (Requires READ_WRITE)
```bash
curl -X PUT http://localhost:8081/api/sensors/1 \
  -H "Content-Type: application/json" \
  -H "Authorization: Bearer YOUR_JWT_TOKEN" \
  -d '{
    "name": "Updated Living Room Sensor",
    "location": "Living Room",
    "active": false,
    "type": "INDOOR"
  }'
```

### 8. Delete Sensor (Requires READ_WRITE)
```bash
curl -X DELETE http://localhost:8081/api/sensors/1 \
  -H "Authorization: Bearer YOUR_JWT_TOKEN"
```

### 9. Create a Measurement (Requires READ_WRITE)
```bash
curl -X POST http://localhost:8081/api/measurements \
  -H "Content-Type: application/json" \
  -H "Authorization: Bearer YOUR_JWT_TOKEN" \
  -d '{
    "sensorId": 1,
    "timestamp": "2024-01-15T10:30:00",
    "temperature": 22.5,
    "humidity": 45.2
  }'
```

### 10. Get Measurements by Sensor
```bash
curl -X GET http://localhost:8081/api/measurements/sensor/1 \
  -H "Authorization: Bearer YOUR_JWT_TOKEN"
```

### 11. Get Measurements by Time Range
```bash
curl -X GET "http://localhost:8081/api/measurements/sensor/1/range?start=2024-01-15T00:00:00&end=2024-01-15T23:59:59" \
  -H "Authorization: Bearer YOUR_JWT_TOKEN"
```

### 12. Get Average Measurements
```bash
curl -X GET "http://localhost:8081/api/measurements/sensor/1/averages?start=2024-01-15T00:00:00&end=2024-01-15T23:59:59" \
  -H "Authorization: Bearer YOUR_JWT_TOKEN"
```

### 13. Get All Users (Requires READ_WRITE)
```bash
curl -X GET http://localhost:8081/api/users \
  -H "Authorization: Bearer YOUR_JWT_TOKEN"
```

## Testing Permissions

### Test READ_ONLY User (Should Fail on Write Operations)
```bash
# Login as READ_ONLY user
TOKEN=$(curl -s -X POST http://localhost:8081/api/auth/login \
  -H "Content-Type: application/json" \
  -d '{"username": "reader", "password": "reader123"}' | jq -r '.token')

# This should work (READ operation)
curl -X GET http://localhost:8081/api/sensors -H "Authorization: Bearer $TOKEN"

# This should fail with 403 Forbidden (WRITE operation)
curl -X POST http://localhost:8081/api/sensors \
  -H "Content-Type: application/json" \
  -H "Authorization: Bearer $TOKEN" \
  -d '{"name": "Test", "location": "Test", "active": true, "type": "INDOOR"}'
```

### Test READ_WRITE User (Should Work on All Operations)
```bash
# Login as READ_WRITE user
TOKEN=$(curl -s -X POST http://localhost:8081/api/auth/login \
  -H "Content-Type: application/json" \
  -d '{"username": "admin", "password": "admin123"}' | jq -r '.token')

# All operations should work
curl -X GET http://localhost:8081/api/sensors -H "Authorization: Bearer $TOKEN"
curl -X POST http://localhost:8081/api/sensors \
  -H "Content-Type: application/json" \
  -H "Authorization: Bearer $TOKEN" \
  -d '{"name": "Test", "location": "Test", "active": true, "type": "INDOOR"}'
```

## Access Points

- **Eureka Server**: http://localhost:8761
- **API Gateway**: http://localhost:8081
- **Swagger UI**: http://localhost:8081/swagger-ui.html
- **Service Instance 1**: http://localhost:8080
- **Service Instance 2**: http://localhost:8082
- **H2 Console**: http://localhost:8080/h2-console

## Database Schema

### Sensors Table
- id (BIGINT, PRIMARY KEY, AUTO_INCREMENT)
- name (VARCHAR, NOT NULL)
- location (VARCHAR, NOT NULL)
- active (BOOLEAN, NOT NULL)
- type (VARCHAR, NOT NULL) - ENUM: OUTDOOR, INDOOR, WATER
- created_at (TIMESTAMP)
- updated_at (TIMESTAMP)

### Measurements Table
- id (BIGINT, PRIMARY KEY, AUTO_INCREMENT)
- sensor_id (BIGINT, FOREIGN KEY -> sensors.id, NOT NULL)
- timestamp (TIMESTAMP, NOT NULL)
- temperature (DECIMAL(5,2), NOT NULL)
- humidity (DECIMAL(5,2), NOT NULL)
- created_at (TIMESTAMP)

### Users Table
- id (BIGINT, PRIMARY KEY, AUTO_INCREMENT)
- username (VARCHAR, UNIQUE, NOT NULL)
- email (VARCHAR, UNIQUE, NOT NULL)
- password (VARCHAR, NOT NULL) - BCrypt encrypted
- role (VARCHAR, NOT NULL) - ENUM: READ_ONLY, READ_WRITE
- created_at (TIMESTAMP)
- updated_at (TIMESTAMP)

## Notes

1. All endpoints except `/api/auth/login` and `/api/users/register` require JWT authentication
2. JWT tokens expire after 24 hours (86400000 milliseconds)
3. Passwords are encrypted using BCrypt
4. The system uses H2 in-memory database for development
5. For production, PostgreSQL can be configured by changing the datasource properties
6. Swagger UI supports JWT authentication - click "Authorize" button and enter "Bearer YOUR_TOKEN"

