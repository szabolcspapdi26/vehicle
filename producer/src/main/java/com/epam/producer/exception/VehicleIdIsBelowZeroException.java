package com.epam.producer.exception;

public class VehicleIdIsBelowZeroException extends RuntimeException {

    public VehicleIdIsBelowZeroException(String msg) {
        super(msg);
    }
}
