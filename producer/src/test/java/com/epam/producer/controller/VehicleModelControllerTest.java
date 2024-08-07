package com.epam.producer.controller;

import com.epam.producer.dto.CoordinateDto;
import com.epam.producer.dto.VehicleDto;
import com.epam.producer.mapper.VehicleMapper;
import com.epam.producer.model.CoordinateModel;
import com.epam.producer.model.VehicleModel;
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
class VehicleModelControllerTest {
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

    CoordinateModel coordinateModel = new CoordinateModel(12.2, 12.2);

    VehicleModel vehicleModel = new VehicleModel(1L, coordinateModel);

    Mockito.when(vehicleMapper.toVehicleModel(vehicleDto)).thenReturn(vehicleModel);

    var expected = ResponseEntity.status(HttpStatus.CREATED).build();

    // WHEN
    var actual = vehicleController.createVehicleData(vehicleDto);

    // THEN
    Mockito.verify(vehicleMapper).toVehicleModel(vehicleDto);
    Mockito.verify(vehicleService).processVehicle(vehicleModel);
    Assertions.assertEquals(actual, expected);
  }
}
