package com.epam.producer.mapper;

import com.epam.producer.dto.CoordinateDto;
import com.epam.producer.dto.VehicleDto;
import com.epam.producer.model.CoordinateModel;
import com.epam.producer.model.VehicleModel;
import com.epam.schema.Coordinate;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface VehicleMapper {
    VehicleModel toVehicleModel(VehicleDto vehicleDto);
    CoordinateModel toCoordinateModel(CoordinateDto coordinate);
    Coordinate toCoordinateSchema(CoordinateModel coordinateModel);
}
