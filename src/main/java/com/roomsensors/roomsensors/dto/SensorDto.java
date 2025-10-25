package com.roomsensors.roomsensors.dto;

import com.roomsensors.roomsensors.model.SensorStatus;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.time.LocalDateTime;

public class SensorDto {
    
    private Long id;
    
    @NotBlank(message = "Sensor ID is required")
    private String sensorId;
    
    @NotBlank(message = "Sensor name is required")
    private String name;
    
    @NotBlank(message = "Location is required")
    private String location;
    
    @NotNull(message = "Status is required")
    private SensorStatus status;
    
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    
    // Constructors
    public SensorDto() {}
    
    public SensorDto(String sensorId, String name, String location, SensorStatus status) {
        this.sensorId = sensorId;
        this.name = name;
        this.location = location;
        this.status = status;
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
    
    public String getName() {
        return name;
    }
    
    public void setName(String name) {
        this.name = name;
    }
    
    public String getLocation() {
        return location;
    }
    
    public void setLocation(String location) {
        this.location = location;
    }
    
    public SensorStatus getStatus() {
        return status;
    }
    
    public void setStatus(SensorStatus status) {
        this.status = status;
    }
    
    public LocalDateTime getCreatedAt() {
        return createdAt;
    }
    
    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }
    
    public LocalDateTime getUpdatedAt() {
        return updatedAt;
    }
    
    public void setUpdatedAt(LocalDateTime updatedAt) {
        this.updatedAt = updatedAt;
    }
    
    @Override
    public String toString() {
        return "SensorDto{" +
                "id=" + id +
                ", sensorId='" + sensorId + '\'' +
                ", name='" + name + '\'' +
                ", location='" + location + '\'' +
                ", status=" + status +
                ", createdAt=" + createdAt +
                ", updatedAt=" + updatedAt +
                '}';
    }
}
