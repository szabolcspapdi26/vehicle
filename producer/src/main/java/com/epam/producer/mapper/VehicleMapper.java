package com.epam.producer.mapper;

import com.epam.producer.dto.VehicleDto;
import com.epam.producer.model.Vehicle;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring", uses = {CoordinateMapper.class})
public interface VehicleMapper {
    @Mapping(source = "vehicleDto.coordinateDto", target = "coordinate")
    Vehicle toVehicle(VehicleDto vehicleDto);
}
