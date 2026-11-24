package com.geekyhacker.valueclass;

/**
 * Immutable and identity-free in spirit: with a JEP 401 preview JDK this can become
 * {@code value record Point(int x, int y) {}}.
 */
public record Point(int x, int y) {
}
