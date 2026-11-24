package com.geekyhacker.awsemulator;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.sqs.SqsClient;

/**
 * The clients use the default configuration, so endpoint, region and credentials come
 * from the environment (AWS_ENDPOINT_URL and friends). Nothing here knows about the emulator.
 */
@Configuration
class AwsConfig {

    @Bean
    S3Client s3Client() {
        return S3Client.create();
    }

    @Bean
    SqsClient sqsClient() {
        return SqsClient.create();
    }

    @Bean(initMethod = "provision")
    DocumentService documentService(S3Client s3, SqsClient sqs,
                                    @Value("${demo.bucket}") String bucket,
                                    @Value("${demo.queue-name}") String queueName) {
        return new DocumentService(s3, sqs, bucket, queueName);
    }
}
