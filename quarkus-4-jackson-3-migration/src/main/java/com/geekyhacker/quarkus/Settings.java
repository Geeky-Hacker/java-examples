package com.geekyhacker.quarkus;

import java.time.Duration;

public record Settings(String name, int retries, Duration timeout) {
}
