package com.geekyhacker.jdbc;

import com.geekyhacker.jdbc.database.Database;

import java.util.List;

public class Application {

  public static void main(String[] args) {
    Database database = new Database();
    List<String> columnNames = database.getTableColumnNames();
    columnNames.forEach(System.out::println);
  }
}
