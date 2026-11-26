package com.geekyhacker.jpavsjdbc.common;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.OptionalDouble;

public final class ResidentMemory {

    private ResidentMemory() {
    }

    /** Resident set size of this process in MiB, read with ps (Linux and macOS). */
    public static OptionalDouble currentMib() {
        try {
            Process process = new ProcessBuilder("ps", "-o", "rss=", "-p",
                    String.valueOf(ProcessHandle.current().pid())).start();
            String output = new String(process.getInputStream().readAllBytes(), StandardCharsets.UTF_8);
            process.waitFor();
            return parseKib(output);
        } catch (IOException e) {
            return OptionalDouble.empty();
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            return OptionalDouble.empty();
        }
    }

    /** Converts the output of ps (KiB) to MiB. */
    public static OptionalDouble parseKib(String psOutput) {
        try {
            return OptionalDouble.of(Long.parseLong(psOutput.trim()) / 1024.0);
        } catch (NumberFormatException e) {
            return OptionalDouble.empty();
        }
    }
}
