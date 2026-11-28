package com.geekyhacker.agentevals;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

/** A minimal Java version of a tool correctness metric: overlap of expected and actual tools. */
public final class EvalRunner {

    public record EvalResult(String goldenId, double toolScore) {
    }

    private EvalRunner() {
    }

    public static List<EvalResult> run(SupportAgent agent, List<Golden> goldens) {
        return goldens.stream()
                .map(g -> new EvalResult(g.id(),
                        toolScore(g.expected_tools(), agent.run(g.input()).toolCalls())))
                .toList();
    }

    public static double toolScore(List<String> expected, List<String> actual) {
        Set<String> union = new HashSet<>(expected);
        union.addAll(actual);
        if (union.isEmpty()) {
            return 1.0;
        }
        Set<String> intersection = new HashSet<>(expected);
        intersection.retainAll(actual);
        return (double) intersection.size() / union.size();
    }
}
