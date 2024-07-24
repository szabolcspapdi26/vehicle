package com.epam.consumer.consumer;

import com.epam.consumer.model.Coordinate;
import com.epam.consumer.processing.ProcessVehicle;
import lombok.RequiredArgsConstructor;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class ConsumerImpl implements Consumer {
    private final ProcessVehicle processVehicle;

    @Override
    @KafkaListener(topics = "${input-topic}", groupId = "vehicle-consumer-group")
    public void consume(ConsumerRecord<Long, Coordinate> vehicle) {
        processVehicle.processVehicleData(vehicle.key(), vehicle.value());
    }
}
