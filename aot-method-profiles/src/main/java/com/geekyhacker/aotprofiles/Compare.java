package com.geekyhacker.aotprofiles;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.LinkedHashMap;
import java.util.Map;

/**
 * Usage: Compare no-cache.csv with-profiles.csv without-profiles.csv
 */
public final class Compare {

    private static final String[] NAMES = {"no cache", "cache + profiles", "cache - profiles"};

    private Compare() {
    }

    public static void main(String[] args) throws IOException {
        if (args.length != NAMES.length) {
            System.err.println("Usage: Compare no-cache.csv with-profiles.csv without-profiles.csv");
            System.exit(1);
        }
        Map<String, double[]> variants = new LinkedHashMap<>();
        for (int i = 0; i < NAMES.length; i++) {
            variants.put(NAMES[i], Stats.parseCsv(Files.readAllLines(Path.of(args[i]))));
        }
        System.out.print(Report.format(variants));
    }
}
