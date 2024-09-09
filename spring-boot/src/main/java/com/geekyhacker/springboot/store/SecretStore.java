package com.geekyhacker.springboot.store;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.stereotype.Service;
import software.amazon.awssdk.services.secretsmanager.SecretsManagerClient;
import software.amazon.awssdk.services.secretsmanager.model.*;

import java.util.List;
import java.util.Map;

@Service
public class SecretStore {

  private final SecretsManagerClient secretsManagerClient;
  private final ObjectMapper objectMapper;

  public SecretStore(SecretsManagerClient secretsManagerClient, ObjectMapper objectMapper) {
    this.secretsManagerClient = secretsManagerClient;
    this.objectMapper = objectMapper;
  }

  public Map<String, String> getSecretAsMap(String secretName) throws JsonProcessingException {
    return objectMapper.readValue(getSecretValue(secretName), new TypeReference<>() {
    });
  }

  public String getSecretValue(String secretName) {
    GetSecretValueResponse secretValueResponse = secretsManagerClient.getSecretValue(GetSecretValueRequest.builder().secretId(secretName).build());
    return secretValueResponse.secretString();
  }

  public List<String> listSecrets() {
    return secretsManagerClient.listSecrets().secretList().stream().map(SecretListEntry::name).toList();
  }

  public void createSecret(String secretName, String secretValue) {
    CreateSecretRequest createSecretRequest = CreateSecretRequest.builder()
        .name(secretName)
        .secretString(secretValue)
        .build();
    secretsManagerClient.createSecret(createSecretRequest);
  }

  public void deleteSecret(String secretName, boolean forceDelete) {
    secretsManagerClient.deleteSecret(DeleteSecretRequest.builder().secretId(secretName).forceDeleteWithoutRecovery(forceDelete).build());
  }

  public void deleteAllSecrets() {
    listSecrets().forEach(secretName -> deleteSecret(secretName, true));
  }

  public void updateSecret(String secretName, String secretValue) {
    secretsManagerClient.updateSecret(UpdateSecretRequest.builder().secretId(secretName).secretString(secretValue).build());
  }
}
