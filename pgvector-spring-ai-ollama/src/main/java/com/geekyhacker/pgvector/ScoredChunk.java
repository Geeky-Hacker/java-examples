package com.geekyhacker.pgvector;

/**
 * A stored chunk with its cosine distance to the query. Smaller means closer.
 */
public record ScoredChunk(String content, double distance) {
}
