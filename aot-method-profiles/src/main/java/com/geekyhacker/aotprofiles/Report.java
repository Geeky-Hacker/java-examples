package com.geekyhacker.aotprofiles;

import java.util.Locale;
import java.util.Map;

/** Formats a comparison table: first round, rounds 2-5 and steady state. */
public final class Report {

    private Report() {
    }

    public static String format(Map<String, double[]> variants) {
        StringBuilder sb = new StringBuilder();
        sb.append(String.format(Locale.ROOT, "%-18s %12s %12s %14s%n",
                "variant", "round 1 ms", "rounds 2-5", "steady state"));
        variants.forEach((name, millis) -> {
            int warmEnd = Math.min(5, millis.length);
            int warmStart = Math.min(2, warmEnd);
            sb.append(String.format(Locale.ROOT, "%-18s %12.1f %12.1f %14.1f%n",
                    name,
                    millis[0],
                    Stats.average(millis, warmStart, warmEnd),
                    Stats.steadyState(millis)));
        });
        return sb.toString();
    }
}
