# Room Sensors Microservice Architecture

## System Architecture Overview

This document describes the architecture of the Room Sensors microservice system, a highly available, distributed system for collecting, storing, and displaying sensor data.

## Architecture Components

### 1. Service Registry (Eureka Server)
- **Port**: 8761
- **Purpose**: Service discovery and registration
- **Features**: 
  - Automatic service registration
  - Health monitoring
  - Load balancing support

### 2. Configuration Server
- **Port**: 8888
- **Purpose**: Centralized configuration management
- **Features**:
  - Environment-specific configurations
  - Dynamic configuration updates
  - Git-based configuration storage

### 3. API Gateway (Spring Cloud Gateway)
- **Port**: 8081
- **Purpose**: Single entry point for all client requests
- **Features**:
  - Request routing
  - Load balancing
  - CORS handling
  - Rate limiting (configurable)

### 4. Room Sensors Service (Multiple Instances)
- **Ports**: 8080, 8082 (and more for scaling)
- **Purpose**: Core business logic for sensor data management
- **Features**:
  - RESTful API for sensor management
  - Data persistence with JPA/Hibernate
  - Health monitoring
  - Metrics collection

## Data Flow Architecture

```
Client Request
     ↓
API Gateway (Load Balancer)
     ↓
Service Registry (Eureka)
     ↓
Room Sensors Service Instance
     ↓
Database (H2/PostgreSQL)
```

## High Availability Features

### 1. Service Discovery
- All services register with Eureka Server
- Automatic health checks and service removal
- Dynamic service discovery

### 2. Load Balancing
- API Gateway distributes requests across service instances
- Round-robin load balancing by default
- Health-aware load balancing

### 3. Configuration Management
- Centralized configuration via Config Server
- Environment-specific settings
- Dynamic configuration updates without restarts

### 4. Health Monitoring
- Spring Boot Actuator endpoints
- Health indicators for all services
- Metrics collection and monitoring

## Database Architecture

### Development (H2)
- In-memory database for development
- H2 Console for database inspection
- Auto-created tables on startup

### Production (PostgreSQL)
- Persistent database for production
- Connection pooling
- Transaction management
- Backup and recovery strategies

## API Design

### RESTful Endpoints

#### Sensor Management
- `GET /api/sensors` - Get all sensors
- `GET /api/sensors/{id}` - Get sensor by ID
- `POST /api/sensors` - Create new sensor
- `PUT /api/sensors/{id}` - Update sensor
- `DELETE /api/sensors/{id}` - Delete sensor

#### Sensor Readings
- `GET /api/readings` - Get all readings
- `GET /api/readings/sensor/{sensorId}` - Get readings by sensor
- `POST /api/readings` - Create new reading
- `GET /api/readings/sensor/{sensorId}/averages` - Get average readings

### Data Models

#### Sensor Entity
```java
- id: Long (Primary Key)
- sensorId: String (Unique)
- name: String
- location: String
- status: SensorStatus (ACTIVE, INACTIVE, MAINTENANCE, ERROR)
- createdAt: LocalDateTime
- updatedAt: LocalDateTime
```

#### SensorReading Entity
```java
- id: Long (Primary Key)
- sensor: Sensor (Foreign Key)
- temperature: BigDecimal
- humidity: BigDecimal
- timestamp: LocalDateTime
- createdAt: LocalDateTime
```

## Security Considerations

### Input Validation
- Bean validation on all DTOs
- SQL injection prevention via JPA
- Input sanitization

### CORS Configuration
- Configured for API Gateway
- Allowed origins, methods, and headers
- Preflight request handling

### Health Endpoints
- Protected health check endpoints
- Metrics collection
- Service status monitoring

## Deployment Architecture

### Docker Containerization
- Each service containerized separately
- Docker Compose for orchestration
- Environment-specific configurations

### Scaling Strategy
- Horizontal scaling of service instances
- Load balancer distribution
- Database connection pooling

### Monitoring and Logging
- Centralized logging
- Health check endpoints
- Metrics collection
- Performance monitoring

## Development Workflow

### Local Development
1. Start Eureka Server
2. Start Config Server
3. Start API Gateway
4. Start multiple service instances
5. Test load balancing and failover

### Testing Strategy
- Unit tests for business logic
- Integration tests for API endpoints
- Load testing for performance
- Health check testing

## Performance Considerations

### Database Optimization
- JPA query optimization
- Connection pooling
- Indexing strategies

### Caching
- Service-level caching
- Database query caching
- Response caching

### Load Balancing
- Round-robin distribution
- Health-aware routing
- Circuit breaker patterns

## Troubleshooting Guide

### Common Issues

1. **Service Registration Issues**
   - Check Eureka Server connectivity
   - Verify service configuration
   - Check network connectivity

2. **Load Balancing Issues**
   - Verify multiple service instances
   - Check API Gateway configuration
   - Monitor service health

3. **Database Connection Issues**
   - Check database configuration
   - Verify connection pool settings
   - Check database accessibility

### Monitoring Commands

```bash
# Check service health
curl http://localhost:8080/actuator/health

# Check Eureka registry
curl http://localhost:8761/eureka/apps

# Check API Gateway routes
curl http://localhost:8081/actuator/gateway/routes
```

## Future Enhancements

### Planned Features
- Circuit breaker implementation
- Distributed tracing
- Advanced monitoring
- Security enhancements
- Performance optimizations

### Scalability Improvements
- Database sharding
- Message queue integration
- Event-driven architecture
- Microservice communication patterns

## Conclusion

This architecture provides a robust, scalable, and maintainable solution for sensor data management with high availability, load balancing, and centralized configuration management. The microservice approach allows for independent scaling and deployment of components while maintaining system reliability and performance.
