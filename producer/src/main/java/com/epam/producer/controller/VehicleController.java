package com.epam.producer.controller;

import com.epam.producer.dto.VehicleDto;
import com.epam.producer.mapper.VehicleMapper;
import com.epam.producer.model.VehicleModel;
import com.epam.producer.service.VehicleService;
import jakarta.validation.Valid;
import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.support.DefaultMessageSourceResolvable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

/**
 * Controller for handling vehicle data operations.
 */
@RestController
@RequiredArgsConstructor
@Slf4j
public class VehicleController {
  private final VehicleService vehicleService;
  private final VehicleMapper vehicleMapper;
  @Value("${anyvariable}")
  private String anyVariable;

  /**
   * Creates a new vehicle entry to kafka based on the provided vehicle data.
   *
   * @param vehicleDto the vehicle data transfer object containing vehicle details
   * @return a ResponseEntity indicating the result of the creation operation
   */
  @PostMapping(path = "/vehicle")
  public ResponseEntity<Void> createVehicleData(@RequestBody @Valid VehicleDto vehicleDto) {

    log.info("Called createVehicleData with {}, {}", vehicleDto, anyVariable);

    VehicleModel vehicleModel = vehicleMapper.toVehicleModel(vehicleDto);

    vehicleService.processVehicle(vehicleModel);

    return ResponseEntity.status(HttpStatus.CREATED).build();
  }

  /**
   * Handles the case where an argument annotated with {@code @Valid} fails validation.
   *
   * @param exception the exception that captures what went wrong during
   *     the method argument validation
   * @return a ResponseEntity containing the validation error messages
   */
  @ExceptionHandler(MethodArgumentNotValidException.class)
  public ResponseEntity<Object> handleInvalidDto(MethodArgumentNotValidException exception) {
    List<String> errors = exception.getBindingResult().getFieldErrors().stream()
                                    .map(DefaultMessageSourceResolvable::getDefaultMessage)
                                    .toList();
    return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errors);
  }
}
