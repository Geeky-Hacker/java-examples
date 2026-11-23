package com.geekyhacker.awsemulator;

import java.util.List;

import software.amazon.awssdk.core.sync.RequestBody;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.CreateBucketRequest;
import software.amazon.awssdk.services.s3.model.GetObjectRequest;
import software.amazon.awssdk.services.s3.model.PutObjectRequest;
import software.amazon.awssdk.services.sqs.SqsClient;
import software.amazon.awssdk.services.sqs.model.CreateQueueRequest;
import software.amazon.awssdk.services.sqs.model.DeleteMessageRequest;
import software.amazon.awssdk.services.sqs.model.Message;
import software.amazon.awssdk.services.sqs.model.ReceiveMessageRequest;
import software.amazon.awssdk.services.sqs.model.SendMessageRequest;

public class DocumentService {

    private final S3Client s3;
    private final SqsClient sqs;
    private final String bucket;
    private final String queueName;
    private String queueUrl;

    public DocumentService(S3Client s3, SqsClient sqs, String bucket, String queueName) {
        this.s3 = s3;
        this.sqs = sqs;
        this.bucket = bucket;
        this.queueName = queueName;
    }

    /** Creates the bucket and the queue (createQueue is idempotent for identical attributes). */
    public void provision() {
        s3.createBucket(CreateBucketRequest.builder().bucket(bucket).build());
        queueUrl = sqs.createQueue(CreateQueueRequest.builder().queueName(queueName).build()).queueUrl();
    }

    public void upload(String key, String content) {
        s3.putObject(PutObjectRequest.builder().bucket(bucket).key(key).build(),
                RequestBody.fromString(content));
        sqs.sendMessage(SendMessageRequest.builder()
                .queueUrl(requireQueueUrl())
                .messageBody(key)
                .build());
    }

    public String download(String key) {
        return s3.getObjectAsBytes(GetObjectRequest.builder().bucket(bucket).key(key).build())
                .asUtf8String();
    }

    public List<String> pollUploadedKeys() {
        List<Message> messages = sqs.receiveMessage(ReceiveMessageRequest.builder()
                .queueUrl(requireQueueUrl())
                .maxNumberOfMessages(10)
                .waitTimeSeconds(1)
                .build()).messages();
        messages.forEach(m -> sqs.deleteMessage(DeleteMessageRequest.builder()
                .queueUrl(requireQueueUrl())
                .receiptHandle(m.receiptHandle())
                .build()));
        return messages.stream().map(Message::body).toList();
    }

    private String requireQueueUrl() {
        if (queueUrl == null) {
            throw new IllegalStateException("provision() must be called first");
        }
        return queueUrl;
    }
}
