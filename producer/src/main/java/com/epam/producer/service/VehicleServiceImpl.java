package com.epam.producer.service;

import com.epam.producer.kafka.KafkaProducer;
import com.epam.producer.model.VehicleModel;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

/**
 * Service implementation for processing vehicle data.
 * <p>
 * This class implements the {@link VehicleService} interface and provides functionality
 * to process vehicle data by sending it to a Kafka topic. It uses {@link KafkaProducer}
 * to handle the actual data transmission.
 * </p>
 */
@Service
@RequiredArgsConstructor
@Slf4j
public class VehicleServiceImpl implements VehicleService {
  private final KafkaProducer kafkaProducer;

  /**
   * Processes the given vehicle model by sending it to Kafka.
   * <p>
   * This method logs the vehicle model details and delegates the sending of the vehicle data
   * to the Kafka producer. It encapsulates the business logic necessary for preparing and
   * sending the data to the messaging system.
   * </p>
   *
   * @param vehicleModel The vehicle model to be processed and sent.
   */
  @Override
  public void processVehicle(VehicleModel vehicleModel) {

    log.info("Inside processVehicle with {}", vehicleModel);
    kafkaProducer.sendVehicle(vehicleModel);
  }
}
