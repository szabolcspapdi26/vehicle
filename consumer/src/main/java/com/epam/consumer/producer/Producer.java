package com.epam.consumer.producer;

import lombok.RequiredArgsConstructor;
import lombok.Setter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class Producer {
    @Value("${output-topic}")
    @Setter
    private String topic;
    private final KafkaTemplate<Long, String> kafkaTemplate;

    public void send(Long id, Double traveledDistance) {
        kafkaTemplate.send(topic, id, traveledDistance.toString());
    }
}
