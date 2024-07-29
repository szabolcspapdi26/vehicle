package com.epam.producer.mapper;

import com.epam.producer.dto.CoordinateDto;
import com.epam.producer.model.Coordinate;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface CoordinateMapper {
    Coordinate toCoordinate(CoordinateDto coordinateDto);
}
