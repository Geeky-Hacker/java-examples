package com.geekyhacker.zgc;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.util.List;
import org.junit.jupiter.api.Test;

class StatsTest {

    @Test
    void warmupIsTimeSpentBeforeSteadyState() {
        List<Long> rounds = List.of(100L, 50L, 20L, 10L, 10L, 10L, 10L, 10L);

        assertEquals(170L, Stats.warmupNanos(rounds, 1.2));
    }

    @Test
    void noWarmupWhenAlreadySteady() {
        assertEquals(0L, Stats.warmupNanos(List.of(10L, 10L, 10L, 10L), 1.2));
    }

    @Test
    void medianPicksMiddleValue() {
        assertEquals(3L, Stats.median(List.of(5L, 1L, 3L)));
        assertThrows(IllegalArgumentException.class, () -> Stats.median(List.of()));
    }
}
