package com.epam.producer.controller.validator;

import com.epam.producer.exception.VehicleCoordinatesXOrYMissingException;
import com.epam.producer.exception.VehicleIdIsBelowZeroException;
import com.epam.producer.exception.VehicleIdMissingException;
import com.epam.producer.model.Vehicle;
import org.springframework.stereotype.Component;

@Component
public class VehicleValidatorImpl implements VehicleValidator {

    @Override
    public void validateVehicle(Vehicle vehicle) {
        if(vehicle.getId() == null) {
            throw new VehicleIdMissingException("Missing vehicle id!");
        } else if(vehicle.getCoordinate().getX() == null || vehicle.getCoordinate().getY() == null) {
            throw new VehicleCoordinatesXOrYMissingException("X or Y coordinates missing!");
        } else if(vehicle.getId() < 0L) {
            throw new VehicleIdIsBelowZeroException("Vehicle id cannot be less than zero!");
        }
    }
}
