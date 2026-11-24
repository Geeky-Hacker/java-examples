package com.geekyhacker.oom;

import java.io.IOException;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

/**
 * Five heap OOM patterns. Run with a small heap, for example -Xmx64m.
 * Usage: Scenarios (cache|batch|report|threadlocal|connections) (broken|fixed)
 */
public final class Scenarios {

    private static final StringBuilder REPORT = new StringBuilder();

    private static final ThreadLocal<List<byte[]>> CONTEXT =
            ThreadLocal.withInitial(ArrayList::new);

    // Keeps the JIT from optimizing the allocations away.
    static volatile long checksum;

    private Scenarios() {
    }

    public static void main(String[] args) throws Exception {
        if (args.length != 2) {
            System.err.println("Usage: Scenarios <scenario> <broken|fixed>");
            System.exit(2);
        }
        boolean fixed = "fixed".equals(args[1]);
        switch (args[0]) {
            case "cache" -> cache(fixed);
            case "batch" -> batch(fixed);
            case "report" -> report(fixed);
            case "threadlocal" -> threadLocal(fixed);
            case "connections" -> connections(fixed);
            default -> {
                System.err.println("Unknown scenario: " + args[0]);
                System.exit(2);
            }
        }
        System.out.println("DONE");
    }

    static void cache(boolean fixed) {
        Map<Integer, byte[]> cache = fixed ? new LruCache<>(1_000) : new HashMap<>();
        for (int i = 0; i < 1_000_000; i++) {
            cache.put(i, new byte[1024]);
        }
        checksum += cache.size();
    }

    static void batch(boolean fixed) {
        int total = 250 * 1024 * 1024;
        if (!fixed) {
            byte[] everything = new byte[total];
            checksum += everything.length;
            return;
        }
        byte[] chunk = new byte[1024 * 1024];
        for (int read = 0; read < total; read += chunk.length) {
            checksum += chunk.length;
        }
    }

    static void report(boolean fixed) throws IOException {
        String line = "x".repeat(100_000);
        try (Writer out = new OutputStreamWriter(OutputStream.nullOutputStream())) {
            for (int i = 0; i < 2_000; i++) {
                if (fixed) {
                    out.write(line);
                } else {
                    REPORT.append(line);
                }
            }
        }
        checksum += REPORT.length();
    }

    static void threadLocal(boolean fixed) throws InterruptedException {
        ExecutorService pool = Executors.newFixedThreadPool(4);
        for (int i = 0; i < 2_000; i++) {
            pool.execute(() -> {
                try {
                    CONTEXT.get().add(new byte[100 * 1024]);
                } finally {
                    if (fixed) {
                        CONTEXT.remove();
                    }
                }
            });
        }
        pool.shutdown();
        pool.awaitTermination(1, TimeUnit.MINUTES);
    }

    static void connections(boolean fixed) {
        Pool pool = new Pool();
        for (int i = 0; i < 1_000; i++) {
            if (fixed) {
                try (Connection connection = pool.acquire()) {
                    checksum += connection.buffer().length;
                }
            } else {
                checksum += pool.acquire().buffer().length;
            }
        }
    }

    static class LruCache<K, V> extends LinkedHashMap<K, V> {
        private final int maxEntries;

        LruCache(int maxEntries) {
            super(16, 0.75f, true);
            this.maxEntries = maxEntries;
        }

        @Override
        protected boolean removeEldestEntry(Map.Entry<K, V> eldest) {
            return size() > maxEntries;
        }
    }

    static class Pool {
        private final Set<Connection> open = new HashSet<>();

        Connection acquire() {
            Connection connection = new Connection(this, new byte[1024 * 1024]);
            open.add(connection);
            return connection;
        }
    }

    // Identity-based equals/hashCode on purpose (a record would hash the 1 MB buffer by identity too,
    // but a plain class makes the intent clearer).
    static final class Connection implements AutoCloseable {
        private final Pool pool;
        private final byte[] buffer;

        Connection(Pool pool, byte[] buffer) {
            this.pool = pool;
            this.buffer = buffer;
        }

        byte[] buffer() {
            return buffer;
        }

        @Override
        public void close() {
            pool.open.remove(this);
        }
    }
}
