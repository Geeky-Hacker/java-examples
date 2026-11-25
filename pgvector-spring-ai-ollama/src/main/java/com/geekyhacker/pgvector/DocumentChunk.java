package com.geekyhacker.pgvector;

import io.hypersistence.utils.hibernate.type.basic.PostgreSQLVectorType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import org.hibernate.annotations.Type;

@Entity
@Table(name = "document_chunks")
public class DocumentChunk {

    public static final int DIMENSIONS = 1024;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(columnDefinition = "text", nullable = false)
    private String content;

    @Type(PostgreSQLVectorType.class)
    @Column(columnDefinition = "vector(1024)", nullable = false)
    private float[] embedding;

    protected DocumentChunk() {
    }

    public DocumentChunk(String content, float[] embedding) {
        this.content = content;
        this.embedding = embedding;
    }

    public Long getId() {
        return id;
    }

    public String getContent() {
        return content;
    }

    public float[] getEmbedding() {
        return embedding;
    }
}
