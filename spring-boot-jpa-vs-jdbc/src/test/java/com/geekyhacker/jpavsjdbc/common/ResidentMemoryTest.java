package com.geekyhacker.jpavsjdbc.common;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;

class ResidentMemoryTest {

    @Test
    void convertsKibToMib() {
        assertThat(ResidentMemory.parseKib("  2048\n").getAsDouble()).isEqualTo(2.0);
    }

    @Test
    void returnsEmptyForGarbage() {
        assertThat(ResidentMemory.parseKib("not a number")).isEmpty();
    }
}
