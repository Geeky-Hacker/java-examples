package com.geekyhacker.design.pattern.creational;

public class DatabaseConnectorStepBuilder {
  private DatabaseConnectorStepBuilder() {
  }

  public static UsernameBuilder newBuilder() {
    return new Builder();
  }

  public interface UsernameBuilder {
    PasswordBuilder withUsername(String username);
  }

  public interface PasswordBuilder {
    UrlBuilder withPassword(String password);
  }

  public interface UrlBuilder {
    PortBuilder withUrl(String url);
  }

  public interface PortBuilder {
    BuildStep withPort(int port);
  }

  public interface BuildStep {
    DatabaseConnector build();
  }

  private static class Builder implements UsernameBuilder, PasswordBuilder, UrlBuilder, PortBuilder, BuildStep {
    private String username;
    private String password;
    private String url;
    private int port;

    private Builder() {

    }

    @Override
    public PasswordBuilder withUsername(String username) {
      this.username = username;
      return this;
    }

    @Override
    public UrlBuilder withPassword(String password) {
      this.password = password;
      return this;
    }

    @Override
    public PortBuilder withUrl(String url) {
      this.url = url;
      return this;
    }

    @Override
    public BuildStep withPort(int port) {
      this.port = port;
      return this;
    }

    @Override
    public DatabaseConnector build() {
      return new DatabaseConnector(username, password, url, port);
    }
  }
}
