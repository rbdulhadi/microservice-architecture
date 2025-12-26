package com.roomsensors.roomsensors.repository;

import com.roomsensors.roomsensors.model.Measurement;
import com.roomsensors.roomsensors.model.Sensor;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Repository
public interface MeasurementRepository extends JpaRepository<Measurement, Long> {
    List<Measurement> findBySensor(Sensor sensor);
    List<Measurement> findBySensorAndTimestampBetween(Sensor sensor, LocalDateTime start, LocalDateTime end);
    
    @Query("SELECT AVG(m.temperature) FROM Measurement m WHERE m.sensor = :sensor AND m.timestamp BETWEEN :start AND :end")
    Optional<Double> findAverageTemperatureBySensorAndTimestampBetween(
        @Param("sensor") Sensor sensor,
        @Param("start") LocalDateTime start,
        @Param("end") LocalDateTime end
    );
    
    @Query("SELECT AVG(m.humidity) FROM Measurement m WHERE m.sensor = :sensor AND m.timestamp BETWEEN :start AND :end")
    Optional<Double> findAverageHumidityBySensorAndTimestampBetween(
        @Param("sensor") Sensor sensor,
        @Param("start") LocalDateTime start,
        @Param("end") LocalDateTime end
    );
}

