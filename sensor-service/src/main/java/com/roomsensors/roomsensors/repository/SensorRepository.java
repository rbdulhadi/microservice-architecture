package com.roomsensors.roomsensors.repository;

import com.roomsensors.roomsensors.model.Sensor;
import com.roomsensors.roomsensors.model.SensorType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface SensorRepository extends JpaRepository<Sensor, Long> {
    List<Sensor> findByActive(Boolean active);
    List<Sensor> findByType(SensorType type);
}

