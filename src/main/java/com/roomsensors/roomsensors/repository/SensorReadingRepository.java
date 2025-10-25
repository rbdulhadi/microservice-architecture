package com.roomsensors.roomsensors.repository;

import com.roomsensors.roomsensors.model.Sensor;
import com.roomsensors.roomsensors.model.SensorReading;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface SensorReadingRepository extends JpaRepository<SensorReading, Long> {
    
    List<SensorReading> findBySensor(Sensor sensor);
    
    Page<SensorReading> findBySensor(Sensor sensor, Pageable pageable);
    
    List<SensorReading> findBySensorAndTimestampBetween(Sensor sensor, LocalDateTime start, LocalDateTime end);
    
    @Query("SELECT sr FROM SensorReading sr WHERE sr.sensor.sensorId = :sensorId ORDER BY sr.timestamp DESC")
    List<SensorReading> findBySensorIdOrderByTimestampDesc(@Param("sensorId") String sensorId);
    
    @Query("SELECT sr FROM SensorReading sr WHERE sr.sensor.sensorId = :sensorId AND sr.timestamp BETWEEN :start AND :end ORDER BY sr.timestamp DESC")
    List<SensorReading> findBySensorIdAndTimestampBetween(@Param("sensorId") String sensorId, 
                                                          @Param("start") LocalDateTime start, 
                                                          @Param("end") LocalDateTime end);
    
    @Query("SELECT sr FROM SensorReading sr WHERE sr.sensor.location = :location ORDER BY sr.timestamp DESC")
    List<SensorReading> findByLocationOrderByTimestampDesc(@Param("location") String location);
    
    @Query("SELECT sr FROM SensorReading sr WHERE sr.sensor.location = :location AND sr.timestamp BETWEEN :start AND :end ORDER BY sr.timestamp DESC")
    List<SensorReading> findByLocationAndTimestampBetween(@Param("location") String location,
                                                           @Param("start") LocalDateTime start,
                                                           @Param("end") LocalDateTime end);
    
    @Query("SELECT AVG(sr.temperature) FROM SensorReading sr WHERE sr.sensor.sensorId = :sensorId AND sr.timestamp BETWEEN :start AND :end")
    Double findAverageTemperatureBySensorIdAndTimestampBetween(@Param("sensorId") String sensorId,
                                                               @Param("start") LocalDateTime start,
                                                               @Param("end") LocalDateTime end);
    
    @Query("SELECT AVG(sr.humidity) FROM SensorReading sr WHERE sr.sensor.sensorId = :sensorId AND sr.timestamp BETWEEN :start AND :end")
    Double findAverageHumidityBySensorIdAndTimestampBetween(@Param("sensorId") String sensorId,
                                                            @Param("start") LocalDateTime start,
                                                            @Param("end") LocalDateTime end);
}
