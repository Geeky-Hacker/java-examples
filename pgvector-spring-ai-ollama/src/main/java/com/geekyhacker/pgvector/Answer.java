package com.geekyhacker.pgvector;

import java.util.List;

public record Answer(String text, List<ScoredChunk> sources) {
}
