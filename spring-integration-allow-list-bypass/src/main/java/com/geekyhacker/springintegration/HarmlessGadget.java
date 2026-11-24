package com.geekyhacker.springintegration;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.Serial;
import java.io.Serializable;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * Stand-in for a real gadget. It does nothing harmful, it only records that it was deserialized.
 */
public class HarmlessGadget implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    public static final AtomicInteger READ_COUNT = new AtomicInteger();

    @Serial
    private void readObject(ObjectInputStream in) throws IOException, ClassNotFoundException {
        in.defaultReadObject();
        READ_COUNT.incrementAndGet();
    }
}
