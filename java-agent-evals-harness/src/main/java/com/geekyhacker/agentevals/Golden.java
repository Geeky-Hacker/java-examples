package com.geekyhacker.agentevals;

import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.List;

public record Golden(String id, String input, String expected, List<String> expected_tools) {

    private static final ObjectMapper MAPPER = new ObjectMapper();

    public static List<Golden> load() throws IOException {
        try (var in = Golden.class.getResourceAsStream("/goldens.jsonl");
             var reader = new BufferedReader(new InputStreamReader(in, StandardCharsets.UTF_8))) {
            return reader.lines()
                    .filter(line -> !line.isBlank())
                    .map(line -> {
                        try {
                            return MAPPER.readValue(line, Golden.class);
                        } catch (IOException e) {
                            throw new IllegalStateException(e);
                        }
                    })
                    .toList();
        }
    }
}
