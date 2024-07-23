package com.epam.producer.service;

import com.epam.producer.kafka.KafkaProducer;
import com.epam.producer.model.Coordinates;
import com.epam.producer.model.Vehicle;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class VehicleServiceImplTest {
    @Mock
    private KafkaProducer kafkaProducer;
    @InjectMocks
    private VehicleServiceImpl vehicleService;

    @Test
    void processVehicle_validVehicleReceived_kafkaProducerCalled() {
        // GIVEN
        Coordinates coordinates = Coordinates.builder()
            .x(12.2)
            .y(12.2)
            .build();

        Vehicle vehicle = Vehicle.builder()
            .id(1L)
            .coordinates(coordinates)
            .build();

        // WHEN
        vehicleService.processVehicle(vehicle);

        // THEN
        Mockito.verify(kafkaProducer).sendVehicle(vehicle);
    }
}
