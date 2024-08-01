package com.epam.producer.model;

import lombok.Builder;

@Builder
public record VehicleModel(Long id, CoordinateModel coordinate) { }
