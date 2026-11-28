package com.geekyhacker.aotprofiles;

import java.util.stream.IntStream;

/**
 * A small stream pipeline with several lambdas. Lambda call sites are where
 * the AOT method profiles are reported to behave differently.
 */
public final class LambdaWorkload {

    private LambdaWorkload() {
    }

    public static long run(int size) {
        return IntStream.range(0, size)
                .boxed()
                .filter(i -> i % 3 != 0)
                .map(i -> i * 31 + 7)
                .mapToLong(i -> (long) i * i % 1_000_003)
                .sum();
    }
}
