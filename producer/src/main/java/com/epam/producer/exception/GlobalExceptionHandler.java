package com.epam.producer.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(VehicleIdMissingException.class)
    public ResponseEntity<String> handleVehicleIdMissingException(VehicleIdMissingException exception) {

        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(exception.getMessage());
    }

    @ExceptionHandler(VehicleCoordinatesXOrYMissingException.class)
    public ResponseEntity<String> handleVehicleCoordinatesXOrYMissingException(
        VehicleCoordinatesXOrYMissingException exception) {

        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(exception.getMessage());
    }

    @ExceptionHandler(VehicleIdIsBelowZeroException.class)
    public ResponseEntity<String> handleVehicleIdIsBelowZeroException(VehicleIdIsBelowZeroException exception) {

        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(exception.getMessage());
    }
}
