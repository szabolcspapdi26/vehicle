package com.epam.consumer.consumer;

import com.epam.consumer.mapper.VehicleMapper;
import com.epam.consumer.model.CoordinateModel;
import com.epam.consumer.processing.ProcessVehicle;
import com.epam.schema.Coordinate;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class ConsumerImplTest {
    @Mock
    private ProcessVehicle processVehicle;
    @Mock
    private VehicleMapper vehicleMapper;
    @InjectMocks
    private ConsumerImpl consumer;

    @Test
    void consume_dataFromKafkaArrives_processVehicleDataCalled() {
        // GIVEN
        Coordinate coordinate = new Coordinate(12.2, 12.2);

        CoordinateModel coordinateModel = CoordinateModel.builder()
            .x(12.2)
            .y(12.2)
            .build();

        ConsumerRecord<Long, Coordinate> vehicle = Mockito.mock(ConsumerRecord.class);
        Mockito.when(vehicle.key()).thenReturn(1L);
        Mockito.when(vehicle.value()).thenReturn(coordinate);
        Mockito.when(vehicleMapper.toCoordinateModel(coordinate)).thenReturn(coordinateModel);

        // WHEN
        consumer.consume(vehicle);

        // THEN
        Mockito.verify(processVehicle).processVehicleData(1L, coordinateModel);
    }
}
