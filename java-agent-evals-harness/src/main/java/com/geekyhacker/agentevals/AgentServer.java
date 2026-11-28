package com.geekyhacker.agentevals;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.sun.net.httpserver.HttpServer;

import java.io.IOException;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.util.Map;

/** Exposes the agent on POST /run using the request/response shape HttpTarget expects. */
public class AgentServer {

    private static final ObjectMapper MAPPER = new ObjectMapper();

    private final HttpServer server;

    public AgentServer(SupportAgent agent, int port) throws IOException {
        server = HttpServer.create(new InetSocketAddress(InetAddress.getLoopbackAddress(), port), 0);
        server.createContext("/run", exchange -> {
            if (!"POST".equals(exchange.getRequestMethod())) {
                exchange.sendResponseHeaders(405, -1);
                exchange.close();
                return;
            }
            JsonNode request = MAPPER.readTree(exchange.getRequestBody());
            AgentResult result = agent.run(request.get("input").asText());

            byte[] body = MAPPER.writeValueAsBytes(Map.of(
                    "output", result.output(),
                    "tool_calls", result.toolCalls()));
            exchange.getResponseHeaders().add("Content-Type", "application/json");
            exchange.sendResponseHeaders(200, body.length);
            exchange.getResponseBody().write(body);
            exchange.close();
        });
    }

    public void start() {
        server.start();
    }

    public void stop() {
        server.stop(0);
    }

    public int port() {
        return server.getAddress().getPort();
    }

    public static void main(String[] args) throws IOException {
        AgentServer agentServer = new AgentServer(new SupportAgent(), 8080);
        agentServer.start();
        System.out.println("Agent listening on http://localhost:" + agentServer.port() + "/run");
    }
}
