package com.epam.consumer.producer;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.kafka.core.KafkaTemplate;

@ExtendWith(MockitoExtension.class)
class ProducerTest {
    @Mock
    private KafkaTemplate<Long, String> kafkaTemplate;
    @InjectMocks
    private Producer producer;

    @Test
    void send_validIdAndTraveledDistanceGiven_kafkaTemplateSendMethodCalled() {
        // GIVEN
        Long id = 1L;

        Double traveledDistance = 10.0;

        String topic = "output";

        // WHEN
        producer.send(id, traveledDistance);

        // THEN
        Mockito.verify(kafkaTemplate).send(topic, id, traveledDistance.toString());
    }
}
