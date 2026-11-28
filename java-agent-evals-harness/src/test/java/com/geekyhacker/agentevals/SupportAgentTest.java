package com.geekyhacker.agentevals;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.geekyhacker.agentevals.EvalRunner.EvalResult;
import org.junit.jupiter.api.Test;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

class SupportAgentTest {

    private final SupportAgent agent = new SupportAgent();

    @Test
    void unitTestsPassForSingleIntents() {
        assertEquals(List.of("lookup_order"), agent.run("Where is order 1234?").toolCalls());
        assertEquals(List.of("create_refund_ticket"), agent.run("I want a refund").toolCalls());
        assertEquals(List.of("send_reset_link"), agent.run("I forgot my password").toolCalls());
        assertEquals(List.of(), agent.run("Tell me a joke").toolCalls());
    }

    @Test
    void evalCatchesWhatUnitTestsMiss() throws Exception {
        List<EvalResult> results = EvalRunner.run(agent, Golden.load());

        assertEquals(1.0, score(results, "order-status"));
        assertEquals(1.0, score(results, "refund"));
        assertEquals(1.0, score(results, "password"));
        assertEquals(0.5, score(results, "order-and-refund"));
    }

    @Test
    void httpEndpointMatchesHttpTargetContract() throws Exception {
        AgentServer server = new AgentServer(agent, 0);
        server.start();
        try {
            HttpRequest request = HttpRequest.newBuilder(
                            URI.create("http://127.0.0.1:" + server.port() + "/run"))
                    .header("Content-Type", "application/json")
                    .POST(HttpRequest.BodyPublishers.ofString("{\"input\":\"Where is order 42?\"}"))
                    .build();

            HttpResponse<String> response = HttpClient.newHttpClient()
                    .send(request, HttpResponse.BodyHandlers.ofString());
            JsonNode json = new ObjectMapper().readTree(response.body());

            assertEquals(200, response.statusCode());
            assertEquals("Order 42 is shipped.", json.get("output").asText());
            assertEquals("lookup_order", json.get("tool_calls").get(0).asText());
        } finally {
            server.stop();
        }
    }

    private static double score(List<EvalResult> results, String id) {
        return results.stream().filter(r -> r.goldenId().equals(id)).findFirst().orElseThrow().toolScore();
    }
}
