package com.epam.schema;

import java.util.HashMap;
import java.util.Map;
import org.checkerframework.checker.units.qual.C;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

class CoordinateTest {
  @Test
  void getX_givenCoordinate_xCorrectlyReturned() {
    // GIVEN
    Coordinate coordinate = new Coordinate();
    coordinate.setX(12.2);

    Double expected = 12.2;

    // WHEN
    var actual = coordinate.getX();

    // THEN
    Assertions.assertEquals(expected, actual);
  }

  @Test
  void setX_givenCoordinate_xCorrectlyReturned() {
    // GIVEN
    Coordinate coordinate = new Coordinate();
    Double expected = 12.2;

    // WHEN
    coordinate.setX(12.2);

    // THEN
    Assertions.assertEquals(expected, coordinate.getX());
  }

  @Test
  void getY_givenCoordinate_yCorrectlyReturned() {
    // GIVEN
    Coordinate coordinate = new Coordinate();
    coordinate.setY(12.2);

    Double expected = 12.2;

    // WHEN
    var actual = coordinate.getY();

    // THEN
    Assertions.assertEquals(expected, actual);
  }

  @Test
  void setY_givenCoordinate_yCorrectlyReturned() {
    // GIVEN
    Coordinate coordinate = new Coordinate();
    Double expected = 12.2;

    // WHEN
    coordinate.setY(12.2);

    // THEN
    Assertions.assertEquals(expected, coordinate.getY());
  }

  @Test
  void getAdditionalProperties_givenCoordinate_emptyMapReturned() {
    // GIVEN
    Coordinate coordinate = new Coordinate();

    Map<String, Object> expected = new HashMap<>();

    // WHEN
    var actual = coordinate.getAdditionalProperties();

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
}
