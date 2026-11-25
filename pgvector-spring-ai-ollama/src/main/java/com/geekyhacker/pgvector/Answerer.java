package com.geekyhacker.pgvector;

@FunctionalInterface
public interface Answerer {

    String answer(String prompt);
}
