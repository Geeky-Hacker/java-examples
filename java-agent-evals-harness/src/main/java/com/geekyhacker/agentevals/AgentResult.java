package com.geekyhacker.agentevals;

import java.util.List;

public record AgentResult(String output, List<String> toolCalls) {
}
