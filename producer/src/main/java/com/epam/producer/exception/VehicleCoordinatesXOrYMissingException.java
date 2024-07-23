package com.epam.producer.exception;

public class VehicleCoordinatesXOrYMissingException extends RuntimeException {

    public VehicleCoordinatesXOrYMissingException (String msg) {
        super(msg);
    }
}
