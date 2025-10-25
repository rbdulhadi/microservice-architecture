# Room Sensors Microservice System

A highly available, distributed system for collecting, storing, and displaying sensor data using a microservice architecture.

## System Overview

This system manages data from multiple combined temperature and humidity sensors, providing a RESTful API for data storage and retrieval. The architecture implements high availability with multiple service instances, service discovery, load balancing, and centralized configuration.

## Architecture Components

### 1. Core Services
- **Room Sensors Service**: Main microservice for sensor data management
- **Eureka Server**: Service registry for instance discovery
- **API Gateway**: Single entry point with load balancing
- **Config Server**: Centralized configuration management

### 2. High Availability Features
- Multiple instances of the core service
- Service discovery and registration
- Load balancing across instances
- Centralized configuration management
- Health monitoring and metrics

## Technology Stack

- **Java 21**
- **Spring Boot 3.5.7**
- **Spring Cloud 2023.0.0**
- **Spring Data JPA**
- **H2 Database** (development)
- **PostgreSQL** (production)
- **Docker & Docker Compose**
- **Swagger/OpenAPI 3**

## Project Structure

```
roomSensors/
├── src/main/java/com/roomsensors/roomsensors/
│   ├── model/                 # Domain entities
│   ├── repository/           # Data access layer
│   ├── service/              # Business logic
│   ├── controller/           # REST API endpoints
│   └── dto/                  # Data transfer objects
├── eureka-server/            # Service registry
├── api-gateway/              # API Gateway
├── config-server/            # Configuration server
├── docker-compose.yml        # Container orchestration
└── Dockerfile               # Container definition
```

## Quick Start

### Prerequisites
- Java 21
- Maven 3.6+
- Docker & Docker Compose

### Running with Docker Compose

1. **Start all services:**
   ```bash
   docker-compose up -d
   ```

2. **Access the services:**
   - Eureka Server: http://localhost:8761
   - API Gateway: http://localhost:8081
   - Swagger UI: http://localhost:8081/swagger-ui.html
   - H2 Console: http://localhost:8080/h2-console

### Running Locally

1. **Start Eureka Server:**
   ```bash
   cd eureka-server
   mvn spring-boot:run
   ```

2. **Start Config Server:**
   ```bash
   cd config-server
   mvn spring-boot:run
   ```

3. **Start API Gateway:**
   ```bash
   cd api-gateway
   mvn spring-boot:run
   ```

4. **Start Room Sensors Service (multiple instances):**
   ```bash
   # Instance 1
   mvn spring-boot:run -Dspring-boot.run.arguments="--server.port=8080"
   
   # Instance 2
   mvn spring-boot:run -Dspring-boot.run.arguments="--server.port=8082"
   ```

## API Documentation

### Sensor Management

#### Create Sensor
```http
POST /api/sensors
Content-Type: application/json

{
  "sensorId": "SENSOR_001",
  "name": "Living Room Sensor",
  "location": "Living Room",
  "status": "ACTIVE"
}
```

#### Get All Sensors
```http
GET /api/sensors
```

#### Get Sensor by ID
```http
GET /api/sensors/{id}
```

#### Update Sensor Status
```http
PATCH /api/sensors/{sensorId}/status?status=INACTIVE
```

### Sensor Readings

#### Create Reading
```http
POST /api/readings
Content-Type: application/json

{
  "sensorId": "SENSOR_001",
  "temperature": 22.5,
  "humidity": 45.2,
  "timestamp": "2024-01-15T10:30:00"
}
```

#### Get Readings by Sensor
```http
GET /api/readings/sensor/{sensorId}
```

#### Get Readings by Time Range
```http
GET /api/readings/sensor/{sensorId}/range?start=2024-01-15T00:00:00&end=2024-01-15T23:59:59
```

#### Get Average Readings
```http
GET /api/readings/sensor/{sensorId}/averages?start=2024-01-15T00:00:00&end=2024-01-15T23:59:59
```

## High Availability Features

### Service Discovery
- All services register with Eureka Server
- Automatic service discovery and health monitoring
- Load balancing across multiple instances

### Load Balancing
- API Gateway distributes requests across service instances
- Round-robin load balancing by default
- Health check integration

### Configuration Management
- Centralized configuration via Config Server
- Environment-specific configurations
- Dynamic configuration updates

### Monitoring & Health Checks
- Spring Boot Actuator endpoints
- Health indicators for all services
- Metrics collection and monitoring

## Database Configuration

### Development (H2)
- In-memory database for development
- H2 Console available at `/h2-console`
- Auto-created tables on startup

### Production (PostgreSQL)
- Persistent database for production
- Connection pooling
- Transaction management

## Security Considerations

- Input validation on all endpoints
- SQL injection prevention via JPA
- CORS configuration for API Gateway
- Health check endpoints for monitoring

## Testing

### Unit Tests
```bash
mvn test
```

### Integration Tests
```bash
mvn verify
```

### Load Testing
Use tools like Apache JMeter or Artillery to test the load balancing and high availability features.

## Monitoring & Observability

### Health Endpoints
- `/actuator/health` - Service health status
- `/actuator/metrics` - Application metrics
- `/actuator/info` - Application information

### Service Discovery
- Eureka Dashboard: http://localhost:8761
- View registered services and their status

## Deployment

### Docker Deployment
```bash
# Build and start all services
docker-compose up -d

# Scale the room-sensors service
docker-compose up -d --scale room-sensors-service-1=2 --scale room-sensors-service-2=2
```

### Production Considerations
- Use external databases (PostgreSQL)
- Configure proper logging
- Set up monitoring and alerting
- Implement security measures
- Configure backup strategies

## Troubleshooting

### Common Issues

1. **Service Registration Issues**
   - Check Eureka Server is running
   - Verify network connectivity
   - Check service configuration

2. **Load Balancing Issues**
   - Verify multiple service instances are running
   - Check API Gateway configuration
   - Monitor service health

3. **Database Connection Issues**
   - Check database configuration
   - Verify database is accessible
   - Check connection pool settings

### Logs
```bash
# View service logs
docker-compose logs -f room-sensors-service-1
docker-compose logs -f api-gateway
docker-compose logs -f eureka-server
```

## Contributing

1. Fork the repository
2. Create a feature branch
3. Make your changes
4. Add tests
5. Submit a pull request

## License

This project is licensed under the MIT License - see the LICENSE.txt file for details.