package com.epam.consumer.processing;

import com.epam.consumer.model.CoordinateModel;
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
    private CoordinateModel coordinateModel;

    @BeforeEach
    void setup() {
        id = 1L;

        coordinateModel = CoordinateModel.builder()
            .x(12.2)
            .y(12.2)
            .build();
    }

    @Test
    void processVehicleData_idAndCoordinateGiven_producerLogicCalledWithCorrectTraveledDistance() {
        // GIVEN
        double expected = 0.0;

        // WHEN
        processVehicle.processVehicleData(id, coordinateModel);

        // THEN
        Mockito.verify(producer).send(id, expected);
    }

    @Test
    void getTraveledDistance_onlyOneCoordinateForVehicleId_ZeroTraveledDistanceReturned() {
        // GIVEN
        double expected = 0.0;

        // WHEN
        var actual = processVehicle.getTraveledDistance(id, coordinateModel);

        // THEN
        Assertions.assertEquals(expected, actual);
    }

    @Test
    void getTraveledDistance_moreThanOneCoordinateForVehicleId_TraveledDistanceReturned() {
        // GIVEN
        List<CoordinateModel> initialCoordinateModels = new ArrayList<>();
        initialCoordinateModels.add(coordinateModel);

        Map<Long, List<CoordinateModel>> vehicleData = new HashMap<>();
        vehicleData.put(id, initialCoordinateModels);

        processVehicle.setVehicleData(vehicleData);

        CoordinateModel newCoordinateModel = CoordinateModel.builder()
            .x(22.2)
            .y(12.2)
            .build();

        double expected = 10.0;

        // WHEN
        var actual = processVehicle.getTraveledDistance(id, newCoordinateModel);

        // THEN
        Assertions.assertEquals(expected, actual);
    }

    @Test
    void updateVehicleCoordinates_vehicleDataDoesNotExitsYetInMap_coordinateAddedToVehicleDataMap() {
        // GIVEN
        int expectedSize = 1;

        List<CoordinateModel> expectedCoordinateModels = new ArrayList<>();
        expectedCoordinateModels.add(coordinateModel);

        // WHEN
        processVehicle.updateVehicleCoordinates(id, coordinateModel);

        // THEN
        Assertions.assertEquals(expectedSize, processVehicle.getVehicleData().get(id).size());
        Assertions.assertEquals(expectedCoordinateModels, processVehicle.getVehicleData().get(id));
    }

    @Test
    void updateVehicleCoordinates_vehicleDataAlreadyExitsInMap_coordinateAddedToVehicleDataMap() {
        // GIVEN
        List<CoordinateModel> initialCoordinateModels = new ArrayList<>();
        initialCoordinateModels.add(coordinateModel);

        Map<Long, List<CoordinateModel>> vehicleData = new HashMap<>();

        vehicleData.put(id, initialCoordinateModels);

        processVehicle.setVehicleData(vehicleData);

        CoordinateModel newCoordinateModel = CoordinateModel.builder()
            .x(22.2)
            .y(12.2)
            .build();

        int expectedSize = 2;

        List<CoordinateModel> expectedCoordinateModels = new ArrayList<>();
        expectedCoordinateModels.add(coordinateModel);
        expectedCoordinateModels.add(newCoordinateModel);

        // WHEN
        processVehicle.updateVehicleCoordinates(id, newCoordinateModel);

        // THEN
        Assertions.assertEquals(expectedSize, processVehicle.getVehicleData().get(id).size());
        Assertions.assertEquals(expectedCoordinateModels, processVehicle.getVehicleData().get(id));
    }

    @Test
    void calculateTraveledDistance_coordinateListOfSizeThreeGiven_traveledDistanceReturned() {
        // GIVEN
        CoordinateModel coordinateModel2 = CoordinateModel.builder()
            .x(22.2)
            .y(12.2)
            .build();
        CoordinateModel coordinateModel3 = CoordinateModel.builder()
            .x(22.2)
            .y(22.2)
            .build();

        List<CoordinateModel> coordinateModels = new ArrayList<>();
        coordinateModels.add(coordinateModel);
        coordinateModels.add(coordinateModel2);
        coordinateModels.add(coordinateModel3);

        double expected = 20.0;

        // WHEN
        var actual = processVehicle.calculateTraveledDistance(coordinateModels);

        // THEN
        Assertions.assertEquals(expected, actual);
    }

    @Test
    void calculateTraveledDistanceFromCoordinates_twoCoordinatesGiven_traveledDistanceReturned() {
        // GIVEN
        CoordinateModel actualCoordinateModel = CoordinateModel.builder()
            .x(22.2)
            .y(12.2)
            .build();

        double expected = 10.0;

        // WHEN
        double actual = processVehicle.calculateTraveledDistanceFromCoordinates(coordinateModel,
            actualCoordinateModel);

        // THEN
        Assertions.assertEquals(expected, actual);
    }
}
