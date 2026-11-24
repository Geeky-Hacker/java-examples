# zgc-startup-warmup

A small harness that starts a fresh JVM per scenario and measures startup and warmup with ZGC defaults versus `-XX:+ZAdaptiveHeapSizing` (JEP 545 / JEP 546, JDK 28), and checks if the kernel supports large page upgrades (Linux 6.1+).
Run: `mvn -q package && java -cp target/classes com.geekyhacker.zgc.Benchmark` (use a JDK 28 EA build to include the adaptive scenario).
