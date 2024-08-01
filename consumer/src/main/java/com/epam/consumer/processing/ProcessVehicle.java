package com.epam.consumer.processing;

import com.epam.consumer.model.CoordinateModel;

public interface ProcessVehicle {
    void processVehicleData(Long id, CoordinateModel coordinateModel);
}
