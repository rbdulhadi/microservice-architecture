package com.roomsensors.roomsensors.controller;

import com.roomsensors.roomsensors.dto.SensorDto;
import com.roomsensors.roomsensors.model.Sensor;
import com.roomsensors.roomsensors.model.SensorStatus;
import com.roomsensors.roomsensors.service.SensorService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/sensors")
@Tag(name = "Sensor Management", description = "APIs for managing sensors")
public class SensorController {
    
    @Autowired
    private SensorService sensorService;
    
    @GetMapping
    @Operation(summary = "Get all sensors", description = "Retrieve a list of all sensors")
    public ResponseEntity<List<SensorDto>> getAllSensors() {
        List<Sensor> sensors = sensorService.getAllSensors();
        List<SensorDto> sensorDtos = sensors.stream()
                .map(this::convertToDto)
                .collect(Collectors.toList());
        return ResponseEntity.ok(sensorDtos);
    }
    
    @GetMapping("/{id}")
    @Operation(summary = "Get sensor by ID", description = "Retrieve a specific sensor by its ID")
    public ResponseEntity<SensorDto> getSensorById(@PathVariable Long id) {
        Optional<Sensor> sensor = sensorService.getSensorById(id);
        return sensor.map(s -> ResponseEntity.ok(convertToDto(s)))
                .orElse(ResponseEntity.notFound().build());
    }
    
    @GetMapping("/sensor-id/{sensorId}")
    @Operation(summary = "Get sensor by sensor ID", description = "Retrieve a specific sensor by its sensor ID")
    public ResponseEntity<SensorDto> getSensorBySensorId(@PathVariable String sensorId) {
        Optional<Sensor> sensor = sensorService.getSensorBySensorId(sensorId);
        return sensor.map(s -> ResponseEntity.ok(convertToDto(s)))
                .orElse(ResponseEntity.notFound().build());
    }
    
    @GetMapping("/status/{status}")
    @Operation(summary = "Get sensors by status", description = "Retrieve sensors filtered by status")
    public ResponseEntity<List<SensorDto>> getSensorsByStatus(@PathVariable SensorStatus status) {
        List<Sensor> sensors = sensorService.getSensorsByStatus(status);
        List<SensorDto> sensorDtos = sensors.stream()
                .map(this::convertToDto)
                .collect(Collectors.toList());
        return ResponseEntity.ok(sensorDtos);
    }
    
    @GetMapping("/location/{location}")
    @Operation(summary = "Get sensors by location", description = "Retrieve sensors filtered by location")
    public ResponseEntity<List<SensorDto>> getSensorsByLocation(@PathVariable String location) {
        List<Sensor> sensors = sensorService.getSensorsByLocation(location);
        List<SensorDto> sensorDtos = sensors.stream()
                .map(this::convertToDto)
                .collect(Collectors.toList());
        return ResponseEntity.ok(sensorDtos);
    }
    
    @GetMapping("/search")
    @Operation(summary = "Search sensors by name", description = "Search sensors by name containing the specified text")
    public ResponseEntity<List<SensorDto>> searchSensorsByName(@RequestParam String name) {
        List<Sensor> sensors = sensorService.getSensorsByNameContaining(name);
        List<SensorDto> sensorDtos = sensors.stream()
                .map(this::convertToDto)
                .collect(Collectors.toList());
        return ResponseEntity.ok(sensorDtos);
    }
    
    @PostMapping
    @Operation(summary = "Create a new sensor", description = "Create a new sensor with the provided details")
    public ResponseEntity<SensorDto> createSensor(@Valid @RequestBody SensorDto sensorDto) {
        try {
            Sensor sensor = convertToEntity(sensorDto);
            Sensor createdSensor = sensorService.createSensor(sensor);
            return ResponseEntity.status(HttpStatus.CREATED).body(convertToDto(createdSensor));
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().build();
        }
    }
    
    @PutMapping("/{id}")
    @Operation(summary = "Update sensor", description = "Update an existing sensor by ID")
    public ResponseEntity<SensorDto> updateSensor(@PathVariable Long id, @Valid @RequestBody SensorDto sensorDto) {
        try {
            Sensor sensor = convertToEntity(sensorDto);
            Sensor updatedSensor = sensorService.updateSensor(id, sensor);
            return ResponseEntity.ok(convertToDto(updatedSensor));
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }
    
    @PatchMapping("/{sensorId}/status")
    @Operation(summary = "Update sensor status", description = "Update the status of a sensor by sensor ID")
    public ResponseEntity<SensorDto> updateSensorStatus(@PathVariable String sensorId, @RequestParam SensorStatus status) {
        try {
            Sensor updatedSensor = sensorService.updateSensorStatus(sensorId, status);
            return ResponseEntity.ok(convertToDto(updatedSensor));
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }
    
    @DeleteMapping("/{id}")
    @Operation(summary = "Delete sensor", description = "Delete a sensor by ID")
    public ResponseEntity<Void> deleteSensor(@PathVariable Long id) {
        try {
            sensorService.deleteSensor(id);
            return ResponseEntity.noContent().build();
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }
    
    @DeleteMapping("/sensor-id/{sensorId}")
    @Operation(summary = "Delete sensor by sensor ID", description = "Delete a sensor by sensor ID")
    public ResponseEntity<Void> deleteSensorBySensorId(@PathVariable String sensorId) {
        try {
            sensorService.deleteSensorBySensorId(sensorId);
            return ResponseEntity.noContent().build();
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }
    
    // Helper methods
    private SensorDto convertToDto(Sensor sensor) {
        SensorDto dto = new SensorDto();
        dto.setId(sensor.getId());
        dto.setSensorId(sensor.getSensorId());
        dto.setName(sensor.getName());
        dto.setLocation(sensor.getLocation());
        dto.setStatus(sensor.getStatus());
        dto.setCreatedAt(sensor.getCreatedAt());
        dto.setUpdatedAt(sensor.getUpdatedAt());
        return dto;
    }
    
    private Sensor convertToEntity(SensorDto dto) {
        Sensor sensor = new Sensor();
        sensor.setSensorId(dto.getSensorId());
        sensor.setName(dto.getName());
        sensor.setLocation(dto.getLocation());
        sensor.setStatus(dto.getStatus());
        return sensor;
    }
}
