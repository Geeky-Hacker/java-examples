package com.geekyhacker.pgvector;

import java.util.List;
import java.util.stream.Collectors;

public class RagService {

    private final Embedder embedder;
    private final Answerer answerer;
    private final DocumentStore store;
    private final int topK;

    public RagService(Embedder embedder, Answerer answerer, DocumentStore store, int topK) {
        this.embedder = embedder;
        this.answerer = answerer;
        this.store = store;
        this.topK = topK;
    }

    public void index(String text) {
        store.save(text, embedder.embed(text));
    }

    public List<ScoredChunk> search(String query) {
        return store.nearest(embedder.embed(query), topK);
    }

    public Answer ask(String question) {
        List<ScoredChunk> sources = search(question);
        String context = sources.stream()
                .map(ScoredChunk::content)
                .collect(Collectors.joining("\n---\n"));

        String prompt = """
                Answer the question using only the context below.
                If the context does not contain the answer, say you don't know.

                Context:
                %s

                Question: %s
                """.formatted(context, question);

        return new Answer(answerer.answer(prompt), sources);
    }
}
