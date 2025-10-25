package com.roomsensors.roomsensors.repository;

import com.roomsensors.roomsensors.model.Sensor;
import com.roomsensors.roomsensors.model.SensorStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface SensorRepository extends JpaRepository<Sensor, Long> {
    
    Optional<Sensor> findBySensorId(String sensorId);
    
    List<Sensor> findByStatus(SensorStatus status);
    
    List<Sensor> findByLocation(String location);
    
    @Query("SELECT s FROM Sensor s WHERE s.location = :location AND s.status = :status")
    List<Sensor> findByLocationAndStatus(@Param("location") String location, @Param("status") SensorStatus status);
    
    @Query("SELECT s FROM Sensor s WHERE s.name LIKE %:name%")
    List<Sensor> findByNameContaining(@Param("name") String name);
    
    boolean existsBySensorId(String sensorId);
}
