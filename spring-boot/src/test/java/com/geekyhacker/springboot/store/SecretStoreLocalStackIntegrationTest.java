package com.geekyhacker.springboot.store;

import com.fasterxml.jackson.core.JsonProcessingException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.testcontainers.containers.localstack.LocalStackContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;
import org.testcontainers.utility.DockerImageName;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

@Testcontainers
@SpringBootTest
class SecretStoreLocalStackIntegrationTest {

  private static final String WEATHER_SECRET_NAME = "weather/api/credentials";
  private static final String SIMPLE_SECRET_NAME = "mySecret";
  private static final String SIMPLE_SECRET_VALUE = "mySecretValue";
  private static final String WEATHER_SECRET_KEY = "api.key.test";
  private static final String WEATHER_SECRET_KEY_VALUE = """
      {
          "api.key.test": "testApiKey"
      }
      """;

  @Container
  private static final LocalStackContainer localStackContainer = new LocalStackContainer(DockerImageName.parse("localstack/localstack"))
      .withServices(LocalStackContainer.Service.SECRETSMANAGER)
      .withEnv("LOCALSTACK_HOSTNAME", "localhost")
      .withEnv("HOSTNAME", "localhost");

  static {
    localStackContainer.setPortBindings(List.of("4566:4566"));
  }

  @Autowired
  private SecretStore secretStore;

  @BeforeEach
  void setSecretsManagerState() {
    secretStore.deleteSecret(WEATHER_SECRET_NAME, true);
    secretStore.deleteSecret(SIMPLE_SECRET_NAME, true);
  }

  @Test
  void testGetSecretAsMap() throws JsonProcessingException {
    secretStore.createSecret(WEATHER_SECRET_NAME, WEATHER_SECRET_KEY_VALUE);

    Map<String, String> secretKeyValue = secretStore.getSecretAsMap(WEATHER_SECRET_NAME);

    assertEquals(WEATHER_SECRET_KEY, new ArrayList<>(secretKeyValue.keySet()).getFirst());
    assertEquals("testApiKey", secretKeyValue.get(WEATHER_SECRET_KEY));
  }

  @Test
  void testGetSecret() {
    secretStore.createSecret(WEATHER_SECRET_NAME, WEATHER_SECRET_KEY_VALUE);

    String secretValue = secretStore.getSecretValue(WEATHER_SECRET_NAME);

    assertEquals(WEATHER_SECRET_KEY_VALUE, secretValue);
  }

  @Test
  void testGetSecretsList() {
    secretStore.createSecret(WEATHER_SECRET_NAME, WEATHER_SECRET_KEY_VALUE);

    List<String> secretNames = secretStore.listSecrets();

    assertEquals(1, secretNames.size());
    assertEquals(WEATHER_SECRET_NAME, secretNames.getFirst());
  }

  @Test
  void testCreateSecret() {
    secretStore.createSecret("mySecret", "mySecretValue");

    String secretValue = secretStore.getSecretValue("mySecret");
    assertEquals("mySecretValue", secretValue);
  }

  @Test
  void testDeleteSecret() {
    secretStore.createSecret(WEATHER_SECRET_NAME, WEATHER_SECRET_KEY_VALUE);
    secretStore.createSecret(SIMPLE_SECRET_NAME, SIMPLE_SECRET_VALUE);

    secretStore.deleteSecret(SIMPLE_SECRET_NAME, true);

    assertFalse(secretStore.listSecrets().contains(SIMPLE_SECRET_NAME));
    assertTrue(secretStore.listSecrets().contains(WEATHER_SECRET_NAME));
  }

  @Test
  void testDeleteAllSecrets() {
    secretStore.createSecret(WEATHER_SECRET_NAME, WEATHER_SECRET_KEY_VALUE);
    secretStore.createSecret(SIMPLE_SECRET_NAME, SIMPLE_SECRET_VALUE);

    secretStore.deleteAllSecrets();

    assertTrue(secretStore.listSecrets().isEmpty());
  }

  @Test
  void testUpdateSecret() {
    secretStore.createSecret(SIMPLE_SECRET_NAME, SIMPLE_SECRET_VALUE);

    assertEquals(SIMPLE_SECRET_VALUE, secretStore.getSecretValue(SIMPLE_SECRET_NAME));

    secretStore.updateSecret(SIMPLE_SECRET_NAME, "newValue");

    assertEquals("newValue", secretStore.getSecretValue(SIMPLE_SECRET_NAME));
  }
}