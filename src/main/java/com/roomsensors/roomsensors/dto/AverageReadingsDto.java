package com.roomsensors.roomsensors.dto;

import java.time.LocalDateTime;

public class AverageReadingsDto {
    
    private String sensorId;
    private Double averageTemperature;
    private Double averageHumidity;
    private LocalDateTime startTime;
    private LocalDateTime endTime;
    
    // Constructors
    public AverageReadingsDto() {}
    
    public AverageReadingsDto(String sensorId, Double averageTemperature, Double averageHumidity, 
                            LocalDateTime startTime, LocalDateTime endTime) {
        this.sensorId = sensorId;
        this.averageTemperature = averageTemperature;
        this.averageHumidity = averageHumidity;
        this.startTime = startTime;
        this.endTime = endTime;
    }
    
    // Getters and Setters
    public String getSensorId() {
        return sensorId;
    }
    
    public void setSensorId(String sensorId) {
        this.sensorId = sensorId;
    }
    
    public Double getAverageTemperature() {
        return averageTemperature;
    }
    
    public void setAverageTemperature(Double averageTemperature) {
        this.averageTemperature = averageTemperature;
    }
    
    public Double getAverageHumidity() {
        return averageHumidity;
    }
    
    public void setAverageHumidity(Double averageHumidity) {
        this.averageHumidity = averageHumidity;
    }
    
    public LocalDateTime getStartTime() {
        return startTime;
    }
    
    public void setStartTime(LocalDateTime startTime) {
        this.startTime = startTime;
    }
    
    public LocalDateTime getEndTime() {
        return endTime;
    }
    
    public void setEndTime(LocalDateTime endTime) {
        this.endTime = endTime;
    }
    
    @Override
    public String toString() {
        return "AverageReadingsDto{" +
                "sensorId='" + sensorId + '\'' +
                ", averageTemperature=" + averageTemperature +
                ", averageHumidity=" + averageHumidity +
                ", startTime=" + startTime +
                ", endTime=" + endTime +
                '}';
    }
}
