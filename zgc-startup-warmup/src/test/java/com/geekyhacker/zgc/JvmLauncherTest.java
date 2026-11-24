package com.geekyhacker.zgc;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.List;
import org.junit.jupiter.api.Test;

class JvmLauncherTest {

    @Test
    void childJvmWithZgcReportsStartupAndRounds() throws Exception {
        RunResult result = JvmLauncher.run(List.of("-XX:+UseZGC"), 4);

        assertTrue(result.startupNanos() > 0);
        assertEquals(4, result.roundNanos().size());
        assertTrue(result.roundNanos().stream().allMatch(n -> n > 0));
        assertTrue(result.gcInitLines().stream().anyMatch(l -> l.contains("Z")));
    }

    @Test
    void flagProbeDoesNotMatchUnknownFlag() throws Exception {
        assertTrue(JvmLauncher.supports("UseZGC"));
        assertEquals(false, JvmLauncher.supports("ThisFlagDoesNotExist"));
    }
}
