package com.epam.consumer.mapper;

import com.epam.consumer.model.CoordinateModel;
import com.epam.schema.Coordinate;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface VehicleMapper {
    CoordinateModel toCoordinateModel(Coordinate coordinate);
}
