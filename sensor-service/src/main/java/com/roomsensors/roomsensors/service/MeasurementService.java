package com.roomsensors.roomsensors.service;

import com.roomsensors.roomsensors.dto.AverageMeasurementDTO;
import com.roomsensors.roomsensors.dto.MeasurementDTO;
import com.roomsensors.roomsensors.model.Measurement;
import com.roomsensors.roomsensors.model.Sensor;
import com.roomsensors.roomsensors.repository.MeasurementRepository;
import com.roomsensors.roomsensors.repository.SensorRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional
public class MeasurementService {
    
    @Autowired
    private MeasurementRepository measurementRepository;
    
    @Autowired
    private SensorRepository sensorRepository;
    
    public List<MeasurementDTO> getAllMeasurements() {
        return measurementRepository.findAll().stream()
            .map(this::convertToDTO)
            .collect(Collectors.toList());
    }
    
    public MeasurementDTO getMeasurementById(Long id) {
        Measurement measurement = measurementRepository.findById(id)
            .orElseThrow(() -> new RuntimeException("Measurement not found with id: " + id));
        return convertToDTO(measurement);
    }
    
    public MeasurementDTO createMeasurement(MeasurementDTO measurementDTO) {
        Sensor sensor = sensorRepository.findById(measurementDTO.getSensorId())
            .orElseThrow(() -> new RuntimeException("Sensor not found with id: " + measurementDTO.getSensorId()));
        
        if (!sensor.getActive()) {
            throw new RuntimeException("Cannot create measurement for inactive sensor");
        }
        
        Measurement measurement = convertToEntity(measurementDTO, sensor);
        Measurement savedMeasurement = measurementRepository.save(measurement);
        return convertToDTO(savedMeasurement);
    }
    
    public List<MeasurementDTO> getMeasurementsBySensor(Long sensorId) {
        Sensor sensor = sensorRepository.findById(sensorId)
            .orElseThrow(() -> new RuntimeException("Sensor not found with id: " + sensorId));
        
        return measurementRepository.findBySensor(sensor).stream()
            .map(this::convertToDTO)
            .collect(Collectors.toList());
    }
    
    public List<MeasurementDTO> getMeasurementsBySensorAndTimeRange(Long sensorId, LocalDateTime start, LocalDateTime end) {
        Sensor sensor = sensorRepository.findById(sensorId)
            .orElseThrow(() -> new RuntimeException("Sensor not found with id: " + sensorId));
        
        return measurementRepository.findBySensorAndTimestampBetween(sensor, start, end).stream()
            .map(this::convertToDTO)
            .collect(Collectors.toList());
    }
    
    public AverageMeasurementDTO getAverageMeasurements(Long sensorId, LocalDateTime start, LocalDateTime end) {
        Sensor sensor = sensorRepository.findById(sensorId)
            .orElseThrow(() -> new RuntimeException("Sensor not found with id: " + sensorId));
        
        Double avgTemp = measurementRepository.findAverageTemperatureBySensorAndTimestampBetween(sensor, start, end)
            .orElse(0.0);
        Double avgHumidity = measurementRepository.findAverageHumidityBySensorAndTimestampBetween(sensor, start, end)
            .orElse(0.0);
        
        return new AverageMeasurementDTO(avgTemp, avgHumidity);
    }
    
    public void deleteMeasurement(Long id) {
        if (!measurementRepository.existsById(id)) {
            throw new RuntimeException("Measurement not found with id: " + id);
        }
        measurementRepository.deleteById(id);
    }
    
    private MeasurementDTO convertToDTO(Measurement measurement) {
        MeasurementDTO dto = new MeasurementDTO();
        dto.setId(measurement.getId());
        dto.setSensorId(measurement.getSensor().getId());
        dto.setTimestamp(measurement.getTimestamp());
        dto.setTemperature(measurement.getTemperature());
        dto.setHumidity(measurement.getHumidity());
        return dto;
    }
    
    private Measurement convertToEntity(MeasurementDTO dto, Sensor sensor) {
        Measurement measurement = new Measurement();
        measurement.setSensor(sensor);
        measurement.setTimestamp(dto.getTimestamp() != null ? dto.getTimestamp() : LocalDateTime.now());
        measurement.setTemperature(dto.getTemperature());
        measurement.setHumidity(dto.getHumidity());
        return measurement;
    }
}

