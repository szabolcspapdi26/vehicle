package com.epam.schema;

import java.util.HashMap;
import java.util.Map;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

class VehicleTest {

  @Test
  void toString_whenToStringCalled_correctObjectStringReturned() {
    // GIVEN
    Vehicle vehicle = new Vehicle();
    vehicle.setId(1L);
    vehicle.setX(12.2);
    vehicle.setY(12.2);

    String expected = "com.epam.schema.Vehicle@"
        + Integer.toHexString(System.identityHashCode(vehicle))
        + "[id=1,x=12.2,y=12.2,additionalProperties={}]";

    // WHEN
    var actual = vehicle.toString();

    // THEN
    Assertions.assertEquals(expected, actual);
  }

  @Test
  void equalsTest_checkIfTwoVehicleObjectIsEqual_theyShouldBeEqual() {
    // GIVEN
    Vehicle vehicle1 = new Vehicle();
    vehicle1.setId(1L);
    vehicle1.setX(12.2);
    vehicle1.setY(12.2);

    Vehicle vehicle2 = new Vehicle();
    vehicle2.setId(1L);
    vehicle2.setX(12.2);
    vehicle2.setY(12.2);

    boolean expected = true;

    // WHEN
    var actual = vehicle1.equals(vehicle2);

    // THEN
    Assertions.assertEquals(expected, actual);
  }

  @Test
  void equalsTest_checkIfTwoVehicleObjectIsEqual_theyShouldNotBeEqual() {
    // GIVEN
    Vehicle vehicle1 = new Vehicle();
    vehicle1.setId(1L);
    vehicle1.setX(12.2);
    vehicle1.setY(12.2);

    Vehicle vehicle2 = new Vehicle();
    vehicle2.setId(1L);
    vehicle2.setX(2.2);
    vehicle2.setY(12.2);

    boolean expected = false;

    // WHEN
    var actual = vehicle1.equals(vehicle2);

    // THEN
    Assertions.assertEquals(expected, actual);
  }

  @Test
  void setAdditionalProperties_givenCoordinate_emptyMapReturned() {
    // GIVEN
    Vehicle vehicle = new Vehicle();
    vehicle.setId(1L);
    vehicle.setX(2.2);
    vehicle.setY(12.2);

    Map<String, Object> expected = new HashMap<>();
    expected.put("a", "a");

    // WHEN
    vehicle.setAdditionalProperty("a", "a");

    // THEN
    Assertions.assertEquals(expected, vehicle.getAdditionalProperties());
  }
}
