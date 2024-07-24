package com.epam.consumer.processing;

import com.epam.consumer.model.Coordinate;
import com.epam.consumer.producer.Producer;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class ProcessVehicleImplTest {
    @Mock
    private Producer producer;
    @InjectMocks
    private ProcessVehicleImpl processVehicle;
    private Long id;
    private Coordinate coordinate;

    @BeforeEach
    void setup() {
        id = 1L;

        coordinate = Coordinate.builder()
            .x(12.2)
            .y(12.2)
            .build();
    }

    @Test
    void processVehicleData_idAndCoordinateGiven_producerLogicCalledWithCorrectTraveledDistance() {
        // GIVEN
        double expected = 0.0;

        // WHEN
        processVehicle.processVehicleData(id, coordinate);

        // THEN
        Mockito.verify(producer).send(id, expected);
    }

    @Test
    void getTraveledDistance_onlyOneCoordinateForVehicleId_ZeroTraveledDistanceReturned() {
        // GIVEN
        double expected = 0.0;

        // WHEN
        var actual = processVehicle.getTraveledDistance(id, coordinate);

        // THEN
        Assertions.assertEquals(expected, actual);
    }

    @Test
    void getTraveledDistance_moreThanOneCoordinateForVehicleId_TraveledDistanceReturned() {
        // GIVEN
        List<Coordinate> initialCoordinates = new ArrayList<>();
        initialCoordinates.add(coordinate);

        Map<Long, List<Coordinate>> vehicleData = new HashMap<>();
        vehicleData.put(id, initialCoordinates);

        processVehicle.setVehicleData(vehicleData);

        Coordinate newCoordinate = Coordinate.builder()
            .x(22.2)
            .y(12.2)
            .build();

        double expected = 10.0;

        // WHEN
        var actual = processVehicle.getTraveledDistance(id, newCoordinate);

        // THEN
        Assertions.assertEquals(expected, actual);
    }

    @Test
    void updateVehicleCoordinates_vehicleDataDoesNotExitsYetInMap_coordinateAddedToVehicleDataMap() {
        // GIVEN
        int expectedSize = 1;

        List<Coordinate> expectedCoordinates = new ArrayList<>();
        expectedCoordinates.add(coordinate);

        // WHEN
        processVehicle.updateVehicleCoordinates(id, coordinate);

        // THEN
        Assertions.assertEquals(expectedSize, processVehicle.getVehicleData().get(id).size());
        Assertions.assertEquals(expectedCoordinates, processVehicle.getVehicleData().get(id));
    }

    @Test
    void updateVehicleCoordinates_vehicleDataAlreadyExitsInMap_coordinateAddedToVehicleDataMap() {
        // GIVEN
        List<Coordinate> initialCoordinates = new ArrayList<>();
        initialCoordinates.add(coordinate);

        Map<Long, List<Coordinate>> vehicleData = new HashMap<>();

        vehicleData.put(id, initialCoordinates);

        processVehicle.setVehicleData(vehicleData);

        Coordinate newCoordinate = Coordinate.builder()
            .x(22.2)
            .y(12.2)
            .build();

        int expectedSize = 2;

        List<Coordinate> expectedCoordinates = new ArrayList<>();
        expectedCoordinates.add(coordinate);
        expectedCoordinates.add(newCoordinate);

        // WHEN
        processVehicle.updateVehicleCoordinates(id, newCoordinate);

        // THEN
        Assertions.assertEquals(expectedSize, processVehicle.getVehicleData().get(id).size());
        Assertions.assertEquals(expectedCoordinates, processVehicle.getVehicleData().get(id));
    }

    @Test
    void calculateTraveledDistance_coordinateListOfSizeThreeGiven_traveledDistanceReturned() {
        // GIVEN
        Coordinate coordinate2 = Coordinate.builder()
            .x(22.2)
            .y(12.2)
            .build();
        Coordinate coordinate3 = Coordinate.builder()
            .x(22.2)
            .y(22.2)
            .build();

        List<Coordinate> coordinates = new ArrayList<>();
        coordinates.add(coordinate);
        coordinates.add(coordinate2);
        coordinates.add(coordinate3);

        double expected = 20.0;

        // WHEN
        var actual = processVehicle.calculateTraveledDistance(coordinates);

        // THEN
        Assertions.assertEquals(expected, actual);
    }

    @Test
    void calculateTraveledDistanceFromCoordinates_twoCoordinatesGiven_traveledDistanceReturned() {
        // GIVEN
        Coordinate actualCoordinate = Coordinate.builder()
            .x(22.2)
            .y(12.2)
            .build();

        double expected = 10.0;

        // WHEN
        double actual = processVehicle.calculateTraveledDistanceFromCoordinates(coordinate,
            actualCoordinate);

        // THEN
        Assertions.assertEquals(expected, actual);
    }
}
