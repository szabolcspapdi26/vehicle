package com.epam.producer.controller.validator;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.catchThrowable;

import com.epam.producer.exception.VehicleCoordinatesXOrYMissingException;
import com.epam.producer.exception.VehicleIdIsBelowZeroException;
import com.epam.producer.exception.VehicleIdMissingException;
import com.epam.producer.model.Coordinates;
import com.epam.producer.model.Vehicle;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

class VehicleValidatorImplTest {
    private static VehicleValidatorImpl vehicleValidator;

    @BeforeAll
    static void setup() {
        vehicleValidator = new VehicleValidatorImpl();
    }

    @Test
    void validateVehicle_missingVehicleId_vehicleIdMissingExceptionThrown() {
        // GIVEN
        Coordinates coordinates = Coordinates.builder()
            .x(12.2)
            .y(12.2)
            .build();

        Vehicle vehicle = Vehicle.builder()
            .id(null)
            .coordinates(coordinates)
            .build();

        // WHEN
        Throwable thrown = catchThrowable(() -> vehicleValidator.validateVehicle(vehicle));

        // THEN
        assertThat(thrown).isInstanceOf(VehicleIdMissingException.class);
    }

    @Test
    void validateVehicle_missingXCoordinates_vehicleCoordinatesXOrYMissingExceptionThrown() {
        // GIVEN
        Coordinates coordinates = Coordinates.builder()
            .x(null)
            .y(12.2)
            .build();

        Vehicle vehicle = Vehicle.builder()
            .id(1L)
            .coordinates(coordinates)
            .build();

        // WHEN
        Throwable thrown = catchThrowable(() -> vehicleValidator.validateVehicle(vehicle));

        // THEN
        assertThat(thrown).isInstanceOf(VehicleCoordinatesXOrYMissingException.class);
    }

    @Test
    void validateVehicle_missingYCoordinates_vehicleCoordinatesXOrYMissingExceptionThrown() {
        // GIVEN
        Coordinates coordinates = Coordinates.builder()
            .x(12.2)
            .y(null)
            .build();

        Vehicle vehicle = Vehicle.builder()
            .id(1L)
            .coordinates(coordinates)
            .build();

        // WHEN
        Throwable thrown = catchThrowable(() -> vehicleValidator.validateVehicle(vehicle));

        // THEN
        assertThat(thrown).isInstanceOf(VehicleCoordinatesXOrYMissingException.class);
    }

    @Test
    void validateVehicle_vehicleIdIsBelowZero_vehicleIdIsBelowZeroExceptionThrown() {
        // GIVEN
        Coordinates coordinates = Coordinates.builder()
            .x(12.2)
            .y(12.2)
            .build();

        Vehicle vehicle = Vehicle.builder()
            .id(-1L)
            .coordinates(coordinates)
            .build();

        // WHEN
        Throwable thrown = catchThrowable(() -> vehicleValidator.validateVehicle(vehicle));

        // THEN
        assertThat(thrown).isInstanceOf(VehicleIdIsBelowZeroException.class);
    }
}
