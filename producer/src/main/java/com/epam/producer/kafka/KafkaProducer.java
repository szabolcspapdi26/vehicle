package com.epam.producer.kafka;

import com.epam.producer.mapper.VehicleMapper;
import com.epam.producer.model.VehicleModel;
import com.epam.schema.Coordinate;
import lombok.RequiredArgsConstructor;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

/**
 * Kafka producer for sending vehicle data to a specified Kafka topic.
 * <p>
 * This class handles the production of vehicle messages to a
 * Kafka topic using the {@link KafkaTemplate}.
 * It converts vehicle model data into a coordinate schema format before sending.
 * </p>
 */
@Component
@RequiredArgsConstructor
public class KafkaProducer {
  private final KafkaTemplate<Long, Coordinate> kafkaTemplate;
  private final VehicleMapper vehicleMapper;

  /**
   * Sends a vehicle's data to the Kafka topic named "input".
   * <p>
   * This method takes a {@link VehicleModel}, extracts and transforms its coordinate data,
   * and then sends it to the Kafka topic. The vehicle's ID is used as the key for the Kafka message
   * to ensure that all messages for the same vehicle are sent to the same partition.
   * </p>
   *
   * @param vehicle The vehicle data to send to Kafka.
   */
  public void sendVehicle(VehicleModel vehicle) {
    kafkaTemplate.send("input", vehicle.id(),
        vehicleMapper.toCoordinateSchema(vehicle.coordinate()));
  }
}
