package com.geekyhacker.oom;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import com.geekyhacker.oom.ScenarioRunner.Result;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

class ScenariosTest {

    @ParameterizedTest
    @ValueSource(strings = {"cache", "batch", "report", "threadlocal", "connections"})
    void brokenScenarioRunsOutOfMemory(String scenario) throws Exception {
        Result result = ScenarioRunner.run(scenario, "broken");

        assertNotEquals(0, result.exitCode(), result.output());
        assertTrue(result.output().contains("OutOfMemoryError"), result.output());
    }

    @ParameterizedTest
    @ValueSource(strings = {"cache", "batch", "report", "threadlocal", "connections"})
    void fixedScenarioCompletes(String scenario) throws Exception {
        Result result = ScenarioRunner.run(scenario, "fixed");

        assertEquals(0, result.exitCode(), result.output());
        assertTrue(result.output().contains("DONE"), result.output());
    }
}
