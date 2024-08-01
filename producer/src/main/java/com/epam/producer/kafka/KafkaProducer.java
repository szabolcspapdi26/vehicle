package com.epam.producer.kafka;

import com.epam.producer.mapper.VehicleMapper;
import com.epam.producer.model.VehicleModel;
import com.epam.schema.Coordinate;
import lombok.RequiredArgsConstructor;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class KafkaProducer {
    private final KafkaTemplate<Long, Coordinate> kafkaTemplate;
    private final VehicleMapper vehicleMapper;

    public void sendVehicle(VehicleModel vehicle) {
        kafkaTemplate.send("input", vehicle.id(), vehicleMapper.toCoordinateSchema(vehicle.coordinate()));
    }
}
