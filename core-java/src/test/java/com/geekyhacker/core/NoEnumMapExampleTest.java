package com.geekyhacker.core;

import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

import java.time.DayOfWeek;

import static org.junit.jupiter.api.Assertions.assertEquals;

class NoEnumMapExampleTest {

  @ParameterizedTest
  @CsvSource(value = {"MONDAY,Montag", "TUESDAY,Dienstag", "WEDNESDAY,Mittwoch",
      "THURSDAY,Donnerstag", "FRIDAY,Freitag", "SATURDAY,Samstag", "SUNDAY,Sonntag"})
  void shouldRetrieveCorrespondingDayOfWeekInGerman(DayOfWeek dayOfWeek, String expectedGermanDayOfWeek) {
    var germanDayOfWeek = EnumMapExample.getCorrespondingGermanDayOfWeek(dayOfWeek);
    assertEquals(expectedGermanDayOfWeek, germanDayOfWeek);
  }

}