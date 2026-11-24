# JVM heap OOM patterns

Five reproducible `OutOfMemoryError: Java heap space` scenarios (unbounded cache, oversized batch, report builder, ThreadLocal leak, leaked connections), each with a broken and a fixed variant.
Run `mvn verify` to start every scenario in a child JVM with `-Xmx64m`, or run one by hand: `java -Xmx64m -cp target/classes com.geekyhacker.oom.Scenarios cache broken`.
