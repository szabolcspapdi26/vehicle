package com.epam.producer.service;

import com.epam.producer.kafka.KafkaProducer;
import com.epam.producer.model.Vehicle;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class VehicleServiceImpl implements VehicleService {
    private final KafkaProducer kafkaProducer;

    @Override
    public void processVehicle(Vehicle vehicle) {
        kafkaProducer.sendVehicle(vehicle);
    }
}
