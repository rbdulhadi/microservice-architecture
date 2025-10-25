package com.roomsensors.roomsensors.service;

import com.roomsensors.roomsensors.model.Sensor;
import com.roomsensors.roomsensors.model.SensorReading;
import com.roomsensors.roomsensors.repository.SensorReadingRepository;
import com.roomsensors.roomsensors.repository.SensorRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
@Transactional
public class SensorReadingService {
    
    @Autowired
    private SensorReadingRepository sensorReadingRepository;
    
    @Autowired
    private SensorRepository sensorRepository;
    
    public List<SensorReading> getAllReadings() {
        return sensorReadingRepository.findAll();
    }
    
    public Optional<SensorReading> getReadingById(Long id) {
        return sensorReadingRepository.findById(id);
    }
    
    public List<SensorReading> getReadingsBySensor(Sensor sensor) {
        return sensorReadingRepository.findBySensor(sensor);
    }
    
    public Page<SensorReading> getReadingsBySensor(Sensor sensor, Pageable pageable) {
        return sensorReadingRepository.findBySensor(sensor, pageable);
    }
    
    public List<SensorReading> getReadingsBySensorId(String sensorId) {
        return sensorReadingRepository.findBySensorIdOrderByTimestampDesc(sensorId);
    }
    
    public List<SensorReading> getReadingsBySensorIdAndTimestampBetween(String sensorId, LocalDateTime start, LocalDateTime end) {
        return sensorReadingRepository.findBySensorIdAndTimestampBetween(sensorId, start, end);
    }
    
    public List<SensorReading> getReadingsByLocation(String location) {
        return sensorReadingRepository.findByLocationOrderByTimestampDesc(location);
    }
    
    public List<SensorReading> getReadingsByLocationAndTimestampBetween(String location, LocalDateTime start, LocalDateTime end) {
        return sensorReadingRepository.findByLocationAndTimestampBetween(location, start, end);
    }
    
    public List<SensorReading> getReadingsBySensorAndTimestampBetween(Sensor sensor, LocalDateTime start, LocalDateTime end) {
        return sensorReadingRepository.findBySensorAndTimestampBetween(sensor, start, end);
    }
    
    public SensorReading createReading(SensorReading reading) {
        return sensorReadingRepository.save(reading);
    }
    
    public SensorReading createReading(String sensorId, BigDecimal temperature, BigDecimal humidity, LocalDateTime timestamp) {
        Sensor sensor = sensorRepository.findBySensorId(sensorId)
                .orElseThrow(() -> new RuntimeException("Sensor not found with sensorId: " + sensorId));
        
        SensorReading reading = new SensorReading(sensor, temperature, humidity, timestamp);
        return sensorReadingRepository.save(reading);
    }
    
    public SensorReading createReading(String sensorId, BigDecimal temperature, BigDecimal humidity) {
        return createReading(sensorId, temperature, humidity, LocalDateTime.now());
    }
    
    public Double getAverageTemperature(String sensorId, LocalDateTime start, LocalDateTime end) {
        return sensorReadingRepository.findAverageTemperatureBySensorIdAndTimestampBetween(sensorId, start, end);
    }
    
    public Double getAverageHumidity(String sensorId, LocalDateTime start, LocalDateTime end) {
        return sensorReadingRepository.findAverageHumidityBySensorIdAndTimestampBetween(sensorId, start, end);
    }
    
    public void deleteReading(Long id) {
        SensorReading reading = sensorReadingRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Reading not found with id: " + id));
        sensorReadingRepository.delete(reading);
    }
    
    public void deleteReadingsBySensor(Sensor sensor) {
        List<SensorReading> readings = sensorReadingRepository.findBySensor(sensor);
        sensorReadingRepository.deleteAll(readings);
    }
}
