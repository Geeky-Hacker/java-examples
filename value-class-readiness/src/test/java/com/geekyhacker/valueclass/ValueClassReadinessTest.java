package com.geekyhacker.valueclass;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotSame;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.List;
import org.junit.jupiter.api.Test;

class ValueClassReadinessTest {

    @Test
    void recordIsReadyAndMutableClassIsNot() {
        assertTrue(ValueClassReadiness.check(Point.class).isEmpty());

        List<ValueClassReadiness.Finding> findings = ValueClassReadiness.check(MutablePoint.class);
        assertEquals(2, findings.size());
        assertTrue(findings.stream().anyMatch(f -> f.member().equals("x")));
        assertTrue(findings.stream().anyMatch(f -> f.member().equals("moveTo")));
    }

    @Test
    void equalsIsTheSafeComparison() {
        Point a = new Point(17, 3);
        Point b = new Point(17, 3);

        assertEquals(a, b);
        // On JDK 21 these are different objects. With a value class, a == b would be true.
        assertNotSame(a, b);
    }
}
