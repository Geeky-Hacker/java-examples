package com.geekyhacker.shirudo;

import static org.junit.jupiter.api.Assertions.assertArrayEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.nio.charset.StandardCharsets;
import org.junit.jupiter.api.Test;

class Base64GuardTest {

    @Test
    void decodesValidInput() {
        Base64Guard guard = new Base64Guard(8);

        assertArrayEquals("abc".getBytes(StandardCharsets.UTF_8), guard.decode("YWJj"));
    }

    @Test
    void rejectsOversizedInput() {
        Base64Guard guard = new Base64Guard(8);

        assertThrows(IllegalArgumentException.class, () -> guard.decode("QUJDREVGR0hJSktM"));
    }

    @Test
    void rejectsNull() {
        Base64Guard guard = new Base64Guard(8);

        assertThrows(IllegalArgumentException.class, () -> guard.decode(null));
    }
}
