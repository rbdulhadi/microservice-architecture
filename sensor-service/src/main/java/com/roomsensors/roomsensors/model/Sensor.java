package com.roomsensors.roomsensors.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import java.time.LocalDateTime;

@Entity
@Table(name = "sensors")
public class Sensor {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @NotBlank(message = "Name is required")
    @Column(nullable = false)
    private String name;
    
    @NotBlank(message = "Location is required")
    @Column(nullable = false)
    private String location;
    
    @NotNull(message = "Active status is required")
    @Column(nullable = false)
    private Boolean active;
    
    @NotNull(message = "Sensor type is required")
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private SensorType type;
    
    @Column(name = "created_at", updatable = false)
    private LocalDateTime createdAt;
    
    @Column(name = "updated_at")
    private LocalDateTime updatedAt;
    
    @PrePersist
    protected void onCreate() {
        createdAt = LocalDateTime.now();
        updatedAt = LocalDateTime.now();
    }
    
    @PreUpdate
    protected void onUpdate() {
        updatedAt = LocalDateTime.now();
    }
    
    // Constructors
    public Sensor() {
    }
    
    public Sensor(String name, String location, Boolean active, SensorType type) {
        this.name = name;
        this.location = location;
        this.active = active;
        this.type = type;
    }
    
    // Getters and Setters
    public Long getId() {
        return id;
    }
    
    public void setId(Long id) {
        this.id = id;
    }
    
    public String getName() {
        return name;
    }
    
    public void setName(String name) {
        this.name = name;
    }
    
    public String getLocation() {
        return location;
    }
    
    public void setLocation(String location) {
        this.location = location;
    }
    
    public Boolean getActive() {
        return active;
    }
    
    public void setActive(Boolean active) {
        this.active = active;
    }
    
    public SensorType getType() {
        return type;
    }
    
    public void setType(SensorType type) {
        this.type = type;
    }
}

