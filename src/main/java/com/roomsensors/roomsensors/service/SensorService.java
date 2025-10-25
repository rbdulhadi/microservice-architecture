package com.roomsensors.roomsensors.service;

import com.roomsensors.roomsensors.model.Sensor;
import com.roomsensors.roomsensors.model.SensorStatus;
import com.roomsensors.roomsensors.repository.SensorRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@Transactional
public class SensorService {
    
    @Autowired
    private SensorRepository sensorRepository;
    
    public List<Sensor> getAllSensors() {
        return sensorRepository.findAll();
    }
    
    public Optional<Sensor> getSensorById(Long id) {
        return sensorRepository.findById(id);
    }
    
    public Optional<Sensor> getSensorBySensorId(String sensorId) {
        return sensorRepository.findBySensorId(sensorId);
    }
    
    public List<Sensor> getSensorsByStatus(SensorStatus status) {
        return sensorRepository.findByStatus(status);
    }
    
    public List<Sensor> getSensorsByLocation(String location) {
        return sensorRepository.findByLocation(location);
    }
    
    public List<Sensor> getSensorsByLocationAndStatus(String location, SensorStatus status) {
        return sensorRepository.findByLocationAndStatus(location, status);
    }
    
    public List<Sensor> getSensorsByNameContaining(String name) {
        return sensorRepository.findByNameContaining(name);
    }
    
    public Sensor createSensor(Sensor sensor) {
        if (sensorRepository.existsBySensorId(sensor.getSensorId())) {
            throw new IllegalArgumentException("Sensor with ID " + sensor.getSensorId() + " already exists");
        }
        return sensorRepository.save(sensor);
    }
    
    public Sensor updateSensor(Long id, Sensor sensorDetails) {
        Sensor sensor = sensorRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Sensor not found with id: " + id));
        
        sensor.setName(sensorDetails.getName());
        sensor.setLocation(sensorDetails.getLocation());
        sensor.setStatus(sensorDetails.getStatus());
        sensor.setUpdatedAt(java.time.LocalDateTime.now());
        
        return sensorRepository.save(sensor);
    }
    
    public Sensor updateSensorStatus(String sensorId, SensorStatus status) {
        Sensor sensor = sensorRepository.findBySensorId(sensorId)
                .orElseThrow(() -> new RuntimeException("Sensor not found with sensorId: " + sensorId));
        
        sensor.setStatus(status);
        sensor.setUpdatedAt(java.time.LocalDateTime.now());
        
        return sensorRepository.save(sensor);
    }
    
    public void deleteSensor(Long id) {
        Sensor sensor = sensorRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Sensor not found with id: " + id));
        sensorRepository.delete(sensor);
    }
    
    public void deleteSensorBySensorId(String sensorId) {
        Sensor sensor = sensorRepository.findBySensorId(sensorId)
                .orElseThrow(() -> new RuntimeException("Sensor not found with sensorId: " + sensorId));
        sensorRepository.delete(sensor);
    }
    
    public boolean existsBySensorId(String sensorId) {
        return sensorRepository.existsBySensorId(sensorId);
    }
}
