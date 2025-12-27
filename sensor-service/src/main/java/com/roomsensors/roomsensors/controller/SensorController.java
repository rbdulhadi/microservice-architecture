package com.roomsensors.roomsensors.controller;

import com.roomsensors.roomsensors.dto.SensorDTO;
import com.roomsensors.roomsensors.model.SensorType;
import com.roomsensors.roomsensors.service.SensorService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/sensors")
@Tag(name = "Sensor Management", description = "API for managing sensors")
@SecurityRequirement(name = "bearerAuth")
public class SensorController {
    
    @Autowired
    private SensorService sensorService;
    
    @GetMapping
    @Operation(summary = "Get all sensors", description = "Retrieve a list of all sensors")
    @PreAuthorize("hasAuthority('ROLE_READ_ONLY') or hasAuthority('ROLE_READ_WRITE')")
    public ResponseEntity<List<SensorDTO>> getAllSensors() {
        List<SensorDTO> sensors = sensorService.getAllSensors();
        return ResponseEntity.ok(sensors);
    }
    
    @GetMapping("/{id}")
    @Operation(summary = "Get sensor by ID", description = "Retrieve a sensor by its ID")
    @PreAuthorize("hasAuthority('ROLE_READ_ONLY') or hasAuthority('ROLE_READ_WRITE')")
    public ResponseEntity<SensorDTO> getSensorById(
            @Parameter(description = "Sensor ID") @PathVariable Long id) {
        SensorDTO sensor = sensorService.getSensorById(id);
        return ResponseEntity.ok(sensor);
    }
    
    @PostMapping
    @Operation(summary = "Create a new sensor", description = "Create a new sensor with the provided details")
    @PreAuthorize("hasAuthority('ROLE_READ_WRITE')")
    public ResponseEntity<SensorDTO> createSensor(@Valid @RequestBody SensorDTO sensorDTO) {
        SensorDTO createdSensor = sensorService.createSensor(sensorDTO);
        return ResponseEntity.status(HttpStatus.CREATED).body(createdSensor);
    }
    
    @PutMapping("/{id}")
    @Operation(summary = "Update sensor", description = "Update an existing sensor")
    @PreAuthorize("hasAuthority('ROLE_READ_WRITE')")
    public ResponseEntity<SensorDTO> updateSensor(
            @Parameter(description = "Sensor ID") @PathVariable Long id,
            @Valid @RequestBody SensorDTO sensorDTO) {
        SensorDTO updatedSensor = sensorService.updateSensor(id, sensorDTO);
        return ResponseEntity.ok(updatedSensor);
    }
    
    @DeleteMapping("/{id}")
    @Operation(summary = "Delete sensor", description = "Delete a sensor by its ID")
    @PreAuthorize("hasAuthority('ROLE_READ_WRITE')")
    public ResponseEntity<Void> deleteSensor(
            @Parameter(description = "Sensor ID") @PathVariable Long id) {
        sensorService.deleteSensor(id);
        return ResponseEntity.noContent().build();
    }
}

