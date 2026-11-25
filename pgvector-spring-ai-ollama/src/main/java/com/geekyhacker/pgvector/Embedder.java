package com.geekyhacker.pgvector;

@FunctionalInterface
public interface Embedder {

    float[] embed(String text);
}
