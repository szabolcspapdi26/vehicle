package com.epam.schema;

import java.util.HashMap;
import java.util.Map;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

class CoordinateTest {
  @Test
  void toString_whenToStringCalled_correctObjectStringReturned() {
    // GIVEN
    Coordinate coordinate = new Coordinate();
    coordinate.setX(12.2);
    coordinate.setY(12.2);

    String expected = "com.epam.schema.Coordinate@"
        + Integer.toHexString(System.identityHashCode(coordinate))
        + "[x=12.2,y=12.2,additionalProperties={}]";

    // WHEN
    var actual = coordinate.toString();

    // THEN
    Assertions.assertEquals(expected, actual);
  }

  @Test
  void equalsTest_checkIfTwoCoordinateObjectIsEqual_theyShouldBeEqual() {
    // GIVEN
    Coordinate coordinate1 = new Coordinate();
    coordinate1.setX(12.2);
    coordinate1.setY(12.2);

    Coordinate coordinate2 = new Coordinate();
    coordinate2.setX(12.2);
    coordinate2.setY(12.2);

    boolean expected = true;

    // WHEN
    var actual = coordinate1.equals(coordinate2);

    // THEN
    Assertions.assertEquals(expected, actual);
  }

  @Test
  void equalsTest_checkIfTwoCoordinateObjectIsEqual_theyShouldNotBeEqual() {
    // GIVEN
    Coordinate coordinate1 = new Coordinate();
    coordinate1.setX(12.2);
    coordinate1.setY(12.2);

    Coordinate coordinate2 = new Coordinate();
    coordinate2.setX(2.2);
    coordinate2.setY(12.2);

    boolean expected = false;

    // WHEN
    var actual = coordinate1.equals(coordinate2);

    // THEN
    Assertions.assertEquals(expected, actual);
  }

  @Test
  void setAdditionalProperties_givenCoordinate_emptyMapReturned() {
    // GIVEN
    Coordinate coordinate = new Coordinate();
    coordinate.setX(12.2);
    coordinate.setY(12.2);

    Map<String, Object> expected = new HashMap<>();
    expected.put("a", "a");

    // WHEN
    coordinate.setAdditionalProperty("a", "a");

    // THEN
    Assertions.assertEquals(expected, coordinate.getAdditionalProperties());
  }
}
