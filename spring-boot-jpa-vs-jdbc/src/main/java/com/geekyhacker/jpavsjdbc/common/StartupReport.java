package com.geekyhacker.jpavsjdbc.common;

import java.lang.management.ManagementFactory;

import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

@Component
public class StartupReport {

    @EventListener(ApplicationReadyEvent.class)
    void report() {
        long startupMs = System.currentTimeMillis()
                - ManagementFactory.getRuntimeMXBean().getStartTime();
        String rss = ResidentMemory.currentMib()
                .stream().mapToObj(m -> String.format("%.1f", m)).findFirst().orElse("n/a");
        System.out.println("STARTUP_MS=" + startupMs + " RSS_MIB=" + rss);
    }
}
