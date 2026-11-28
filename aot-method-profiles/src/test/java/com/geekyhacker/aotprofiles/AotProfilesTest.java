package com.geekyhacker.aotprofiles;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import org.junit.jupiter.api.Test;

class AotProfilesTest {

    @Test
    void workloadMatchesPlainLoop() {
        long expected = 0;
        for (int i = 0; i < 1_000; i++) {
            if (i % 3 != 0) {
                int mapped = i * 31 + 7;
                expected += (long) mapped * mapped % 1_000_003;
            }
        }

        assertEquals(expected, LambdaWorkload.run(1_000));
    }

    @Test
    void runnerRecordsOneTimingPerRound() {
        RoundRunner.Result result = RoundRunner.measure(4, 3, 100);

        assertEquals(4, result.millis().length);
        assertEquals(3 * LambdaWorkload.run(100), result.checksum() / 4);
        for (double ms : result.millis()) {
            assertTrue(ms >= 0);
        }
    }

    @Test
    void parsesCsvAndSkipsHeader() {
        double[] values = Stats.parseCsv(List.of("round,millis", "1,10.5", "2,8.0", ""));

        assertEquals(2, values.length);
        assertEquals(10.5, values[0], 0.0001);
        assertEquals(8.0, values[1], 0.0001);
    }

    @Test
    void averagesRoundRangeAndSteadyState() {
        double[] millis = {100, 50, 30, 20, 10, 10, 10, 10, 10, 10};

        // rounds 2..5 are 50, 30, 20, 10 -> 110 / 4
        assertEquals(27.5, Stats.average(millis, 2, 5), 0.0001);
        assertEquals(10.0, Stats.steadyState(millis), 0.0001);
        assertThrows(IllegalArgumentException.class, () -> Stats.average(millis, 0, 3));
    }

    @Test
    void reportContainsAllVariants() {
        Map<String, double[]> variants = new LinkedHashMap<>();
        variants.put("no cache", new double[] {100, 50, 30, 20, 10, 10, 10, 10, 10, 10});
        variants.put("cache + profiles", new double[] {60, 30, 20, 12, 11, 11, 11, 11, 11, 11});

        String report = Report.format(variants);

        assertTrue(report.contains("no cache"));
        assertTrue(report.contains("cache + profiles"));
        assertTrue(report.contains("steady state"));
    }
}
