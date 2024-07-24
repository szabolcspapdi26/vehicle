package com.epam.producer.controller.validator;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.catchThrowable;

import com.epam.producer.exception.VehicleCoordinatesXOrYMissingException;
import com.epam.producer.exception.VehicleIdIsBelowZeroException;
import com.epam.producer.exception.VehicleIdMissingException;
import com.epam.producer.model.Coordinate;
import com.epam.producer.model.Vehicle;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class VehicleValidatorImplTest {
    private static VehicleValidatorImpl vehicleValidator;
    private Coordinate coordinate;
    private Vehicle vehicle;

    @BeforeAll
    static void setupBeforeAll() {
        vehicleValidator = new VehicleValidatorImpl();
    }

    @BeforeEach
    void setupBeforeEach() {
         coordinate = Coordinate.builder()
            .x(12.2)
            .y(12.2)
            .build();

         vehicle = Vehicle.builder()
            .id(1L)
            .coordinate(coordinate)
            .build();
    }

    @Test
    void validateVehicle_missingVehicleId_vehicleIdMissingExceptionThrown() {
        // GIVEN
        vehicle.setId(null);

        // WHEN
        Throwable thrown = catchThrowable(() -> vehicleValidator.validateVehicle(vehicle));

        // THEN
        assertThat(thrown).isInstanceOf(VehicleIdMissingException.class);
    }

    @Test
    void validateVehicle_missingXCoordinates_vehicleCoordinatesXOrYMissingExceptionThrown() {
        // GIVEN
        coordinate.setX(null);
        vehicle.setCoordinate(coordinate);

        // WHEN
        Throwable thrown = catchThrowable(() -> vehicleValidator.validateVehicle(vehicle));

        // THEN
        assertThat(thrown).isInstanceOf(VehicleCoordinatesXOrYMissingException.class);
    }

    @Test
    void validateVehicle_missingYCoordinates_vehicleCoordinatesXOrYMissingExceptionThrown() {
        // GIVEN
        coordinate.setY(null);
        vehicle.setCoordinate(coordinate);

        // WHEN
        Throwable thrown = catchThrowable(() -> vehicleValidator.validateVehicle(vehicle));

        // THEN
        assertThat(thrown).isInstanceOf(VehicleCoordinatesXOrYMissingException.class);
    }

    @Test
    void validateVehicle_vehicleIdIsBelowZero_vehicleIdIsBelowZeroExceptionThrown() {
        // GIVEN
        vehicle.setId(-1L);

        // WHEN
        Throwable thrown = catchThrowable(() -> vehicleValidator.validateVehicle(vehicle));

        // THEN
        assertThat(thrown).isInstanceOf(VehicleIdIsBelowZeroException.class);
    }
}
