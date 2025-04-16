package com.geekyhacker.core;

import java.time.DayOfWeek;
import java.util.HashMap;
import java.util.Map;

public class NoEnumMapExample {
  private static final Map<String, String> germanDayOfWeeks = new HashMap<>();

  static {
    germanDayOfWeeks.put(DayOfWeek.MONDAY.name(), "Montag");
    germanDayOfWeeks.put(DayOfWeek.TUESDAY.name(), "Dienstag");
    germanDayOfWeeks.put(DayOfWeek.WEDNESDAY.name(), "Mittwoch");
    germanDayOfWeeks.put(DayOfWeek.THURSDAY.name(), "Donnerstag");
    germanDayOfWeeks.put(DayOfWeek.FRIDAY.name(), "Freitag");
    germanDayOfWeeks.put(DayOfWeek.SATURDAY.name(), "Samstag");
    germanDayOfWeeks.put(DayOfWeek.SUNDAY.name(), "Sonntag");
  }

  private NoEnumMapExample() {

  }

  public static String getCorrespondingGermanDayOfWeek(DayOfWeek englishDayOfWeek) {
    return germanDayOfWeeks.get(englishDayOfWeek.name());
  }
}
