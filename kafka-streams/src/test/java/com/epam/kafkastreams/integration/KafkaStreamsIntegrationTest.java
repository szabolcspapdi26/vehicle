package com.epam.kafkastreams.integration;

import static org.springframework.test.annotation.DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD;

import com.epam.schema.Coordinate;
import com.epam.schema.Vehicle;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.awaitility.Awaitility;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.test.EmbeddedKafkaBroker;
import org.springframework.kafka.test.context.EmbeddedKafka;
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
  @Autowired
  private KafkaTemplate<Long, Coordinate> kafkaTemplate;

  @BeforeEach
  void setup() {
    vehicleRecords = new ArrayList<>();
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
}
