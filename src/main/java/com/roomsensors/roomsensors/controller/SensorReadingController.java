package com.roomsensors.roomsensors.controller;

import com.roomsensors.roomsensors.dto.SensorReadingDto;
import com.roomsensors.roomsensors.dto.AverageReadingsDto;
import com.roomsensors.roomsensors.model.Sensor;
import com.roomsensors.roomsensors.model.SensorReading;
import com.roomsensors.roomsensors.service.SensorReadingService;
import com.roomsensors.roomsensors.service.SensorService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/readings")
@Tag(name = "Sensor Reading Management", description = "APIs for managing sensor readings")
public class SensorReadingController {
    
    @Autowired
    private SensorReadingService sensorReadingService;
    
    @Autowired
    private SensorService sensorService;
    
    @GetMapping
    @Operation(summary = "Get all sensor readings", description = "Retrieve all sensor readings")
    public ResponseEntity<List<SensorReadingDto>> getAllReadings() {
        List<SensorReading> readings = sensorReadingService.getAllReadings();
        List<SensorReadingDto> readingDtos = readings.stream()
                .map(this::convertToDto)
                .collect(Collectors.toList());
        return ResponseEntity.ok(readingDtos);
    }
    
    @GetMapping("/{id}")
    @Operation(summary = "Get reading by ID", description = "Retrieve a specific sensor reading by its ID")
    public ResponseEntity<SensorReadingDto> getReadingById(@PathVariable Long id) {
        Optional<SensorReading> reading = sensorReadingService.getReadingById(id);
        return reading.map(r -> ResponseEntity.ok(convertToDto(r)))
                .orElse(ResponseEntity.notFound().build());
    }
    
    @GetMapping("/sensor/{sensorId}")
    @Operation(summary = "Get readings by sensor ID", description = "Retrieve all readings for a specific sensor")
    public ResponseEntity<List<SensorReadingDto>> getReadingsBySensorId(@PathVariable String sensorId) {
        List<SensorReading> readings = sensorReadingService.getReadingsBySensorId(sensorId);
        List<SensorReadingDto> readingDtos = readings.stream()
                .map(this::convertToDto)
                .collect(Collectors.toList());
        return ResponseEntity.ok(readingDtos);
    }
    
    @GetMapping("/sensor/{sensorId}/range")
    @Operation(summary = "Get readings by sensor ID and time range", description = "Retrieve readings for a sensor within a specific time range")
    public ResponseEntity<List<SensorReadingDto>> getReadingsBySensorIdAndRange(
            @PathVariable String sensorId,
            @Parameter(description = "Start time (ISO format)") @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime start,
            @Parameter(description = "End time (ISO format)") @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime end) {
        List<SensorReading> readings = sensorReadingService.getReadingsBySensorIdAndTimestampBetween(sensorId, start, end);
        List<SensorReadingDto> readingDtos = readings.stream()
                .map(this::convertToDto)
                .collect(Collectors.toList());
        return ResponseEntity.ok(readingDtos);
    }
    
    @GetMapping("/location/{location}")
    @Operation(summary = "Get readings by location", description = "Retrieve all readings for sensors in a specific location")
    public ResponseEntity<List<SensorReadingDto>> getReadingsByLocation(@PathVariable String location) {
        List<SensorReading> readings = sensorReadingService.getReadingsByLocation(location);
        List<SensorReadingDto> readingDtos = readings.stream()
                .map(this::convertToDto)
                .collect(Collectors.toList());
        return ResponseEntity.ok(readingDtos);
    }
    
    @GetMapping("/location/{location}/range")
    @Operation(summary = "Get readings by location and time range", description = "Retrieve readings for a location within a specific time range")
    public ResponseEntity<List<SensorReadingDto>> getReadingsByLocationAndRange(
            @PathVariable String location,
            @Parameter(description = "Start time (ISO format)") @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime start,
            @Parameter(description = "End time (ISO format)") @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime end) {
        List<SensorReading> readings = sensorReadingService.getReadingsByLocationAndTimestampBetween(location, start, end);
        List<SensorReadingDto> readingDtos = readings.stream()
                .map(this::convertToDto)
                .collect(Collectors.toList());
        return ResponseEntity.ok(readingDtos);
    }
    
    @GetMapping("/sensor/{sensorId}/averages")
    @Operation(summary = "Get average readings for a sensor", description = "Get average temperature and humidity for a sensor within a time range")
    public ResponseEntity<AverageReadingsDto> getAverageReadings(
            @PathVariable String sensorId,
            @Parameter(description = "Start time (ISO format)") @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime start,
            @Parameter(description = "End time (ISO format)") @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime end) {
        Double avgTemperature = sensorReadingService.getAverageTemperature(sensorId, start, end);
        Double avgHumidity = sensorReadingService.getAverageHumidity(sensorId, start, end);
        
        AverageReadingsDto response = new AverageReadingsDto(sensorId, avgTemperature, avgHumidity, start, end);
        return ResponseEntity.ok(response);
    }
    
    @PostMapping
    @Operation(summary = "Create a new sensor reading", description = "Create a new sensor reading with the provided data")
    public ResponseEntity<SensorReadingDto> createReading(@Valid @RequestBody SensorReadingDto readingDto) {
        try {
            SensorReading reading = convertToEntity(readingDto);
            SensorReading createdReading = sensorReadingService.createReading(reading);
            return ResponseEntity.status(HttpStatus.CREATED).body(convertToDto(createdReading));
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().build();
        }
    }
    
    @PostMapping("/sensor/{sensorId}")
    @Operation(summary = "Create a reading for a sensor", description = "Create a new reading for a specific sensor")
    public ResponseEntity<SensorReadingDto> createReadingForSensor(
            @PathVariable String sensorId,
            @RequestParam BigDecimal temperature,
            @RequestParam BigDecimal humidity,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime timestamp) {
        try {
            LocalDateTime readingTimestamp = timestamp != null ? timestamp : LocalDateTime.now();
            SensorReading reading = sensorReadingService.createReading(sensorId, temperature, humidity, readingTimestamp);
            return ResponseEntity.status(HttpStatus.CREATED).body(convertToDto(reading));
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().build();
        }
    }
    
    @DeleteMapping("/{id}")
    @Operation(summary = "Delete sensor reading", description = "Delete a specific sensor reading by ID")
    public ResponseEntity<Void> deleteReading(@PathVariable Long id) {
        try {
            sensorReadingService.deleteReading(id);
            return ResponseEntity.noContent().build();
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }
    
    // Helper methods
    private SensorReadingDto convertToDto(SensorReading reading) {
        SensorReadingDto dto = new SensorReadingDto();
        dto.setId(reading.getId());
        dto.setSensorId(reading.getSensor().getSensorId());
        dto.setTemperature(reading.getTemperature());
        dto.setHumidity(reading.getHumidity());
        dto.setTimestamp(reading.getTimestamp());
        dto.setCreatedAt(reading.getCreatedAt());
        return dto;
    }
    
    private SensorReading convertToEntity(SensorReadingDto dto) {
        Sensor sensor = sensorService.getSensorBySensorId(dto.getSensorId())
                .orElseThrow(() -> new RuntimeException("Sensor not found with sensorId: " + dto.getSensorId()));
        
        SensorReading reading = new SensorReading();
        reading.setSensor(sensor);
        reading.setTemperature(dto.getTemperature());
        reading.setHumidity(dto.getHumidity());
        reading.setTimestamp(dto.getTimestamp());
        return reading;
    }
}
