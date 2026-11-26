package com.geekyhacker.pgvector;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

/**
 * Plain Java store using cosine distance, the same ordering as pgvector's <=> operator.
 */
public class InMemoryDocumentStore implements DocumentStore {

    private record Entry(String content, float[] embedding) {
    }

    private final List<Entry> entries = new ArrayList<>();

    @Override
    public void save(String content, float[] embedding) {
        entries.add(new Entry(content, embedding));
    }

    @Override
    public List<ScoredChunk> nearest(float[] query, int limit) {
        return entries.stream()
                .map(e -> new ScoredChunk(e.content(), cosineDistance(query, e.embedding())))
                .sorted(Comparator.comparingDouble(ScoredChunk::distance))
                .limit(limit)
                .toList();
    }

    static double cosineDistance(float[] a, float[] b) {
        double dot = 0;
        double normA = 0;
        double normB = 0;
        for (int i = 0; i < a.length; i++) {
            dot += a[i] * b[i];
            normA += a[i] * a[i];
            normB += b[i] * b[i];
        }
        if (normA == 0 || normB == 0) {
            return 1.0;
        }
        return 1.0 - dot / (Math.sqrt(normA) * Math.sqrt(normB));
    }
}
