package com.epam.consumer.integration;

import com.epam.schema.Coordinate;
import io.confluent.kafka.serializers.KafkaAvroSerializer;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.common.serialization.LongSerializer;
import org.awaitility.Awaitility;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.core.DefaultKafkaProducerFactory;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.core.ProducerFactory;
import org.springframework.kafka.test.EmbeddedKafkaBroker;
import org.springframework.kafka.test.context.EmbeddedKafka;
import org.springframework.kafka.test.utils.KafkaTestUtils;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;

@ExtendWith(SpringExtension.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@ActiveProfiles(value = "test")
@EmbeddedKafka(partitions = 1)
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
class ConsumerIntegrationTest {
    @Autowired
    private EmbeddedKafkaBroker embeddedKafkaBroker;
    private KafkaTemplate<Long, Coordinate> kafkaTemplate;
    private List<String> vehicleData;
    @Value("${spring.kafka.properties.schema.registry.url}")
    private String schemaRegistryUrl;

    @BeforeEach
    void setup() {
        vehicleData = new ArrayList<>();

        if(kafkaTemplate == null) {
            kafkaTemplate = createProducer();
        }
    }

    @Test
    void oneVehicleDataReceived_zeroTraveledDistanceSentToKafkaOutputTopic() {
        // GIVEN
        Long id = 1L;

        Coordinate coordinate = Coordinate.newBuilder()
            .setX(12.2)
            .setY(12.2)
            .build();

        String expected = "0.0";

        // WHEN
        kafkaTemplate.send("input", id, coordinate);

        // THEN

        Awaitility.await().atMost(2, TimeUnit.SECONDS).untilAsserted(() -> {
            Assertions.assertEquals(1, vehicleData.size());
            Assertions.assertEquals(expected, vehicleData.get(0));
        });
    }

    @Test
    void vehicleDatasReceived_correctTraveledDistanceSentToKafka() {
        // GIVEN
        Long id = 1L;

        Coordinate coordinate1 = Coordinate.newBuilder()
            .setX(12.2)
            .setY(12.2)
            .build();

        Coordinate coordinate2 = Coordinate.newBuilder()
            .setX(22.2)
            .setY(12.2)
            .build();

        String expected = "10.0";

        // WHEN
        kafkaTemplate.send("input", id, coordinate1);
        kafkaTemplate.send("input", id, coordinate2);

        // THEN
        Awaitility.await().atMost(2, TimeUnit.SECONDS).untilAsserted(() -> {
            Assertions.assertEquals(2, vehicleData.size());
            Assertions.assertEquals(expected, vehicleData.get(1));
        });
    }

    @KafkaListener(topics = "output", groupId = "test-consumer",
        properties = {"bootstrap-servers:${spring.embedded.kafka.brokers}",
        "key.deserializer:org.apache.kafka.common.serialization.LongDeserializer",
        "value.deserializer:org.apache.kafka.common.serialization.StringDeserializer"})
    private void consume(ConsumerRecord<Long, String> record) {

        vehicleData.add(record.value());
    }

    private KafkaTemplate<Long, Coordinate> createProducer() {
        Map<String, Object> producerProps = KafkaTestUtils.producerProps(embeddedKafkaBroker);
        producerProps.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, LongSerializer.class);
        producerProps.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, KafkaAvroSerializer.class);
        producerProps.put("schema.registry.url", schemaRegistryUrl);
        ProducerFactory<Long, Coordinate> producerFactory = new DefaultKafkaProducerFactory<>(producerProps);

        return new KafkaTemplate<>(producerFactory);
    }
}
