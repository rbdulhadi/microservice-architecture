package com.roomsensors.roomsensors.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Table(name = "measurements")
public class Measurement {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @NotNull(message = "Sensor is required")
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "sensor_id", nullable = false)
    private Sensor sensor;
    
    @NotNull(message = "Timestamp is required")
    @Column(nullable = false)
    private LocalDateTime timestamp;
    
    @NotNull(message = "Temperature is required")
    @Column(nullable = false, precision = 5, scale = 2)
    private BigDecimal temperature;
    
    @NotNull(message = "Humidity is required")
    @Column(nullable = false, precision = 5, scale = 2)
    private BigDecimal humidity;
    
    @Column(name = "created_at", updatable = false)
    private LocalDateTime createdAt;
    
    @PrePersist
    protected void onCreate() {
        if (createdAt == null) {
            createdAt = LocalDateTime.now();
        }
        if (timestamp == null) {
            timestamp = LocalDateTime.now();
        }
    }
    
    // Constructors
    public Measurement() {
    }
    
    public Measurement(Sensor sensor, LocalDateTime timestamp, BigDecimal temperature, BigDecimal humidity) {
        this.sensor = sensor;
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
    
    public Sensor getSensor() {
        return sensor;
    }
    
    public void setSensor(Sensor sensor) {
        this.sensor = sensor;
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
    
    public LocalDateTime getCreatedAt() {
        return createdAt;
    }
    
    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }
}

