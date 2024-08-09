package com.epam.kafkastreams.integration;

import static org.springframework.test.annotation.DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD;

import com.epam.schema.Coordinate;
import com.epam.schema.Vehicle;
import io.confluent.kafka.serializers.json.KafkaJsonSchemaSerializer;
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
@SpringBootTest
@ActiveProfiles(value = "test")
@EmbeddedKafka(topics = {"input", "output"})
@DirtiesContext(classMode = AFTER_EACH_TEST_METHOD)
class KafkaStreamsIntegrationTest {
  private List<Vehicle> vehicleRecords;
  @Autowired
  private EmbeddedKafkaBroker embeddedKafkaBroker;

  private KafkaTemplate<Long, Coordinate> kafkaTemplate;
  @Value("${spring.kafka.properties.schema.registry.url}")
  private String schemaRegistryUrl;

  @BeforeEach
  void setup() {
    vehicleRecords = new ArrayList<>();

    if (kafkaTemplate == null) {
      kafkaTemplate = createProducer();
    }
  }

  @Test
  void kafkaStreamsTest_AddingaRecordWithxCoordinateHigherThanTen_OutputTopicShouldHaveOneRecord() {
    // GIVEN
    Long id = 1L;
    Coordinate coordinate = new Coordinate();
    coordinate.setX(12.2);
    coordinate.setY(12.2);

    Vehicle expectedVehicle = new Vehicle();
    expectedVehicle.setId(id);
    expectedVehicle.setX(12.2);
    expectedVehicle.setY(12.2);

    // WHEN
    kafkaTemplate.send("input", id, coordinate);

    // THEN
    Awaitility.await().atMost(2, TimeUnit.SECONDS).untilAsserted(() -> {
      Assertions.assertEquals(1, vehicleRecords.size());
      Assertions.assertEquals(expectedVehicle, vehicleRecords.get(0));
    });
  }

  @Test
  void kafkaStreamsTest_AddingaRecordWithxCoordinateIsLowerThanTen_ConsumerDontReceiveData() {
    // GIVEN
    Long id = 2L;
    Coordinate coordinate = new Coordinate();
    coordinate.setX(9.2);
    coordinate.setY(12.2);

    int expectedSize = 0;

    // WHEN
    kafkaTemplate.send("input", id, coordinate);

    // THEN
    Awaitility.await().atMost(2, TimeUnit.SECONDS).untilAsserted(() -> {
      Assertions.assertEquals(expectedSize, vehicleRecords.size());
    });
  }

  @KafkaListener(topics = "output", groupId = "test-group")
  void consume(ConsumerRecord<Long, Vehicle> record) {
    vehicleRecords.add(record.value());
  }

  private KafkaTemplate<Long, Coordinate> createProducer() {
    Map<String, Object> producerProps = KafkaTestUtils.producerProps(embeddedKafkaBroker);
    producerProps.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, LongSerializer.class);
    producerProps.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG,
        KafkaJsonSchemaSerializer.class);
    producerProps.put("schema.registry.url", schemaRegistryUrl);
    ProducerFactory<Long, Coordinate> producerFactory =
        new DefaultKafkaProducerFactory<>(producerProps);

    return new KafkaTemplate<>(producerFactory);
  }
}
