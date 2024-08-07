package com.epam.producer.dto;

import jakarta.validation.constraints.NotNull;
import lombok.Builder;

/**
 * Represents a coordinate with x and y values.
 * <p>
 * This record is used to encapsulate the x and y coordinates, ensuring that neither value is null
 * through validation constraints. It provides a builder for convenient construction.
 * </p>
 */
@Builder
public record CoordinateDto(
    @NotNull(message = "x cannot be null")
    Double x,
    @NotNull(message = "y cannot be null")
    Double y) {
}
