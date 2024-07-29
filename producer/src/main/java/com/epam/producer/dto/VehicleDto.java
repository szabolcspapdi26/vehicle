package com.epam.producer.dto;

import jakarta.validation.Valid;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.Builder;

@Builder
public record VehicleDto(
    @NotNull(message = "id cannot be null")
    @Min(value = 0, message = "id cannot be less than 0")
    Long id,
    @Valid
    CoordinateDto coordinateDto) {
}
