package com.epam.kafkastreams.topology;

import static io.confluent.kafka.serializers.AbstractKafkaSchemaSerDeConfig.SCHEMA_REGISTRY_URL_CONFIG;

import com.epam.schema.Coordinate;
import com.epam.schema.Vehicle;
import io.confluent.kafka.streams.serdes.json.KafkaJsonSchemaSerde;
import java.util.HashMap;
import java.util.Map;
import org.apache.kafka.common.serialization.Serdes;
import org.apache.kafka.streams.StreamsBuilder;
import org.apache.kafka.streams.TestInputTopic;
import org.apache.kafka.streams.TestOutputTopic;
import org.apache.kafka.streams.TopologyTestDriver;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class VehicleTopologyTest {
  private TestInputTopic<Long, Coordinate> testInputTopic;
  private TestOutputTopic<Long, Vehicle> testOutputTopic;
  private TopologyTestDriver topologyTestDriver;
  private final VehicleTopology vehicleTopology = new VehicleTopology();
  private KafkaJsonSchemaSerde<Coordinate> inputSerde;
  private KafkaJsonSchemaSerde<Vehicle> outputSerde;
  private String schemaRegistryUrl = "mock://not-used";

  @BeforeEach
  void setup() {
    Map<String, Object> serdeConfig = new HashMap<>();
    serdeConfig.put(SCHEMA_REGISTRY_URL_CONFIG, schemaRegistryUrl);

    inputSerde = new KafkaJsonSchemaSerde<>(Coordinate.class);
    inputSerde.configure(serdeConfig, false);

    outputSerde = new KafkaJsonSchemaSerde<>(Vehicle.class);
    outputSerde.configure(serdeConfig, false);

    StreamsBuilder streamsBuilder = new StreamsBuilder();
    vehicleTopology.processVehicle(streamsBuilder);

    topologyTestDriver = new TopologyTestDriver(streamsBuilder.build());

    testInputTopic = topologyTestDriver.createInputTopic("input",
      Serdes.Long().serializer(), inputSerde.serializer());
    testOutputTopic = topologyTestDriver.createOutputTopic("output",
      Serdes.Long().deserializer(), outputSerde.deserializer());
  }

  @AfterEach
  void tearDown() {
    topologyTestDriver.close();
  }

  @Test
  void processVehicle_xCoordinateIsHigherThanTen_dataSentToOutputTopic() {
    // GIVEN
    Coordinate coordinate = new Coordinate();
    coordinate.setX(12.2);
    coordinate.setY(12.2);
    Long id = 1L;

    Vehicle expectedVehicle = new Vehicle();
    expectedVehicle.setId(id);
    expectedVehicle.setX(12.2);
    expectedVehicle.setY(12.2);

    // WHEN
    testInputTopic.pipeInput(id, coordinate);

    // THEN
    Assertions.assertEquals(1, testOutputTopic.getQueueSize());
    var listOfRecords = testOutputTopic.readRecordsToList();
    Assertions.assertEquals(id, listOfRecords.get(0).getKey());
    Assertions.assertEquals(expectedVehicle, listOfRecords.get(0).getValue());
  }

  @Test
  void processVehicle_xCoordinateIsLowerThanTen_outputTopicShouldBeEmpty() {
    // GIVEN
    Coordinate coordinate = new Coordinate();
    coordinate.setX(9.1);
    coordinate.setY(12.2);
    Long id = 1L;

    Long expectedTopicSize = 0L;

    // WHEN
    testInputTopic.pipeInput(id, coordinate);

    // THEN
    Assertions.assertEquals(expectedTopicSize, testOutputTopic.getQueueSize());
  }
}
