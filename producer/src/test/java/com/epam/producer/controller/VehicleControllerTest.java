package com.epam.producer.controller;

import com.epam.producer.dto.CoordinateDto;
import com.epam.producer.dto.VehicleDto;
import com.epam.producer.mapper.VehicleMapper;
import com.epam.producer.model.Coordinate;
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
    private VehicleMapper vehicleMapper;
    @Mock
    private VehicleService vehicleService;
    @InjectMocks
    private VehicleController vehicleController;

    @Test
    void createVehicleData_validVehicleDataProvided_createdResponseReturned() {
        // GIVEN
        CoordinateDto coordinateDto = new CoordinateDto(12.2, 12.2);

        VehicleDto vehicleDto = new VehicleDto(1L, coordinateDto);

        Coordinate coordinate = new Coordinate(12.2, 12.2);

        Vehicle vehicle = new Vehicle(1L, coordinate);

        Mockito.when(vehicleMapper.toVehicle(vehicleDto)).thenReturn(vehicle);

        var expected = ResponseEntity.status(HttpStatus.CREATED).build();

        // WHEN
        var actual = vehicleController.createVehicleData(vehicleDto);

        // THEN
        Mockito.verify(vehicleMapper).toVehicle(vehicleDto);
        Mockito.verify(vehicleService).processVehicle(vehicle);
        Assertions.assertEquals(actual, expected);
    }
}
