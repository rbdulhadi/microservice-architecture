package com.roomsensors.roomsensors.service;

import com.roomsensors.roomsensors.dto.SensorDTO;
import com.roomsensors.roomsensors.model.Sensor;
import com.roomsensors.roomsensors.model.SensorType;
import com.roomsensors.roomsensors.repository.SensorRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional
public class SensorService {
    
    @Autowired
    private SensorRepository sensorRepository;
    
    public List<SensorDTO> getAllSensors() {
        return sensorRepository.findAll().stream()
            .map(this::convertToDTO)
            .collect(Collectors.toList());
    }
    
    public SensorDTO getSensorById(Long id) {
        Sensor sensor = sensorRepository.findById(id)
            .orElseThrow(() -> new RuntimeException("Sensor not found with id: " + id));
        return convertToDTO(sensor);
    }
    
    public SensorDTO createSensor(SensorDTO sensorDTO) {
        Sensor sensor = convertToEntity(sensorDTO);
        Sensor savedSensor = sensorRepository.save(sensor);
        return convertToDTO(savedSensor);
    }
    
    public SensorDTO updateSensor(Long id, SensorDTO sensorDTO) {
        Sensor sensor = sensorRepository.findById(id)
            .orElseThrow(() -> new RuntimeException("Sensor not found with id: " + id));
        
        sensor.setName(sensorDTO.getName());
        sensor.setLocation(sensorDTO.getLocation());
        sensor.setActive(sensorDTO.getActive());
        sensor.setType(sensorDTO.getType());
        
        Sensor updatedSensor = sensorRepository.save(sensor);
        return convertToDTO(updatedSensor);
    }
    
    public void deleteSensor(Long id) {
        if (!sensorRepository.existsById(id)) {
            throw new RuntimeException("Sensor not found with id: " + id);
        }
        sensorRepository.deleteById(id);
    }
    
    public List<SensorDTO> getSensorsByType(SensorType type) {
        return sensorRepository.findByType(type).stream()
            .map(this::convertToDTO)
            .collect(Collectors.toList());
    }
    
    public List<SensorDTO> getActiveSensors() {
        return sensorRepository.findByActive(true).stream()
            .map(this::convertToDTO)
            .collect(Collectors.toList());
    }
    
    private SensorDTO convertToDTO(Sensor sensor) {
        SensorDTO dto = new SensorDTO();
        dto.setId(sensor.getId());
        dto.setName(sensor.getName());
        dto.setLocation(sensor.getLocation());
        dto.setActive(sensor.getActive());
        dto.setType(sensor.getType());
        return dto;
    }
    
    private Sensor convertToEntity(SensorDTO dto) {
        Sensor sensor = new Sensor();
        sensor.setName(dto.getName());
        sensor.setLocation(dto.getLocation());
        sensor.setActive(dto.getActive());
        sensor.setType(dto.getType());
        return sensor;
    }
}

