package com.epam.consumer.producer;

import lombok.RequiredArgsConstructor;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class Producer {
    private final KafkaTemplate<Long, String> kafkaTemplate;

    public void send(Long id, Double traveledDistance) {
        kafkaTemplate.send("output", id, traveledDistance.toString());
    }
}
