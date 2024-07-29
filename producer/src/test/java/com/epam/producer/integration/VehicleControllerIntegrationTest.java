package com.epam.producer.integration;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.epam.producer.dto.CoordinateDto;
import com.epam.producer.dto.VehicleDto;
import com.epam.producer.model.Coordinate;
import com.epam.producer.model.Vehicle;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.awaitility.Awaitility;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.test.EmbeddedKafkaBroker;
import org.springframework.kafka.test.context.EmbeddedKafka;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;

@ExtendWith(SpringExtension.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@ActiveProfiles(value = "test")
@EmbeddedKafka(partitions = 1)
@AutoConfigureMockMvc
class VehicleControllerIntegrationTest {
    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private ObjectMapper objectMapper;
    private List<Vehicle> vehicleData;

    @BeforeEach
    void setup() {
        vehicleData = new ArrayList<>();
    }

    @Test
    void createVehicle_validVehicleDataProvided_dataSentToKafka() throws Exception {
        // GIVEN
        CoordinateDto coordinateDto = new CoordinateDto(12.2, 12.2);
        VehicleDto vehicleDto = new VehicleDto(1L, coordinateDto);

        Coordinate expectedCoordinate = new Coordinate(12.2, 12.2);

        // WHEN
        mockMvc.perform(post("/vehicle")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(vehicleDto)))
            .andExpect(status().isCreated());

        // THEN
        Awaitility.await().atMost(10, TimeUnit.SECONDS).untilAsserted(() -> {
            Assertions.assertEquals(1, vehicleData.size());
            Assertions.assertEquals(1L, vehicleData.get(0).id());
            Assertions.assertEquals(expectedCoordinate, vehicleData.get(0).coordinate());
        });
    }

    @Test
    void createVehicle_missingVehicleId_dataNotSentToKafka() throws Exception {
        // GIVEN
        CoordinateDto coordinateDto = new CoordinateDto(12.2, 12.2);
        VehicleDto vehicleDto = new VehicleDto(null, coordinateDto);

        // WHEN
        mockMvc.perform(post("/vehicle")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(vehicleDto)))
            .andExpect(status().isBadRequest());

        // THEN
        Assertions.assertEquals(0, vehicleData.size());
    }

    @Test
    void createVehicle_missingXCoordinates_dataNotSentToKafka() throws Exception {
        // GIVEN
        CoordinateDto coordinateDto = new CoordinateDto(null, 12.2);
        VehicleDto vehicleDto = new VehicleDto(1L, coordinateDto);

        // WHEN
        mockMvc.perform(post("/vehicle")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(vehicleDto)))
            .andExpect(status().isBadRequest());

        // THEN
        Assertions.assertEquals(0, vehicleData.size());
    }

    @Test
    void createVehicle_missingYCoordinates_dataNotSentToKafka() throws Exception {
        // GIVEN
        CoordinateDto coordinateDto = new CoordinateDto(12.2, null);
        VehicleDto vehicleDto = new VehicleDto(1L, coordinateDto);

        // WHEN
        mockMvc.perform(post("/vehicle")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(vehicleDto)))
            .andExpect(status().isBadRequest());

        // THEN
        Assertions.assertEquals(0, vehicleData.size());
    }

    @Test
    void createVehicle_vehicleIdIsBelowZero_dataNotSentToKafka() throws Exception {
        // GIVEN
        CoordinateDto coordinateDto = new CoordinateDto(12.2, 12.2);
        VehicleDto vehicleDto = new VehicleDto(-1L, coordinateDto);

        // WHEN
        mockMvc.perform(post("/vehicle")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(vehicleDto)))
            .andExpect(status().isBadRequest());

        // THEN
        Assertions.assertEquals(0, vehicleData.size());
    }

    @KafkaListener(topics = "input", groupId = "test-group")
    public void consume(ConsumerRecord<Long, Coordinate> record) {
        Vehicle vehicle = Vehicle.builder()
            .id(record.key())
            .coordinate(record.value())
            .build();

        vehicleData.add(vehicle);
    }
}
