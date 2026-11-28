package com.geekyhacker.aotprofiles;

/**
 * Runs the workload in rounds and records the duration of every round,
 * which gives a warmup curve instead of a single number.
 */
public final class RoundRunner {

    public record Result(double[] millis, long checksum) {
    }

    private RoundRunner() {
    }

    public static Result measure(int rounds, int iterationsPerRound, int size) {
        double[] millis = new double[rounds];
        long checksum = 0;
        for (int round = 0; round < rounds; round++) {
            long start = System.nanoTime();
            for (int i = 0; i < iterationsPerRound; i++) {
                checksum += LambdaWorkload.run(size);
            }
            millis[round] = (System.nanoTime() - start) / 1_000_000.0;
        }
        return new Result(millis, checksum);
    }
}
