package com.epam.consumer.processing;

import com.epam.consumer.model.Coordinate;
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
    private Map<Long, List<Coordinate>> vehicleData = new HashMap<>();
    private final Producer producer;

    @Override
    public void processVehicleData(Long id, Coordinate coordinate) {
        double traveledDistance = getTraveledDistance(id, coordinate);
        producer.send(id, traveledDistance);
    }

    double getTraveledDistance(Long id, Coordinate coordinate) {
        updateVehicleCoordinates(id, coordinate);
        List<Coordinate> coordinates = vehicleData.get(id);

        double traveledDistance = 0.0;

        if(coordinates.size() > 1) {
            traveledDistance = calculateTraveledDistance(coordinates);
        }

        return traveledDistance;
    }

    void updateVehicleCoordinates(Long id, Coordinate coordinate) {
        List<Coordinate> coordinates;
        if(vehicleData.containsKey(id)) {
            coordinates = vehicleData.get(id);
        } else {
            coordinates = new ArrayList<>();
        }
        coordinates.add(coordinate);
        vehicleData.put(id, coordinates);
    }

    double calculateTraveledDistance(List<Coordinate> coordinates) {
        double traveledDistance = 0.0;

        for (int i = 1; i < coordinates.size(); i++) {
            Coordinate previous = coordinates.get(i - 1);
            Coordinate actual = coordinates.get(i);
            traveledDistance += calculateTraveledDistanceFromCoordinates(previous, actual);
        }

        return traveledDistance;
    }

    double calculateTraveledDistanceFromCoordinates(Coordinate previous, Coordinate actual) {

        return Math.sqrt(Math.pow((actual.x() - previous.x()), 2) +
            Math.pow((actual.y() - previous.y()), 2));
    }

    public Map<Long, List<Coordinate>> getVehicleData() {

        return Collections.unmodifiableMap(vehicleData);
    }
}
