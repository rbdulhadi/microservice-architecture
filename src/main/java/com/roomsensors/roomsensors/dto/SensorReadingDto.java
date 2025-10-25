package com.roomsensors.roomsensors.dto;

import jakarta.validation.constraints.NotNull;
import java.math.BigDecimal;
import java.time.LocalDateTime;

public class SensorReadingDto {
    
    private Long id;
    
    @NotNull(message = "Sensor ID is required")
    private String sensorId;
    
    @NotNull(message = "Temperature is required")
    private BigDecimal temperature;
    
    @NotNull(message = "Humidity is required")
    private BigDecimal humidity;
    
    @NotNull(message = "Timestamp is required")
    private LocalDateTime timestamp;
    
    private LocalDateTime createdAt;
    
    // Constructors
    public SensorReadingDto() {}
    
    public SensorReadingDto(String sensorId, BigDecimal temperature, BigDecimal humidity, LocalDateTime timestamp) {
        this.sensorId = sensorId;
        this.temperature = temperature;
        this.humidity = humidity;
        this.timestamp = timestamp;
    }
    
    // Getters and Setters
    public Long getId() {
        return id;
    }
    
    public void setId(Long id) {
        this.id = id;
    }
    
    public String getSensorId() {
        return sensorId;
    }
    
    public void setSensorId(String sensorId) {
        this.sensorId = sensorId;
    }
    
    public BigDecimal getTemperature() {
        return temperature;
    }
    
    public void setTemperature(BigDecimal temperature) {
        this.temperature = temperature;
    }
    
    public BigDecimal getHumidity() {
        return humidity;
    }
    
    public void setHumidity(BigDecimal humidity) {
        this.humidity = humidity;
    }
    
    public LocalDateTime getTimestamp() {
        return timestamp;
    }
    
    public void setTimestamp(LocalDateTime timestamp) {
        this.timestamp = timestamp;
    }
    
    public LocalDateTime getCreatedAt() {
        return createdAt;
    }
    
    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }
    
    @Override
    public String toString() {
        return "SensorReadingDto{" +
                "id=" + id +
                ", sensorId='" + sensorId + '\'' +
                ", temperature=" + temperature +
                ", humidity=" + humidity +
                ", timestamp=" + timestamp +
                ", createdAt=" + createdAt +
                '}';
    }
}
