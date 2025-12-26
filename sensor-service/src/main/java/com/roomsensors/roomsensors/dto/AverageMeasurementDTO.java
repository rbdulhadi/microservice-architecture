package com.roomsensors.roomsensors.dto;

import java.math.BigDecimal;

public class AverageMeasurementDTO {
    
    private Double averageTemperature;
    private Double averageHumidity;
    
    // Constructors
    public AverageMeasurementDTO() {
    }
    
    public AverageMeasurementDTO(Double averageTemperature, Double averageHumidity) {
        this.averageTemperature = averageTemperature;
        this.averageHumidity = averageHumidity;
    }
    
    // Getters and Setters
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
}

