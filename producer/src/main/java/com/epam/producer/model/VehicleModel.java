package com.epam.producer.model;

import lombok.Builder;

/**
 * Represents a model for vehicle data.
 * <p>
 * This record encapsulates vehicle identification and its associated coordinates.
 * It is designed to be immutable and is typically used for transferring vehicle data
 * within the application. The record is equipped with a builder for ease of instantiation.
 * </p>
 */
@Builder
public record VehicleModel(Long id, CoordinateModel coordinate) {
}
