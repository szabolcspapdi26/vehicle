package com.epam.producer.model;

import lombok.Builder;

@Builder
public record Vehicle (Long id, Coordinate coordinate) { }
