package com.roomsensors.roomsensors.dto;

import jakarta.validation.constraints.NotNull;
import java.math.BigDecimal;
import java.time.LocalDateTime;

public class MeasurementDTO {
    
    private Long id;
    
    @NotNull(message = "Sensor ID is required")
    private Long sensorId;
    
    @NotNull(message = "Timestamp is required")
    private LocalDateTime timestamp;
    
    @NotNull(message = "Temperature is required")
    private BigDecimal temperature;
    
    @NotNull(message = "Humidity is required")
    private BigDecimal humidity;
    
    // Constructors
    public MeasurementDTO() {
    }
    
    public MeasurementDTO(Long sensorId, LocalDateTime timestamp, BigDecimal temperature, BigDecimal humidity) {
        this.sensorId = sensorId;
        this.timestamp = timestamp;
        this.temperature = temperature;
        this.humidity = humidity;
    }
    
    // Getters and Setters
    public Long getId() {
        return id;
    }
    
    public void setId(Long id) {
        this.id = id;
    }
    
    public Long getSensorId() {
        return sensorId;
    }
    
    public void setSensorId(Long sensorId) {
        this.sensorId = sensorId;
    }
    
    public LocalDateTime getTimestamp() {
        return timestamp;
    }
    
    public void setTimestamp(LocalDateTime timestamp) {
        this.timestamp = timestamp;
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
}

