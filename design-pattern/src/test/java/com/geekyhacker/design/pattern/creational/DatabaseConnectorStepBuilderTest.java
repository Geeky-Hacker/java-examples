package com.geekyhacker.design.pattern.creational;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class DatabaseConnectorStepBuilderTest {

  @Test
  void shouldBuildDatabaseConnector() {
    DatabaseConnector connector = DatabaseConnectorStepBuilder.newBuilder()
        .withUsername("root")
        .withPassword("secret")
        .withUrl("localhost")
        .withPort(3306)
        .build();

    assertEquals("root", connector.getUsername());
    assertEquals("secret", connector.getPassword());
    assertEquals("localhost", connector.getUrl());
    assertEquals(3306, connector.getPort());
  }
}
