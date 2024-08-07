package com.epam.producer.dto;

import jakarta.validation.Valid;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.Builder;

/**
 * Represents a vehicle with a unique identifier and associated coordinates.
 * <p>
 * This record is designed to encapsulate vehicle data including a unique identifier (ID) and
 * its coordinates. It uses validation annotations to ensure data integrity and provides a builder
 * for convenient construction.
 * </p>
 */
@Builder
public record VehicleDto(
    @NotNull(message = "id cannot be null")
    @Min(value = 0, message = "id cannot be less than 0")
    Long id,
    @Valid
    CoordinateDto coordinate) {
}
