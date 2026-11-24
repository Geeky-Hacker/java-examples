package com.geekyhacker.zgc;

import java.util.ArrayList;
import java.util.List;

/**
 * Allocation heavy workload run in a child JVM. Prints READY at the start of main and one line per round.
 */
public final class Workload {

    private Workload() {
    }

    public static void main(String[] args) {
        int rounds = Integer.parseInt(args[0]);
        System.out.println("READY");
        List<byte[]> retained = new ArrayList<>();
        for (int round = 0; round < rounds; round++) {
            long start = System.nanoTime();
            for (int i = 0; i < 200_000; i++) {
                byte[] chunk = new byte[256];
                chunk[i % chunk.length] = (byte) i;
                if (i % 50 == 0) {
                    retained.add(chunk);
                }
            }
            System.out.println("ROUND " + round + " " + (System.nanoTime() - start));
        }
        if (retained.isEmpty()) {
            System.out.println("EMPTY");
        }
    }
}
