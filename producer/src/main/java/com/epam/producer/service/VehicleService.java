package com.epam.producer.service;

import com.epam.producer.model.VehicleModel;

/**
 * Interface for vehicle processing services.
 * <p>
 * This interface defines the contract for services that handle the processing of vehicles.
 * Implementations of this interface are expected to provide specific logic for processing
 * vehicle data, which could include operations such as validation, persistence, or external
 * system integration.
 * </p>
 */
public interface VehicleService {

  /**
   * Processes a given vehicle model.
   * <p>
   * This method is responsible for handling all operations associated with processing a vehicle,
   * such as saving to a database, sending data to external services, or performing business logic
   * validations. Implementations should define the specific behavior.
   * </p>
   *
   * @param vehicleModel The vehicle model to be processed.
   */
  void processVehicle(VehicleModel vehicleModel);
}
