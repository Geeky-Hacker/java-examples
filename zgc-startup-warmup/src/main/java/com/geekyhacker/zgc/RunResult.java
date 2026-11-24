package com.geekyhacker.zgc;

import java.util.List;

public record RunResult(long startupNanos, List<Long> roundNanos, List<String> gcInitLines) {
}
