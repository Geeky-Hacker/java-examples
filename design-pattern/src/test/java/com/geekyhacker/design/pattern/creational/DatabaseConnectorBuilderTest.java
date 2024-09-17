package com.geekyhacker.design.pattern.creational;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

public class DatabaseConnectorBuilderTest {

  @Test
  void shouldBuildDatabaseConnectorFully() {
    DatabaseConnector connector = DatabaseConnectorBuilder.newBuilder()
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

  @Test
  void shouldBuildDatabaseConnectorWithoutUrl() {
    DatabaseConnector connector = DatabaseConnectorBuilder.newBuilder()
        .withUsername("root")
        .withPassword("secret")
        .withPort(3306)
        .build();

    assertEquals("root", connector.getUsername());
    assertEquals("secret", connector.getPassword());
    assertEquals(3306, connector.getPort());
    assertNull(connector.getUrl());
  }

  @Test
  void shouldBuildDatabaseConnectorWithoutAnyParameter() {
    DatabaseConnector connector = DatabaseConnectorBuilder.newBuilder().build();

    assertNull(connector.getUsername());
    assertNull(connector.getPassword());
    assertNull(connector.getUrl());
    assertEquals(0, connector.getPort());
  }
}
