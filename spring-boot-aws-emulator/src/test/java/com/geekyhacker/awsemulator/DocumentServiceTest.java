package com.geekyhacker.awsemulator;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.inOrder;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.nio.charset.StandardCharsets;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InOrder;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import software.amazon.awssdk.core.ResponseBytes;
import software.amazon.awssdk.core.sync.RequestBody;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.CreateBucketRequest;
import software.amazon.awssdk.services.s3.model.GetObjectRequest;
import software.amazon.awssdk.services.s3.model.GetObjectResponse;
import software.amazon.awssdk.services.s3.model.PutObjectRequest;
import software.amazon.awssdk.services.sqs.SqsClient;
import software.amazon.awssdk.services.sqs.model.CreateQueueRequest;
import software.amazon.awssdk.services.sqs.model.CreateQueueResponse;
import software.amazon.awssdk.services.sqs.model.DeleteMessageRequest;
import software.amazon.awssdk.services.sqs.model.Message;
import software.amazon.awssdk.services.sqs.model.ReceiveMessageRequest;
import software.amazon.awssdk.services.sqs.model.ReceiveMessageResponse;
import software.amazon.awssdk.services.sqs.model.SendMessageRequest;

@ExtendWith(MockitoExtension.class)
class DocumentServiceTest {

    private static final String QUEUE_URL = "https://127.0.0.1:9297/000000000000/uploads";

    @Mock
    private S3Client s3;

    @Mock
    private SqsClient sqs;

    private DocumentService service;

    @BeforeEach
    void setUp() {
        service = new DocumentService(s3, sqs, "documents", "uploads");
    }

    private void provision() {
        when(sqs.createQueue(any(CreateQueueRequest.class)))
                .thenReturn(CreateQueueResponse.builder().queueUrl(QUEUE_URL).build());
        service.provision();
    }

    @Test
    void provisionCreatesBucketAndQueue() {
        provision();

        ArgumentCaptor<CreateBucketRequest> bucket = ArgumentCaptor.forClass(CreateBucketRequest.class);
        verify(s3).createBucket(bucket.capture());
        assertEquals("documents", bucket.getValue().bucket());

        ArgumentCaptor<CreateQueueRequest> queue = ArgumentCaptor.forClass(CreateQueueRequest.class);
        verify(sqs).createQueue(queue.capture());
        assertEquals("uploads", queue.getValue().queueName());
    }

    @Test
    void uploadWritesObjectBeforeSendingMessage() {
        provision();

        service.upload("hello.txt", "Hello");

        InOrder order = inOrder(s3, sqs);
        ArgumentCaptor<PutObjectRequest> put = ArgumentCaptor.forClass(PutObjectRequest.class);
        order.verify(s3).putObject(put.capture(), any(RequestBody.class));
        ArgumentCaptor<SendMessageRequest> send = ArgumentCaptor.forClass(SendMessageRequest.class);
        order.verify(sqs).sendMessage(send.capture());

        assertEquals("documents", put.getValue().bucket());
        assertEquals("hello.txt", put.getValue().key());
        assertEquals(QUEUE_URL, send.getValue().queueUrl());
        assertEquals("hello.txt", send.getValue().messageBody());
    }

    @Test
    void downloadReturnsObjectContent() {
        when(s3.getObjectAsBytes(any(GetObjectRequest.class))).thenReturn(
                ResponseBytes.fromByteArray(GetObjectResponse.builder().build(),
                        "Hello".getBytes(StandardCharsets.UTF_8)));

        assertEquals("Hello", service.download("hello.txt"));
    }

    @Test
    void pollReturnsBodiesAndDeletesEveryMessage() {
        provision();
        when(sqs.receiveMessage(any(ReceiveMessageRequest.class))).thenReturn(
                ReceiveMessageResponse.builder().messages(
                        Message.builder().body("a.txt").receiptHandle("r1").build(),
                        Message.builder().body("b.txt").receiptHandle("r2").build()).build());

        List<String> keys = service.pollUploadedKeys();

        assertEquals(List.of("a.txt", "b.txt"), keys);
        ArgumentCaptor<DeleteMessageRequest> deleted = ArgumentCaptor.forClass(DeleteMessageRequest.class);
        verify(sqs, org.mockito.Mockito.times(2)).deleteMessage(deleted.capture());
        assertEquals(List.of("r1", "r2"),
                deleted.getAllValues().stream().map(DeleteMessageRequest::receiptHandle).toList());
    }

    @Test
    void uploadFailsBeforeProvisioning() {
        assertThrows(IllegalStateException.class, () -> service.upload("x", "y"));
    }
}
