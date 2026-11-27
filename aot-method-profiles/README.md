# AOT method profiles benchmark

A small lambda-heavy benchmark that prints per-round timings, plus a `Compare` tool that summarises warmup and steady-state speed for three variants: no AOT cache, AOT cache with profiles (JEP 515), and AOT cache without profiles.
Run `mvn package` and then `./run-benchmark.sh` on JDK 25 or newer; `mvn verify` runs the tests on JDK 21+.

# Related articles

- [Measuring JEP 515 AOT method profiles on JDK 25](https://www.geekyhacker.com/jep-515-aot-method-profiles/)
