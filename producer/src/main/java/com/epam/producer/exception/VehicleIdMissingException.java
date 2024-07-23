package com.epam.producer.exception;

public class VehicleIdMissingException extends RuntimeException {

    public VehicleIdMissingException (String msg) {
        super(msg);
    }
}
