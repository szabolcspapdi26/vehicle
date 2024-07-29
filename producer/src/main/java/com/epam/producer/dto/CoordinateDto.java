package com.epam.producer.dto;

import jakarta.validation.constraints.NotNull;
import lombok.Builder;

@Builder
public record CoordinateDto(
    @NotNull(message = "x cannot be null")
    Double x,
    @NotNull(message = "y cannot be null")
    Double y) { }
