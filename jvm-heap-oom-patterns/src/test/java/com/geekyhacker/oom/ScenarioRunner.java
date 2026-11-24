package com.geekyhacker.oom;

import java.io.IOException;
import java.net.URISyntaxException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Path;
import java.util.List;
import java.util.concurrent.TimeUnit;

/** Runs a scenario in a child JVM with a small heap. */
final class ScenarioRunner {

    record Result(int exitCode, String output) {
    }

    private ScenarioRunner() {
    }

    static Result run(String scenario, String mode) throws IOException, InterruptedException, URISyntaxException {
        String java = Path.of(System.getProperty("java.home"), "bin", "java").toString();
        String classes = Path.of(Scenarios.class.getProtectionDomain().getCodeSource().getLocation().toURI())
                .toString();

        Process process = new ProcessBuilder(List.of(
                java,
                "-Xmx64m",
                "-XX:+UseG1GC",
                "-XX:+ExitOnOutOfMemoryError",
                "-cp", classes,
                Scenarios.class.getName(),
                scenario, mode))
                .redirectErrorStream(true)
                .start();

        String output = new String(process.getInputStream().readAllBytes(), StandardCharsets.UTF_8);
        if (!process.waitFor(2, TimeUnit.MINUTES)) {
            process.destroyForcibly();
            throw new IllegalStateException("Scenario timed out: " + scenario);
        }
        return new Result(process.exitValue(), output);
    }
}
