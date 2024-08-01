package com.epam.producer.service;

import com.epam.producer.kafka.KafkaProducer;
import com.epam.producer.model.CoordinateModel;
import com.epam.producer.model.VehicleModel;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class VehicleModelServiceImplTest {
    @Mock
    private KafkaProducer kafkaProducer;
    @InjectMocks
    private VehicleServiceImpl vehicleService;

    @Test
    void processVehicle_validVehicleReceived_kafkaProducerCalled() {
        // GIVEN
        CoordinateModel coordinateModel = CoordinateModel.builder()
            .x(12.2)
            .y(12.2)
            .build();

        VehicleModel vehicleModel = VehicleModel.builder()
            .id(1L)
            .coordinate(coordinateModel)
            .build();

        // WHEN
        vehicleService.processVehicle(vehicleModel);

        // THEN
        Mockito.verify(kafkaProducer).sendVehicle(vehicleModel);
    }
}
