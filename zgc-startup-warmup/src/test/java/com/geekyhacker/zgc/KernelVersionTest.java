package com.geekyhacker.zgc;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Test;

class KernelVersionTest {

    @Test
    void kernel61AndNewerSupportsMadvCollapse() {
        assertTrue(KernelVersion.parse("6.1.0-13-amd64").supportsMadvCollapse());
        assertTrue(KernelVersion.parse("6.18.60-1-lts").supportsMadvCollapse());
        assertTrue(KernelVersion.parse("7.0.1").supportsMadvCollapse());
    }

    @Test
    void olderKernelDoesNot() {
        assertFalse(KernelVersion.parse("5.15.0-91-generic").supportsMadvCollapse());
        assertFalse(KernelVersion.parse("6.0.19").supportsMadvCollapse());
    }
}
