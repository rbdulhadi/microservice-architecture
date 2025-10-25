@echo off
REM Room Sensors Microservice System Startup Script for Windows

echo Starting Room Sensors Microservice System...

REM Create logs directory
if not exist logs mkdir logs

REM Start Eureka Server
echo Starting Eureka Server...
cd eureka-server
start "Eureka Server" cmd /k "mvn spring-boot:run"
cd ..

REM Wait a bit for Eureka to start
timeout /t 10 /nobreak > nul

REM Start Config Server
echo Starting Config Server...
cd config-server
start "Config Server" cmd /k "mvn spring-boot:run"
cd ..

REM Wait a bit for Config Server to start
timeout /t 10 /nobreak > nul

REM Start API Gateway
echo Starting API Gateway...
cd api-gateway
start "API Gateway" cmd /k "mvn spring-boot:run"
cd ..

REM Wait a bit for API Gateway to start
timeout /t 10 /nobreak > nul

REM Start Room Sensors Service Instance 1
echo Starting Room Sensors Service Instance 1...
start "Room Sensors Service 1" cmd /k "mvn spring-boot:run -Dspring-boot.run.arguments=--server.port=8080"

REM Start Room Sensors Service Instance 2
echo Starting Room Sensors Service Instance 2...
start "Room Sensors Service Instance 2" cmd /k "mvn spring-boot:run -Dspring-boot.run.arguments=--server.port=8082"

echo.
echo ==========================================
echo All services are starting...
echo ==========================================
echo Eureka Server: http://localhost:8761
echo API Gateway: http://localhost:8081
echo Swagger UI: http://localhost:8081/swagger-ui.html
echo Room Sensors Service 1: http://localhost:8080
echo Room Sensors Service 2: http://localhost:8082
echo H2 Console: http://localhost:8080/h2-console
echo.
echo Services are starting in separate windows.
echo Close the windows to stop individual services.
echo ==========================================

pause
