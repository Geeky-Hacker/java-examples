# Related articles

- [How to mock @Value field in Spring Boot](https://www.geekyhacker.com/how-to-mock-at-value-field-in-spring-boot/)


## LocalStack

To run LocalStack in this project:

```bash
$ docker-compose up
```

To use AWS Secrets manager secret with the application, go to the `scripts` directory and run:

```bash
$ ./create_secret.sh
```

That creates a secret for `api.key` property of the application. Without that, the application defaults to `testKey` value and on test to `fakeApiKey` value.

After that you can access the LocalStack environment on `http://localhost:4566`. For example, to get list of secrets:

```bash
$ aws --endpoint-url=http://localhost:4566 --region=eu-central-1 secretsmanager list-secrets
```
