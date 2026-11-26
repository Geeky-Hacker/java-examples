package com.geekyhacker.pgvector;

import org.springframework.ai.chat.model.ChatModel;
import org.springframework.ai.embedding.EmbeddingModel;

/**
 * Adapts Spring AI models (for example the Ollama ones) to the small interfaces used by {@link RagService}.
 */
public final class SpringAiAdapters {

    private SpringAiAdapters() {
    }

    public static Embedder embedder(EmbeddingModel model) {
        return text -> model.embed(text);
    }

    public static Answerer answerer(ChatModel model) {
        return prompt -> model.call(prompt);
    }
}
