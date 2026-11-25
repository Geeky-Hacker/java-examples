package com.geekyhacker.pgvector;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

class RagServiceTest {

    private final List<String> prompts = new ArrayList<>();
    private RagService service;

    /** Toy embedding: counts of the words java, python and pasta. */
    private static float[] embed(String text) {
        String lower = text.toLowerCase();
        return new float[]{count(lower, "java"), count(lower, "python"), count(lower, "pasta")};
    }

    private static float count(String text, String word) {
        return text.split(word, -1).length - 1;
    }

    @BeforeEach
    void setUp() {
        service = new RagService(RagServiceTest::embed, prompt -> {
            prompts.add(prompt);
            return "fake answer";
        }, new InMemoryDocumentStore(), 1);

        service.index("Java records are immutable data carriers.");
        service.index("Python uses indentation for blocks.");
        service.index("Pasta should be cooked al dente.");
    }

    @Test
    void searchReturnsTheClosestChunkFirst() {
        List<ScoredChunk> result = service.search("Tell me about java");

        assertEquals(1, result.size());
        assertEquals("Java records are immutable data carriers.", result.get(0).content());
        assertEquals(0.0, result.get(0).distance(), 1e-9);
    }

    @Test
    void askPutsRetrievedContextInThePrompt() {
        Answer answer = service.ask("How do I cook pasta?");

        assertEquals("fake answer", answer.text());
        assertEquals(1, prompts.size());
        assertTrue(prompts.get(0).contains("Pasta should be cooked al dente."));
        assertTrue(prompts.get(0).contains("Question: How do I cook pasta?"));
        assertTrue(!prompts.get(0).contains("Python"));
    }

    @Test
    void entityUsesPgVectorMapping() throws Exception {
        Field field = DocumentChunk.class.getDeclaredField("embedding");

        org.hibernate.annotations.Type type = field.getAnnotation(org.hibernate.annotations.Type.class);
        jakarta.persistence.Column column = field.getAnnotation(jakarta.persistence.Column.class);

        assertEquals(io.hypersistence.utils.hibernate.type.basic.PostgreSQLVectorType.class, type.value());
        assertEquals("vector(" + DocumentChunk.DIMENSIONS + ")", column.columnDefinition());
    }
}
