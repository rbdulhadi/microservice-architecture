package com.roomsensors.roomsensors;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@SpringBootApplication
@EnableDiscoveryClient
@EnableJpaRepositories
public class RoomSensorsApplication {

    public static void main(String[] args) {
        SpringApplication.run(RoomSensorsApplication.class, args);
    }

}

