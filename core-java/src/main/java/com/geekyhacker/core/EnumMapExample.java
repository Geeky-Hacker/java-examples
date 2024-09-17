package com.geekyhacker.core;

import java.time.DayOfWeek;
import java.util.EnumMap;

public class EnumMapExample {
  private static final EnumMap<DayOfWeek, String> germanDayOfWeeks = new EnumMap<>(DayOfWeek.class);

  static {
    germanDayOfWeeks.put(DayOfWeek.MONDAY, "Montag");
    germanDayOfWeeks.put(DayOfWeek.TUESDAY, "Dienstag");
    germanDayOfWeeks.put(DayOfWeek.WEDNESDAY, "Mittwoch");
    germanDayOfWeeks.put(DayOfWeek.THURSDAY, "Donnerstag");
    germanDayOfWeeks.put(DayOfWeek.FRIDAY, "Freitag");
    germanDayOfWeeks.put(DayOfWeek.SATURDAY, "Samstag");
    germanDayOfWeeks.put(DayOfWeek.SUNDAY, "Sonntag");
  }

  private EnumMapExample() {

  }

  public static String getCorrespondingGermanDayOfWeek(DayOfWeek englishDayOfWeek) {
    return germanDayOfWeeks.get(englishDayOfWeek);
  }
}
