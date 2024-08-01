package com.epam.producer.model;

import lombok.Builder;

@Builder
public record CoordinateModel(Double x, Double y) { }