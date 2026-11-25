# pgvector-spring-ai-ollama

Shows how to map a pgvector column with Hypersistence Utils `PostgreSQLVectorType`, query it with the cosine operator, and run a small RAG flow with Spring AI and Ollama.
Run `mvn verify`: tests use an in-memory store and fake models, so no database, Docker or Ollama is needed.

# Related articles

- [pgvector with JPA, Hibernate and Spring AI on Ollama](https://www.geekyhacker.com/pgvector-jpa-hibernate-spring-ai-ollama/)
