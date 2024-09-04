# Related articles

- [How to mock @Value field in Spring Boot](https://www.geekyhacker.com/how-to-mock-at-value-field-in-spring-boot/)


## LocalStack

To run LocalStack in this project:

```bash
$ docker-compose up
```

After that you can access the LocalStack environment on `http://localhost:4566`. For example, to get list of secrets:

```bash
$ aws --endpoint-url=http://localhost:4566 --region=eu-central-1 secretsmanager list-secrets
```
