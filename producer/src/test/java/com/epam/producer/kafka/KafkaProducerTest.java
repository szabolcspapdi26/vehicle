package com.epam.producer.kafka;

import com.epam.producer.model.Coordinate;
import com.epam.producer.model.Vehicle;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.kafka.core.KafkaTemplate;

@ExtendWith(MockitoExtension.class)
class KafkaProducerTest {
    @Mock
    private KafkaTemplate<Long, Coordinate> kafkaTemplate;
    @InjectMocks
    private KafkaProducer kafkaProducer;

    @Test
    void sendVehicle_validVehicleGiven_kafkaTemplateSendMethodCalled() {
        // GIVEN
        Coordinate coordinate = new Coordinate(12.2, 12.2);

        Vehicle vehicle = new Vehicle(1L, coordinate);

        String topic = "input";

        // WHEN
        kafkaProducer.sendVehicle(vehicle);

        // THEN
        Mockito.verify(kafkaTemplate).send(topic, vehicle.id(), vehicle.coordinate());
    }
}
