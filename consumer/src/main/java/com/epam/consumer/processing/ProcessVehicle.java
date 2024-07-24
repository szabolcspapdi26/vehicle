package com.epam.consumer.processing;

import com.epam.consumer.model.Coordinate;

public interface ProcessVehicle {
    void processVehicleData(Long id, Coordinate coordinate);
}
