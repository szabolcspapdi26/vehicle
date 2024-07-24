package com.epam.consumer.consumer;

import com.epam.consumer.model.Coordinate;
import com.epam.consumer.processing.ProcessVehicle;
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
    @InjectMocks
    private ConsumerImpl consumer;

    @Test
    void consume_dataFromKafkaArrives_processVehicleDataCalled() {
        // GIVEN
        Coordinate coordinate = Coordinate.builder()
            .x(12.2)
            .y(12.2)
            .build();

        ConsumerRecord<Long, Coordinate> vehicle = Mockito.mock(ConsumerRecord.class);
        Mockito.when(vehicle.key()).thenReturn(1L);
        Mockito.when(vehicle.value()).thenReturn(coordinate);

        // WHEN
        consumer.consume(vehicle);

        // THEN
        Mockito.verify(processVehicle).processVehicleData(1L, coordinate);
    }
}
