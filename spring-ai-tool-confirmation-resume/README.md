# Spring AI tool call confirmation that survives a restart

Shows Spring AI 2.1 `requiresConfirmation` tools: a payment tool is paused, the tool calls are parked in a file-based H2 database, and a new set of objects (a simulated restart) resumes the turn after approval. No model or API key is needed, the model reply is scripted.

Run it with `mvn verify` (JDK 21 or newer).

# Related articles

- [Spring AI tool call confirmation that survives a restart](https://www.geekyhacker.com/spring-ai-tool-call-confirmation-restart/)
