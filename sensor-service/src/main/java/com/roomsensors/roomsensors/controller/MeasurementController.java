package com.roomsensors.roomsensors.controller;

import com.roomsensors.roomsensors.dto.AverageMeasurementDTO;
import com.roomsensors.roomsensors.dto.MeasurementDTO;
import com.roomsensors.roomsensors.service.MeasurementService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequestMapping("/api/measurements")
@Tag(name = "Measurement Management", description = "API for managing sensor measurements")
@SecurityRequirement(name = "bearerAuth")
public class MeasurementController {
    
    @Autowired
    private MeasurementService measurementService;
    
    @GetMapping
    @Operation(summary = "Get all measurements", description = "Retrieve a list of all measurements")
    @PreAuthorize("hasAuthority('ROLE_READ_ONLY') or hasAuthority('ROLE_READ_WRITE')")
    public ResponseEntity<List<MeasurementDTO>> getAllMeasurements() {
        List<MeasurementDTO> measurements = measurementService.getAllMeasurements();
        return ResponseEntity.ok(measurements);
    }
    
    @GetMapping("/{id}")
    @Operation(summary = "Get measurement by ID", description = "Retrieve a measurement by its ID")
    @PreAuthorize("hasAuthority('ROLE_READ_ONLY') or hasAuthority('ROLE_READ_WRITE')")
    public ResponseEntity<MeasurementDTO> getMeasurementById(
            @Parameter(description = "Measurement ID") @PathVariable Long id) {
        MeasurementDTO measurement = measurementService.getMeasurementById(id);
        return ResponseEntity.ok(measurement);
    }
    
    @PostMapping
    @Operation(summary = "Create a new measurement", description = "Create a new measurement with the provided details")
    @PreAuthorize("hasAuthority('ROLE_READ_WRITE')")
    public ResponseEntity<MeasurementDTO> createMeasurement(@Valid @RequestBody MeasurementDTO measurementDTO) {
        MeasurementDTO createdMeasurement = measurementService.createMeasurement(measurementDTO);
        return ResponseEntity.status(HttpStatus.CREATED).body(createdMeasurement);
    }
    
    @GetMapping("/sensor/{sensorId}")
    @Operation(summary = "Get measurements by sensor", description = "Retrieve all measurements for a specific sensor")
    @PreAuthorize("hasAuthority('ROLE_READ_ONLY') or hasAuthority('ROLE_READ_WRITE')")
    public ResponseEntity<List<MeasurementDTO>> getMeasurementsBySensor(
            @Parameter(description = "Sensor ID") @PathVariable Long sensorId) {
        List<MeasurementDTO> measurements = measurementService.getMeasurementsBySensor(sensorId);
        return ResponseEntity.ok(measurements);
    }
    
    @GetMapping("/sensor/{sensorId}/range")
    @Operation(summary = "Get measurements by sensor and time range", description = "Retrieve measurements for a sensor within a time range")
    @PreAuthorize("hasAuthority('ROLE_READ_ONLY') or hasAuthority('ROLE_READ_WRITE')")
    public ResponseEntity<List<MeasurementDTO>> getMeasurementsBySensorAndTimeRange(
            @Parameter(description = "Sensor ID") @PathVariable Long sensorId,
            @Parameter(description = "Start timestamp") @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime start,
            @Parameter(description = "End timestamp") @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime end) {
        List<MeasurementDTO> measurements = measurementService.getMeasurementsBySensorAndTimeRange(sensorId, start, end);
        return ResponseEntity.ok(measurements);
    }
    
    @GetMapping("/sensor/{sensorId}/averages")
    @Operation(summary = "Get average measurements", description = "Calculate average temperature and humidity for a sensor within a time range")
    @PreAuthorize("hasAuthority('ROLE_READ_ONLY') or hasAuthority('ROLE_READ_WRITE')")
    public ResponseEntity<AverageMeasurementDTO> getAverageMeasurements(
            @Parameter(description = "Sensor ID") @PathVariable Long sensorId,
            @Parameter(description = "Start timestamp") @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime start,
            @Parameter(description = "End timestamp") @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime end) {
        AverageMeasurementDTO averages = measurementService.getAverageMeasurements(sensorId, start, end);
        return ResponseEntity.ok(averages);
    }
    
    @DeleteMapping("/{id}")
    @Operation(summary = "Delete measurement", description = "Delete a measurement by its ID")
    @PreAuthorize("hasAuthority('ROLE_READ_WRITE')")
    public ResponseEntity<Void> deleteMeasurement(
            @Parameter(description = "Measurement ID") @PathVariable Long id) {
        measurementService.deleteMeasurement(id);
        return ResponseEntity.noContent().build();
    }
}

