package com.epam.producer.kafka;

import com.epam.producer.model.Coordinate;
import com.epam.producer.model.Vehicle;
import lombok.RequiredArgsConstructor;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class KafkaProducer {
    private final KafkaTemplate<Long, Coordinate> kafkaTemplate;

    public void sendVehicle(Vehicle vehicle) {
        kafkaTemplate.send("input", vehicle.id(), vehicle.coordinate());
    }
}
