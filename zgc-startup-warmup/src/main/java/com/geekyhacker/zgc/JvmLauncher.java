package com.geekyhacker.zgc;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URISyntaxException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;

public final class JvmLauncher {

    private JvmLauncher() {
    }

    public static String javaBinary() {
        return Path.of(System.getProperty("java.home"), "bin", "java").toString();
    }

    /** Asks the current JVM if it knows the given -XX flag. */
    public static boolean supports(String flag) throws IOException, InterruptedException {
        Process process = new ProcessBuilder(javaBinary(), "-XX:+UseZGC", "-XX:+PrintFlagsFinal", "-version")
                .redirectErrorStream(true).start();
        String output = new String(process.getInputStream().readAllBytes(), StandardCharsets.UTF_8);
        process.waitFor();
        return output.contains(flag);
    }

    public static RunResult run(List<String> flags, int rounds)
            throws IOException, InterruptedException, URISyntaxException {
        String classes = Path.of(Workload.class.getProtectionDomain().getCodeSource().getLocation().toURI())
                .toString();
        List<String> command = new ArrayList<>();
        command.add(javaBinary());
        command.addAll(flags);
        command.add("-Xlog:gc+init");
        command.addAll(List.of("-cp", classes, Workload.class.getName(), String.valueOf(rounds)));

        long begin = System.nanoTime();
        Process process = new ProcessBuilder(command).redirectErrorStream(true).start();
        long startup = -1;
        List<Long> roundNanos = new ArrayList<>();
        List<String> gcInit = new ArrayList<>();
        try (BufferedReader reader = new BufferedReader(
                new InputStreamReader(process.getInputStream(), StandardCharsets.UTF_8))) {
            String line;
            while ((line = reader.readLine()) != null) {
                if (line.equals("READY")) {
                    startup = System.nanoTime() - begin;
                } else if (line.startsWith("ROUND ")) {
                    roundNanos.add(Long.parseLong(line.substring(line.lastIndexOf(' ') + 1)));
                } else if (line.contains("[gc,init]")) {
                    gcInit.add(line);
                }
            }
        }
        int exit = process.waitFor();
        if (exit != 0 || startup < 0) {
            throw new IllegalStateException("Child JVM failed with exit code " + exit + ": " + command);
        }
        return new RunResult(startup, roundNanos, gcInit);
    }
}
