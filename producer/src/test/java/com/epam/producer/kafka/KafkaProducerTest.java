package com.epam.producer.kafka;

import com.epam.producer.mapper.VehicleMapper;
import com.epam.producer.model.CoordinateModel;
import com.epam.producer.model.VehicleModel;
import com.epam.schema.Coordinate;
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
    @Mock
    private VehicleMapper vehicleMapper;
    @InjectMocks
    private KafkaProducer kafkaProducer;

    @Test
    void sendVehicle_validVehicleGiven_kafkaTemplateSendMethodCalled() {
        // GIVEN
        CoordinateModel coordinateModel = new CoordinateModel(12.2, 12.2);

        VehicleModel vehicleModel = new VehicleModel(1L, coordinateModel);

        Coordinate coordinate = new Coordinate();
        coordinate.setX(12.2);
        coordinate.setY(12.2);

        Mockito.when(vehicleMapper.toCoordinateSchema(coordinateModel)).thenReturn(coordinate);

        String topic = "input";

        // WHEN
        kafkaProducer.sendVehicle(vehicleModel);

        // THEN
        Mockito.verify(kafkaTemplate).send(topic, vehicleModel.id(), coordinate);
    }
}
