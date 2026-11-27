package com.geekyhacker.aotprofiles;

import java.util.Locale;

/**
 * Usage: Main [rounds] [iterationsPerRound]
 * Prints one "round,millis" line per round.
 */
public final class Main {

    private static final int WORKLOAD_SIZE = 10_000;

    private Main() {
    }

    public static void main(String[] args) {
        int rounds = args.length > 0 ? Integer.parseInt(args[0]) : 20;
        int iterations = args.length > 1 ? Integer.parseInt(args[1]) : 200;

        RoundRunner.Result result = RoundRunner.measure(rounds, iterations, WORKLOAD_SIZE);

        System.out.println("round,millis");
        for (int i = 0; i < result.millis().length; i++) {
            System.out.println(String.format(Locale.ROOT, "%d,%.3f", i + 1, result.millis()[i]));
        }
        System.err.println("checksum=" + result.checksum());
    }
}
