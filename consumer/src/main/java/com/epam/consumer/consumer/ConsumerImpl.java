package com.epam.consumer.consumer;

import com.epam.consumer.mapper.VehicleMapper;
import com.epam.consumer.processing.ProcessVehicle;
import com.epam.schema.Coordinate;
import lombok.RequiredArgsConstructor;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class ConsumerImpl implements Consumer {
    private final ProcessVehicle processVehicle;
    private final VehicleMapper vehicleMapper;

    @Override
    @KafkaListener(topics = "input", groupId = "vehicle-consumer-group")
    public void consume(ConsumerRecord<Long, Coordinate> vehicle) {
        processVehicle.processVehicleData(vehicle.key(), vehicleMapper.toCoordinateModel(vehicle.value()));
    }
}
