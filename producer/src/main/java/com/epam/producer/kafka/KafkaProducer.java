package com.epam.producer.kafka;

import com.epam.producer.model.Coordinate;
import com.epam.producer.model.Vehicle;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class KafkaProducer {
    @Value("${topic}")
    @Setter
    private String topic;

    private final KafkaTemplate<Long, Coordinate> kafkaTemplate;

    public void sendVehicle(Vehicle vehicle) {
        kafkaTemplate.send(topic, vehicle.getId(), vehicle.getCoordinate());
    }
}
