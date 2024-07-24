package com.epam.consumer.integration;


import com.epam.consumer.model.Coordinate;
import java.util.Map;
import org.apache.kafka.clients.consumer.Consumer;
import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.clients.consumer.ConsumerRecords;
import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.common.serialization.LongDeserializer;
import org.apache.kafka.common.serialization.LongSerializer;
import org.apache.kafka.common.serialization.StringDeserializer;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.kafka.core.DefaultKafkaConsumerFactory;
import org.springframework.kafka.core.DefaultKafkaProducerFactory;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.core.ProducerFactory;
import org.springframework.kafka.support.serializer.JsonSerializer;
import org.springframework.kafka.test.EmbeddedKafkaBroker;
import org.springframework.kafka.test.context.EmbeddedKafka;
import org.springframework.kafka.test.utils.KafkaTestUtils;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;

@ExtendWith(SpringExtension.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@ActiveProfiles(value = "test")
@EmbeddedKafka
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
class ConsumerIntegrationTest {
    @Autowired
    private EmbeddedKafkaBroker embeddedKafkaBroker;
    private KafkaTemplate<Long, Coordinate> kafkaTemplate;
    private Consumer<Long, String> consumer;
    @Value("${input-topic}")
    private String inputTopic;
    @Value("${output-topic}")
    private String outputTopic;

    @BeforeEach
    void setup() {
        if(!embeddedKafkaBroker.getTopics().contains(outputTopic)) {
            embeddedKafkaBroker.addTopics(outputTopic);
        }
        if(kafkaTemplate == null) {
            kafkaTemplate = createProducer();
        }
        if(consumer == null) {
            consumer = createConsumer();
        }
    }

    @AfterEach
    void tearDown() {
        if(consumer != null) {
            consumer.close();
        }
    }

    @Test
    void oneVehicleDataReceived_zeroTraveledDistanceSentToKafkaOutputTopic() {
        // GIVEN
        Long id = 1L;

        Coordinate coordinate = Coordinate.builder()
            .x(12.2)
            .y(12.2)
            .build();

        String expected = "0.0";

        // WHEN
        kafkaTemplate.send(inputTopic, id, coordinate);

        // THEN
        ConsumerRecords<Long, String> records = KafkaTestUtils.getRecords(consumer);
        ConsumerRecord<Long, String> record = records.iterator().next();

        Assertions.assertEquals(1, records.count());
        Assertions.assertEquals(id, record.key());
        Assertions.assertEquals(expected, record.value());
    }

    @Test
    void vehicleDatasReceived_correctTraveledDistanceSentToKafka() {
        // GIVEN
        Long id = 1L;

        Coordinate coordinate1 = Coordinate.builder()
            .x(12.2)
            .y(12.2)
            .build();

        Coordinate coordinate2 = Coordinate.builder()
            .x(22.2)
            .y(12.2)
            .build();

        String expected = "10.0";

        // WHEN
        kafkaTemplate.send(inputTopic, id, coordinate1);
        kafkaTemplate.send(inputTopic, id, coordinate2);

        // THEN
        ConsumerRecords<Long, String> records = KafkaTestUtils.getRecords(consumer);
        Assertions.assertEquals(2, records.count());

        ConsumerRecord<Long, String> record = null;
        for (ConsumerRecord<Long, String> consumerRecord : records) {
            record = consumerRecord;
        }

        Assertions.assertEquals(id, record.key());
        Assertions.assertEquals(expected, record.value());
    }

    private Consumer<Long, String> createConsumer() {
        Map<String, Object> consumerProps = KafkaTestUtils.consumerProps("testGroup",
            "true", embeddedKafkaBroker);
        consumerProps.put(ConsumerConfig.AUTO_OFFSET_RESET_CONFIG, "earliest");
        consumerProps.put(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, LongDeserializer.class);
        consumerProps.put(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class);
        Consumer<Long, String> consumer = new DefaultKafkaConsumerFactory<Long, String>(consumerProps)
            .createConsumer();
        embeddedKafkaBroker.consumeFromAnEmbeddedTopic(consumer, outputTopic);

        return consumer;
    }

    private KafkaTemplate<Long, Coordinate> createProducer() {
        Map<String, Object> producerProps = KafkaTestUtils.producerProps(embeddedKafkaBroker);
        producerProps.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, LongSerializer.class);
        producerProps.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, JsonSerializer.class);
        ProducerFactory<Long, Coordinate> producerFactory = new DefaultKafkaProducerFactory<>(producerProps);

        return new KafkaTemplate<>(producerFactory);
    }
}
