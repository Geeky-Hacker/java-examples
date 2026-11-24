package com.geekyhacker.zgc;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public final class Stats {

    private Stats() {
    }

    public static long median(List<Long> values) {
        if (values.isEmpty()) {
            throw new IllegalArgumentException("No values");
        }
        List<Long> sorted = new ArrayList<>(values);
        Collections.sort(sorted);
        return sorted.get(sorted.size() / 2);
    }

    /**
     * Time spent in rounds slower than the steady state. The steady state is the median of the last quarter
     * of the rounds. Rounds before the first one within the tolerance count as warmup.
     */
    public static long warmupNanos(List<Long> rounds, double tolerance) {
        int tail = Math.max(1, rounds.size() / 4);
        long steady = median(rounds.subList(rounds.size() - tail, rounds.size()));
        long total = 0;
        for (long round : rounds) {
            if (round <= steady * tolerance) {
                break;
            }
            total += round;
        }
        return total;
    }
}
