package com.geekyhacker.awsemulator;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.List;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.condition.EnabledIfEnvironmentVariable;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

/**
 * Runs against a real AWS Emulator, and only when AWS_ENDPOINT_URL is set in the environment.
 * Without it the class is skipped before the Spring context (and the AWS clients) are created.
 */
@SpringBootTest(properties = {
        "demo.bucket=docs-${random.int[100000,999999]}",
        "demo.queue-name=uploads-${random.int[100000,999999]}"
})
@EnabledIfEnvironmentVariable(named = "AWS_ENDPOINT_URL", matches = ".+")
class DocumentServiceEmulatorIT {

    @Autowired
    private DocumentService service;

    @Test
    void uploadedDocumentCanBeDownloadedAndIsAnnouncedOnTheQueue() {
        service.upload("hello.txt", "Hello emulator");

        assertEquals("Hello emulator", service.download("hello.txt"));
        assertEquals(List.of("hello.txt"), service.pollUploadedKeys());
    }
}
