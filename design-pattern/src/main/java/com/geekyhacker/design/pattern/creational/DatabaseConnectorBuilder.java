package com.geekyhacker.design.pattern.creational;

public class DatabaseConnectorBuilder {
  private String username;
  private String password;
  private String url;
  private int port;

  public static DatabaseConnectorBuilder newBuilder() {
    return new DatabaseConnectorBuilder();
  }

  public DatabaseConnectorBuilder withUsername(String username) {
    this.username = username;
    return this;
  }

  public DatabaseConnectorBuilder withPassword(String password) {
    this.password = password;
    return this;
  }

  public DatabaseConnectorBuilder withUrl(String url) {
    this.url = url;
    return this;
  }

  public DatabaseConnectorBuilder withPort(int port) {
    this.port = port;
    return this;
  }

  public DatabaseConnector build() {
    return new DatabaseConnector(username, password, url, port);
  }
}
