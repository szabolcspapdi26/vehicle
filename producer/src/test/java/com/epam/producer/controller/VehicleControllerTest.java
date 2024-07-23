package com.epam.producer.controller;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.catchThrowable;

import com.epam.producer.controller.validator.VehicleValidator;
import com.epam.producer.exception.VehicleCoordinatesXOrYMissingException;
import com.epam.producer.exception.VehicleIdIsBelowZeroException;
import com.epam.producer.exception.VehicleIdMissingException;
import com.epam.producer.model.Coordinates;
import com.epam.producer.model.Vehicle;
import com.epam.producer.service.VehicleService;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

@ExtendWith(MockitoExtension.class)
class VehicleControllerTest {
    @Mock
    private VehicleService vehicleService;
    @Mock
    private VehicleValidator vehicleValidator;
    @InjectMocks
    private VehicleController vehicleController;

    @Test
    void createVehicleData_validVehicleDataProvided_createdResponseReturned() {
        // GIVEN
        Coordinates coordinates = Coordinates.builder()
            .x(12.2)
            .y(12.2)
            .build();

        Vehicle vehicle = Vehicle.builder()
            .id(1L)
            .coordinates(coordinates)
            .build();

        var expected = ResponseEntity.status(HttpStatus.CREATED).build();

        // WHEN
        var actual = vehicleController.createVehicleData(vehicle);

        // THEN
        Mockito.verify(vehicleService).processVehicle(vehicle);
        Mockito.verify(vehicleValidator).validateVehicle(vehicle);
        Assertions.assertEquals(actual, expected);
    }

    @Test
    void createVehicleData_missingVehicleId_vehicleIdMissingExceptionThrown() {
        // GIVEN
        Coordinates coordinates = Coordinates.builder()
            .x(12.2)
            .y(12.2)
            .build();

        Vehicle vehicle = Vehicle.builder()
            .id(null)
            .coordinates(coordinates)
            .build();

        Mockito.doThrow(VehicleIdMissingException.class)
            .when(vehicleValidator).validateVehicle(vehicle);

        // WHEN
        Throwable thrown = catchThrowable(() -> vehicleController.createVehicleData(vehicle));

        // THEN
        assertThat(thrown).isInstanceOf(VehicleIdMissingException.class);
    }

    @Test
    void createVehicleData_missingXCoordinates_vehicleCoordinatesXOrYMissingExceptionThrown() {
        // GIVEN
        Coordinates coordinates = Coordinates.builder()
            .x(null)
            .y(12.2)
            .build();

        Vehicle vehicle = Vehicle.builder()
            .id(1L)
            .coordinates(coordinates)
            .build();

        Mockito.doThrow(VehicleCoordinatesXOrYMissingException.class)
            .when(vehicleValidator).validateVehicle(vehicle);

        // WHEN
        Throwable thrown = catchThrowable(() -> vehicleController.createVehicleData(vehicle));

        // THEN
        assertThat(thrown).isInstanceOf(VehicleCoordinatesXOrYMissingException.class);
    }

    @Test
    void createVehicleData_missingYCoordinates_vehicleCoordinatesXOrYMissingExceptionThrown() {
        // GIVEN
        Coordinates coordinates = Coordinates.builder()
            .x(12.2)
            .y(null)
            .build();

        Vehicle vehicle = Vehicle.builder()
            .id(1L)
            .coordinates(coordinates)
            .build();

        Mockito.doThrow(VehicleCoordinatesXOrYMissingException.class)
            .when(vehicleValidator).validateVehicle(vehicle);

        // WHEN
        Throwable thrown = catchThrowable(() -> vehicleController.createVehicleData(vehicle));

        // THEN
        assertThat(thrown).isInstanceOf(VehicleCoordinatesXOrYMissingException.class);
    }

    @Test
    void createVehicleData_vehicleIdIsBelowZero_vehicleIdIsBelowZeroExceptionThrown() {
        // GIVEN
        Coordinates coordinates = Coordinates.builder()
            .x(12.2)
            .y(12.2)
            .build();

        Vehicle vehicle = Vehicle.builder()
            .id(-1L)
            .coordinates(coordinates)
            .build();

        Mockito.doThrow(VehicleIdIsBelowZeroException.class)
            .when(vehicleValidator).validateVehicle(vehicle);

        // WHEN
        Throwable thrown = catchThrowable(() -> vehicleController.createVehicleData(vehicle));

        // THEN
        assertThat(thrown).isInstanceOf(VehicleIdIsBelowZeroException.class);
    }
}
