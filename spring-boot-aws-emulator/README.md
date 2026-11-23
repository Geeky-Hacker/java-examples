# spring-boot-aws-emulator

A Spring Boot app that stores documents in S3 and announces them on SQS, tested with Mockito and, when `AWS_ENDPOINT_URL` is set, against the official AWS Emulator.
Run `mvn verify` for the offline tests. To include the emulator test, start AWS Emulator, export the SDK environment variables and pass `-Demulator.truststore.args="-Djavax.net.ssl.trustStore=$HOME/.aws/emulator/9297/truststore.p12 -Djavax.net.ssl.trustStoreType=PKCS12"`.
