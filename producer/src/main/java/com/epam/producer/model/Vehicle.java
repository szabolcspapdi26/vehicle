package com.epam.producer.model;

import lombok.Builder;

@Builder
public record Vehicle (long id, Coordinate coordinate) { }
