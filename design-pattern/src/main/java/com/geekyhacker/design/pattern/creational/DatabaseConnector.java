package com.geekyhacker.design.pattern.creational;

public class DatabaseConnector {
  private final String username;
  private final String password;
  private final String url;
  private final int port;
  
  DatabaseConnector(String username, String password, String url, int port) {
    this.username = username;
    this.password = password;
    this.url = url;
    this.port = port;
  }

  public String getUsername() {
    return username;
  }

  public String getPassword() {
    return password;
  }

  public String getUrl() {
    return url;
  }

  public int getPort() {
    return port;
  }

  public boolean connect() {
    System.out.println("Connecting to the database");
    return true;
  }
}
