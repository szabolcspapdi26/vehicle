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
        Coordinate coordinates = Coordinate.builder()
            .x(12.2)
            .y(12.2)
            .build();

        Vehicle vehicle = Vehicle.builder()
            .id(1L)
            .coordinate(coordinates)
            .build();

        String topic = "input";

        kafkaProducer.setTopic(topic);

        // WHEN
        kafkaProducer.sendVehicle(vehicle);

        // THEN
        Mockito.verify(kafkaTemplate).send(topic, vehicle.getId(), vehicle.getCoordinate());
    }
}
