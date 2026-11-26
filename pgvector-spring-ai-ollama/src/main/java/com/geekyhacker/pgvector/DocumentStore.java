package com.geekyhacker.pgvector;

import java.util.List;

public interface DocumentStore {

    void save(String content, float[] embedding);

    List<ScoredChunk> nearest(float[] query, int limit);
}
