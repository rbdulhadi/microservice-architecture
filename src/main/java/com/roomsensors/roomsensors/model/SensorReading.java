package com.roomsensors.roomsensors.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Table(name = "sensor_readings")
public class SensorReading {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @NotNull(message = "Sensor is required")
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "sensor_id", nullable = false)
    private Sensor sensor;
    
    @NotNull(message = "Temperature is required")
    @Column(nullable = false, precision = 5, scale = 2)
    private BigDecimal temperature;
    
    @NotNull(message = "Humidity is required")
    @Column(nullable = false, precision = 5, scale = 2)
    private BigDecimal humidity;
    
    @NotNull(message = "Timestamp is required")
    @Column(nullable = false)
    private LocalDateTime timestamp;
    
    @Column(name = "created_at")
    private LocalDateTime createdAt;
    
    // Constructors
    public SensorReading() {
        this.createdAt = LocalDateTime.now();
    }
    
    public SensorReading(Sensor sensor, BigDecimal temperature, BigDecimal humidity, LocalDateTime timestamp) {
        this();
        this.sensor = sensor;
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
    
    public Sensor getSensor() {
        return sensor;
    }
    
    public void setSensor(Sensor sensor) {
        this.sensor = sensor;
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
        return "SensorReading{" +
                "id=" + id +
                ", sensor=" + (sensor != null ? sensor.getSensorId() : "null") +
                ", temperature=" + temperature +
                ", humidity=" + humidity +
                ", timestamp=" + timestamp +
                ", createdAt=" + createdAt +
                '}';
    }
}
