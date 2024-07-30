package com.epam.producer.mapper;

import com.epam.producer.dto.CoordinateDto;
import com.epam.producer.dto.VehicleDto;
import com.epam.producer.model.Coordinate;
import com.epam.producer.model.Vehicle;
import org.mapstruct.Mapper;


@Mapper(componentModel = "spring")
public interface VehicleMapper {
    Vehicle toVehicle(VehicleDto vehicleDto);
    Coordinate toCoordinate(CoordinateDto coordinateDto);
}
