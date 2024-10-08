package com.epam.kafkastreams.topology;

import com.epam.schema.Coordinate;
import com.epam.schema.Vehicle;
import io.confluent.kafka.streams.serdes.json.KafkaJsonSchemaSerde;
import lombok.RequiredArgsConstructor;
import org.apache.kafka.common.serialization.Serdes;
import org.apache.kafka.streams.KeyValue;
import org.apache.kafka.streams.StreamsBuilder;
import org.apache.kafka.streams.kstream.Consumed;
import org.apache.kafka.streams.kstream.Produced;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * Defines the Kafka Streams topology for processing vehicle data.
 * <p>
 * This class sets up a stream processing topology using Kafka Streams to process incoming vehicle
 * coordinate data. The topology reads from the "input" topic, processes the data based on specific
 * conditions and transformations, and outputs the processed data to the "output" topic.
 * </p>
 */
@Component
@RequiredArgsConstructor
public class VehicleTopology {
  private final KafkaJsonSchemaSerde<Coordinate> inputSerde;
  private final KafkaJsonSchemaSerde<Vehicle> outputSerde;
  /**
   * Sets up the stream processing topology for vehicle data.
   * <p>
   * This method configures the Kafka Streams topology to read
   *    vehicle coordinate data from the "input" topic,
   * filter out coordinates where x is less than or equal to 10.0,
   *    and transform the filtered data into
   * {@link Vehicle} objects. It then sends the transformed data to the "output" topic.
   * </p>
   *
   * @param streamsBuilder the {@link StreamsBuilder} used to define and build the stream topology.
   */

  @Autowired
  public void processVehicle(StreamsBuilder streamsBuilder) {
    streamsBuilder
      .stream("input", Consumed.with(Serdes.Long(), inputSerde))
      .filter((key, value) -> value.getX() > 10.0)
      .map((key, value) -> {
        Vehicle vehicle = new Vehicle();
        vehicle.setId(key);
        vehicle.setX(value.getX());
        vehicle.setY(value.getY());

        return KeyValue.pair(key, vehicle);
      })
        .to("output", Produced.with(Serdes.Long(), outputSerde));
  }
}
