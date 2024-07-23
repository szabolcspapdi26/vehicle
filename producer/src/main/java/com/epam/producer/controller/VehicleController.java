package com.epam.producer.controller;

import com.epam.producer.controller.validator.VehicleValidator;
import com.epam.producer.model.Vehicle;
import com.epam.producer.service.VehicleService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class VehicleController {
    private final VehicleService vehicleService;
    private final VehicleValidator vehicleValidator;

    @PostMapping(path = "/vehicle")
    public ResponseEntity<Void> createVehicleData(@RequestBody Vehicle vehicle) {
        vehicleValidator.validateVehicle(vehicle);
        vehicleService.processVehicle(vehicle);

        return ResponseEntity.status(HttpStatus.CREATED).build();
    }
}
