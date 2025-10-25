#!/bin/bash

# Room Sensors Microservice System Stop Script

echo "Stopping Room Sensors Microservice System..."

# Function to kill process by PID
kill_process() {
    local pid_file=$1
    local service_name=$2
    
    if [ -f "$pid_file" ]; then
        local pid=$(cat "$pid_file")
        if kill -0 "$pid" 2>/dev/null; then
            echo "Stopping $service_name (PID: $pid)..."
            kill "$pid"
            sleep 2
            if kill -0 "$pid" 2>/dev/null; then
                echo "Force killing $service_name..."
                kill -9 "$pid"
            fi
            echo "$service_name stopped"
        else
            echo "$service_name was not running"
        fi
        rm -f "$pid_file"
    else
        echo "No PID file found for $service_name"
    fi
}

# Create logs directory if it doesn't exist
mkdir -p logs

# Stop services in reverse order
kill_process "logs/service2.pid" "Room Sensors Service 2"
kill_process "logs/service1.pid" "Room Sensors Service 1"
kill_process "logs/gateway.pid" "API Gateway"
kill_process "logs/config.pid" "Config Server"
kill_process "logs/eureka.pid" "Eureka Server"

echo ""
echo "All services stopped successfully!"
echo "Log files are available in the logs/ directory"
