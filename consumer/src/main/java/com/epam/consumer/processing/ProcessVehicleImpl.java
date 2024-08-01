package com.epam.consumer.processing;

import com.epam.consumer.model.CoordinateModel;
import com.epam.consumer.producer.Producer;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class ProcessVehicleImpl implements ProcessVehicle {
    @Setter
    private Map<Long, List<CoordinateModel>> vehicleData = new HashMap<>();
    private final Producer producer;

    @Override
    public void processVehicleData(Long id, CoordinateModel coordinateModel) {
        double traveledDistance = getTraveledDistance(id, coordinateModel);
        producer.send(id, traveledDistance);
    }

    double getTraveledDistance(Long id, CoordinateModel coordinateModel) {
        updateVehicleCoordinates(id, coordinateModel);
        List<CoordinateModel> coordinateModels = vehicleData.get(id);

        double traveledDistance = 0.0;

        if(coordinateModels.size() > 1) {
            traveledDistance = calculateTraveledDistance(coordinateModels);
        }

        return traveledDistance;
    }

    void updateVehicleCoordinates(Long id, CoordinateModel coordinateModel) {
        List<CoordinateModel> coordinateModels;
        if(vehicleData.containsKey(id)) {
            coordinateModels = vehicleData.get(id);
        } else {
            coordinateModels = new ArrayList<>();
        }
        coordinateModels.add(coordinateModel);
        vehicleData.put(id, coordinateModels);
    }

    double calculateTraveledDistance(List<CoordinateModel> coordinateModels) {
        double traveledDistance = 0.0;

        for (int i = 1; i < coordinateModels.size(); i++) {
            CoordinateModel previous = coordinateModels.get(i - 1);
            CoordinateModel actual = coordinateModels.get(i);
            traveledDistance += calculateTraveledDistanceFromCoordinates(previous, actual);
        }

        return traveledDistance;
    }

    double calculateTraveledDistanceFromCoordinates(CoordinateModel previous, CoordinateModel actual) {

        return Math.sqrt(Math.pow((actual.x() - previous.x()), 2) +
            Math.pow((actual.y() - previous.y()), 2));
    }

    public Map<Long, List<CoordinateModel>> getVehicleData() {

        return Collections.unmodifiableMap(vehicleData);
    }
}
