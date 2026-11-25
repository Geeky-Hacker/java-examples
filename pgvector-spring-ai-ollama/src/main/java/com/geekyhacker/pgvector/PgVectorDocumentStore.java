package com.geekyhacker.pgvector;

import jakarta.persistence.EntityManager;

import java.util.Arrays;
import java.util.List;

/**
 * JPA implementation backed by PostgreSQL with the pgvector extension.
 * The caller is responsible for the transaction.
 */
public class PgVectorDocumentStore implements DocumentStore {

    private final EntityManager entityManager;

    public PgVectorDocumentStore(EntityManager entityManager) {
        this.entityManager = entityManager;
    }

    @Override
    public void save(String content, float[] embedding) {
        entityManager.persist(new DocumentChunk(content, embedding));
    }

    @Override
    public List<ScoredChunk> nearest(float[] query, int limit) {
        List<?> rows = entityManager.createNativeQuery("""
                        SELECT content, embedding <=> CAST(:embedding AS vector) AS distance
                        FROM document_chunks
                        ORDER BY distance
                        LIMIT :limit
                        """)
                .setParameter("embedding", Arrays.toString(query).replace(" ", ""))
                .setParameter("limit", limit)
                .getResultList();

        return rows.stream()
                .map(row -> (Object[]) row)
                .map(row -> new ScoredChunk((String) row[0], ((Number) row[1]).doubleValue()))
                .toList();
    }
}
