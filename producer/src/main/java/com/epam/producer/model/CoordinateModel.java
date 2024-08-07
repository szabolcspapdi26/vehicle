package com.epam.producer.model;

import lombok.Builder;

/**
 * Represents a model for coordinate data.
 * <p>
 * This record is a simple data container used for storing the x and y coordinates.
 * It is designed to be immutable and is typically used for transferring coordinate data
 * within the application.
 * </p>
 */
@Builder
public record CoordinateModel(Double x, Double y) {
}