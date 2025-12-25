#!/bin/bash

# Room Sensors Microservice System Startup Script

echo "Starting Room Sensors Microservice System..."

# Function to check if a port is available
check_port() {
    local port=$1
    if lsof -Pi :$port -sTCP:LISTEN -t >/dev/null ; then
        echo "Port $port is already in use"
        return 1
    else
        echo "Port $port is available"
        return 0
    fi
}

# Function to wait for service to be ready
wait_for_service() {
    local url=$1
    local service_name=$2
    local max_attempts=30
    local attempt=1
    
    echo "Waiting for $service_name to be ready..."
    while [ $attempt -le $max_attempts ]; do
        if curl -s "$url" > /dev/null 2>&1; then
            echo "$service_name is ready!"
            return 0
        fi
        echo "Attempt $attempt/$max_attempts: $service_name not ready yet..."
        sleep 2
        attempt=$((attempt + 1))
    done
    
    echo "$service_name failed to start within expected time"
    return 1
}

# Start Eureka Server
echo "Starting Eureka Server..."
cd eureka-server
mvn spring-boot:run > ../logs/eureka-server.log 2>&1 &
EUREKA_PID=$!
cd ..

# Wait for Eureka Server
wait_for_service "http://localhost:8761" "Eureka Server"

# Start Config Server
echo "Starting Config Server..."
cd config-server
mvn spring-boot:run > ../logs/config-server.log 2>&1 &
CONFIG_PID=$!
cd ..

# Wait for Config Server
wait_for_service "http://localhost:8888" "Config Server"

# Start API Gateway
echo "Starting API Gateway..."
cd api-gateway
mvn spring-boot:run > ../logs/api-gateway.log 2>&1 &
GATEWAY_PID=$!
cd ..

# Wait for API Gateway
wait_for_service "http://localhost:8081" "API Gateway"

# Start Room Sensors Service Instance 1
echo "Starting Room Sensors Service Instance 1..."
mvn spring-boot:run -Dspring-boot.run.arguments="--server.port=8080" > logs/room-sensors-1.log 2>&1 &
SERVICE1_PID=$!

# Start Room Sensors Service Instance 2
echo "Starting Room Sensors Service Instance 2..."
mvn spring-boot:run -Dspring-boot.run.arguments="--server.port=8082" > logs/room-sensors-2.log 2>&1 &
SERVICE2_PID=$!

# Wait for services
wait_for_service "http://localhost:8080/actuator/health" "Room Sensors Service 1"
wait_for_service "http://localhost:8082/actuator/health" "Room Sensors Service 2"

echo ""
echo "=========================================="
echo "All services started successfully!"
echo "=========================================="
echo "Eureka Server: http://localhost:8761"
echo "API Gateway: http://localhost:8081"
echo "Swagger UI: http://localhost:8081/swagger-ui.html"
echo "Room Sensors Service 1: http://localhost:8080"
echo "Room Sensors Service 2: http://localhost:8082"
echo "H2 Console: http://localhost:8080/h2-console"
echo ""
echo "Process IDs:"
echo "Eureka Server: $EUREKA_PID"
echo "Config Server: $CONFIG_PID"
echo "API Gateway: $GATEWAY_PID"
echo "Room Sensors 1: $SERVICE1_PID"
echo "Room Sensors 2: $SERVICE2_PID"
echo ""
echo "To stop all services, run: ./stop-services.sh"
echo "To view logs, check the logs/ directory"
echo "=========================================="

# Save PIDs for stop script
echo "$EUREKA_PID" > logs/eureka.pid
echo "$CONFIG_PID" > logs/config.pid
echo "$GATEWAY_PID" > logs/gateway.pid
echo "$SERVICE1_PID" > logs/service1.pid
echo "$SERVICE2_PID" > logs/service2.pid
