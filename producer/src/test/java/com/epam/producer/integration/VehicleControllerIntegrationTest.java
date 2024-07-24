package com.epam.producer.integration;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.epam.producer.model.Coordinate;
import com.epam.producer.model.Vehicle;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.util.Map;
import org.apache.kafka.clients.consumer.Consumer;
import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.clients.consumer.ConsumerRecords;
import org.apache.kafka.common.TopicPartition;
import org.apache.kafka.common.serialization.LongDeserializer;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.kafka.core.DefaultKafkaConsumerFactory;
import org.springframework.kafka.support.serializer.JsonDeserializer;
import org.springframework.kafka.test.EmbeddedKafkaBroker;
import org.springframework.kafka.test.context.EmbeddedKafka;
import org.springframework.kafka.test.utils.KafkaTestUtils;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;

@ExtendWith(SpringExtension.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@ActiveProfiles(value = "test")
@EmbeddedKafka
@AutoConfigureMockMvc
class VehicleControllerIntegrationTest {
    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private ObjectMapper objectMapper;
    @Autowired
    private EmbeddedKafkaBroker embeddedKafkaBroker;
    @Value("${topic}")
    private String topic;
    private Consumer<Long, Coordinate> consumer;
    private Vehicle vehicle;
    private Coordinate coordinate;

    @BeforeEach
    void setup() {
        if(!embeddedKafkaBroker.getTopics().contains(topic)) {
            embeddedKafkaBroker.addTopics(topic);
        }
        if(consumer == null) {
            consumer = createConsumer();
        }

        coordinate = Coordinate.builder()
            .x(12.2)
            .y(12.2)
            .build();

        vehicle = Vehicle.builder()
            .id(1L)
            .coordinate(coordinate)
            .build();
    }

    @AfterEach
    void tearDown() {
        if(consumer != null) {
            consumer.close();
        }
    }

    @Test
    void createVehicle_validVehicleDataProvided_dataSentToKafka() throws Exception {
        // GIVEN
        Map<TopicPartition, Long> endOffsetsBefore = consumer.endOffsets(consumer.assignment());
        long expected = endOffsetsBefore.values().stream().mapToLong(Long::longValue).sum() + 1L;

        // WHEN
        mockMvc.perform(post("/vehicle")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(vehicle)))
            .andExpect(status().isCreated());

        // THEN
        ConsumerRecords<Long, Coordinate> records = KafkaTestUtils.getRecords(consumer);
        ConsumerRecord<Long, Coordinate> record = records.records(topic).iterator().next();

        Map<TopicPartition, Long> endOffsetsAfter = consumer.endOffsets(consumer.assignment());
        long actual = endOffsetsAfter.values().stream().mapToLong(Long::longValue).sum();

        Assertions.assertEquals(expected, actual);
        Assertions.assertEquals(vehicle.getId(), record.key());
        Assertions.assertEquals(coordinate, record.value());
    }

    @Test
    void createVehicle_missingVehicleId_dataNotSentToKafka() throws Exception {
        // GIVEN
        vehicle.setId(null);

        Map<TopicPartition, Long> endOffsetsBefore = consumer.endOffsets(consumer.assignment());
        long expected = endOffsetsBefore.values().stream().mapToLong(Long::longValue).sum();

        // WHEN
        mockMvc.perform(post("/vehicle")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(vehicle)))
            .andExpect(status().isBadRequest());

        // THEN
        Map<TopicPartition, Long> endOffsetsAfter = consumer.endOffsets(consumer.assignment());
        long actual = endOffsetsAfter.values().stream().mapToLong(Long::longValue).sum();

        Assertions.assertEquals(expected, actual);
    }

    @Test
    void createVehicle_missingXCoordinates_dataNotSentToKafka() throws Exception {
        // GIVEN
        coordinate.setX(null);
        vehicle.setCoordinate(coordinate);

        Map<TopicPartition, Long> endOffsetsBefore = consumer.endOffsets(consumer.assignment());
        long expected = endOffsetsBefore.values().stream().mapToLong(Long::longValue).sum();

        // WHEN
        mockMvc.perform(post("/vehicle")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(vehicle)))
            .andExpect(status().isBadRequest());

        // THEN
        Map<TopicPartition, Long> endOffsetsAfter = consumer.endOffsets(consumer.assignment());
        long actual = endOffsetsAfter.values().stream().mapToLong(Long::longValue).sum();

        Assertions.assertEquals(expected, actual);
    }

    @Test
    void createVehicle_missingYCoordinates_dataNotSentToKafka() throws Exception {
        // GIVEN
        coordinate.setY(null);
        vehicle.setCoordinate(coordinate);

        Map<TopicPartition, Long> endOffsetsBefore = consumer.endOffsets(consumer.assignment());
        long expected = endOffsetsBefore.values().stream().mapToLong(Long::longValue).sum();

        // WHEN
        mockMvc.perform(post("/vehicle")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(vehicle)))
            .andExpect(status().isBadRequest());

        // THEN
        Map<TopicPartition, Long> endOffsetsAfter = consumer.endOffsets(consumer.assignment());
        long actual = endOffsetsAfter.values().stream().mapToLong(Long::longValue).sum();

        Assertions.assertEquals(expected, actual);
    }

    @Test
    void createVehicle_vehicleIdIsBelowZero_dataNotSentToKafka() throws Exception {
        // GIVEN
        vehicle.setId(-1L);

        Map<TopicPartition, Long> endOffsetsBefore = consumer.endOffsets(consumer.assignment());
        long expected = endOffsetsBefore.values().stream().mapToLong(Long::longValue).sum();

        // WHEN
        mockMvc.perform(post("/vehicle")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(vehicle)))
            .andExpect(status().isBadRequest());

        // THEN
        Map<TopicPartition, Long> endOffsetsAfter = consumer.endOffsets(consumer.assignment());
        long actual = endOffsetsAfter.values().stream().mapToLong(Long::longValue).sum();

        Assertions.assertEquals(expected, actual);
    }

    private Consumer<Long, Coordinate> createConsumer() {
        Map<String, Object> consumerProps = KafkaTestUtils.consumerProps("testGroup",
            "true", embeddedKafkaBroker);
        consumerProps.put(ConsumerConfig.AUTO_OFFSET_RESET_CONFIG, "earliest");
        consumerProps.put(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, LongDeserializer.class);
        consumerProps.put(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, JsonDeserializer.class);
        consumerProps.put(JsonDeserializer.VALUE_DEFAULT_TYPE, Coordinate.class);
        Consumer<Long, Coordinate> consumer = new DefaultKafkaConsumerFactory<Long, Coordinate>(consumerProps)
            .createConsumer();
        embeddedKafkaBroker.consumeFromAnEmbeddedTopic(consumer, topic);

        return consumer;
    }
}
