package com.geekyhacker.zgc;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public final class Benchmark {

    private static final int RUNS = 5;
    private static final int ROUNDS = 40;

    private Benchmark() {
    }

    public static void main(String[] args) throws Exception {
        KernelVersion kernel = KernelVersion.parse(System.getProperty("os.version"));
        System.out.println("JVM: " + System.getProperty("java.vm.version"));
        System.out.println("Kernel supports MADV_COLLAPSE: " + kernel.supportsMadvCollapse());

        Map<String, List<String>> scenarios = new LinkedHashMap<>();
        scenarios.put("zgc-default", List.of("-XX:+UseZGC"));
        if (JvmLauncher.supports("ZAdaptiveHeapSizing")) {
            scenarios.put("zgc-adaptive", List.of("-XX:+UseZGC", "-XX:+ZAdaptiveHeapSizing"));
        } else {
            System.out.println("ZAdaptiveHeapSizing is not available on this JVM, skipping that scenario");
        }

        for (Map.Entry<String, List<String>> scenario : scenarios.entrySet()) {
            List<Long> startups = new ArrayList<>();
            List<Long> warmups = new ArrayList<>();
            List<String> gcInit = List.of();
            for (int i = 0; i < RUNS; i++) {
                RunResult result = JvmLauncher.run(scenario.getValue(), ROUNDS);
                startups.add(result.startupNanos());
                warmups.add(Stats.warmupNanos(result.roundNanos(), 1.2));
                gcInit = result.gcInitLines();
            }
            System.out.printf("%s: startup %d ms, warmup %d ms (median of %d runs)%n", scenario.getKey(),
                    Stats.median(startups) / 1_000_000, Stats.median(warmups) / 1_000_000, RUNS);
            gcInit.stream()
                    .filter(l -> l.toLowerCase().contains("page") || l.toLowerCase().contains("heat"))
                    .forEach(l -> System.out.println("    " + l));
        }
    }
}
