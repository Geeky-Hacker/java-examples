package com.geekyhacker.valueclass;

/**
 * Not a candidate: it has a mutable field and a synchronized instance method.
 */
public class MutablePoint {

    private int x;
    private final int y;

    public MutablePoint(int x, int y) {
        this.x = x;
        this.y = y;
    }

    public synchronized void moveTo(int newX) {
        this.x = newX;
    }

    public int x() {
        return x;
    }

    public int y() {
        return y;
    }
}
