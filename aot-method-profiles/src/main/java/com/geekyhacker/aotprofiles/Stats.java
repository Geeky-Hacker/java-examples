package com.geekyhacker.aotprofiles;

import java.util.ArrayList;
import java.util.List;

/** Helpers to read round timings and summarise them. */
public final class Stats {

    private Stats() {
    }

    /** Parses "round,millis" lines; the header and blank lines are skipped. */
    public static double[] parseCsv(List<String> lines) {
        List<Double> values = new ArrayList<>();
        for (String line : lines) {
            String trimmed = line.trim();
            if (trimmed.isEmpty() || trimmed.startsWith("round")) {
                continue;
            }
            String[] parts = trimmed.split(",");
            values.add(Double.parseDouble(parts[1]));
        }
        return values.stream().mapToDouble(Double::doubleValue).toArray();
    }

    /** Average of rounds fromRound..toRound, both 1-based and inclusive. */
    public static double average(double[] millis, int fromRound, int toRound) {
        if (fromRound < 1 || toRound > millis.length || fromRound > toRound) {
            throw new IllegalArgumentException(
                    "Invalid range " + fromRound + "-" + toRound + " for " + millis.length + " rounds");
        }
        double sum = 0;
        for (int i = fromRound - 1; i < toRound; i++) {
            sum += millis[i];
        }
        return sum / (toRound - fromRound + 1);
    }

    /** Average of the last 20 percent of rounds (at least one round). */
    public static double steadyState(double[] millis) {
        int tail = Math.max(1, millis.length / 5);
        return average(millis, millis.length - tail + 1, millis.length);
    }
}
