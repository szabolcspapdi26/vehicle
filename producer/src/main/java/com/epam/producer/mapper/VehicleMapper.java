package com.epam.producer.mapper;

import com.epam.producer.dto.CoordinateDto;
import com.epam.producer.dto.VehicleDto;
import com.epam.producer.model.CoordinateModel;
import com.epam.producer.model.VehicleModel;
import com.epam.schema.Coordinate;
import org.mapstruct.Mapper;

/**
 * Mapper interface for converting between various vehicle and coordinate related data models.
 * <p>
 * This interface is used to map data between DTO, model classes, and schema objects
 * used in messaging systems like Kafka. It leverages MapStruct for
 * automatic code generation which simplifies
 * the implementation by generating the mapping code at compile-time.
 * </p>
 */
@Mapper(componentModel = "spring")
public interface VehicleMapper {
  /**
   * Converts a {@link VehicleDto} to a {@link VehicleModel}.
   *
   * @param vehicleDto the vehicle DTO to be converted
   * @return the corresponding vehicle model
   */
  VehicleModel toVehicleModel(VehicleDto vehicleDto);

  /**
   * Converts a {@link CoordinateDto} to a {@link CoordinateModel}.
   *
   * @param coordinate the coordinate DTO to be converted
   * @return the corresponding coordinate model
   */
  CoordinateModel toCoordinateModel(CoordinateDto coordinate);

  /**
   * Converts a {@link CoordinateModel} to a Kafka {@link Coordinate} schema object.
   *
   * @param coordinateModel the coordinate model to be converted
   * @return the corresponding Kafka Coordinate schema object
   */
  Coordinate toCoordinateSchema(CoordinateModel coordinateModel);
}
