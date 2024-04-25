package com.geekyhacker.jdbc.database;

import org.apache.commons.lang3.StringUtils;

import java.sql.*;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class Database {

    private static final String DRIVER_NAME = "org.h2.Driver";

    private static final String JDBC_URL = "jdbc:h2:mem:students";

    private static final String CREATE_TABLE_STATEMENT = "CREATE TABLE students (id INT AUTO_INCREMENT PRIMARY KEY, name VARCHAR(50), age INT, study_year INT);";

    private static final String INSERT_STATEMENT = "INSERT INTO students (name, age, study_year) VALUES (?, ?, ?);";

    private final Connection connection;

    public Database() {
        this.connection = connect();
        createTable();
        seedTable();
    }

    public List<String> getTableColumnNames() {
        try {
            Statement statement = connection.createStatement();
            ResultSet resultSet = statement.executeQuery("SELECT * FROM students");
            ResultSetMetaData resultSetMetaData = resultSet.getMetaData();
            return IntStream.range(1, resultSetMetaData.getColumnCount() + 1).mapToObj(i -> getColumnNameQuietly(resultSetMetaData, i)).collect(Collectors.toList());
        } catch (SQLException exception) {
            throw new RuntimeException(exception);
        }
    }

    public boolean insertStudent(String name, int age, int semester) {
        try {
            int i = 0;
            PreparedStatement preparedStatement = connection.prepareStatement(INSERT_STATEMENT);
            preparedStatement.setString(++i, name);
            preparedStatement.setInt(++i, age);
            preparedStatement.setInt(++i, semester);
            return preparedStatement.executeUpdate() > 0;
        } catch (Exception exception) {
            throw new RuntimeException(exception);
        }
    }

    private void createTable() {
        try {
            Statement statement = connection.createStatement();
            statement.execute(CREATE_TABLE_STATEMENT);
        } catch (Exception ex) {
            throw new RuntimeException();
        }
    }

    private void seedTable() {
        insertStudent("James Doe", 22, 8);
        insertStudent("Myra Ellis", 32, 1);
        insertStudent("Hazel Brewer", 19, 2);
    }

    private Connection connect() {
        try {
            Class.forName(DRIVER_NAME);
            return DriverManager.getConnection(JDBC_URL, "sa", StringUtils.EMPTY);
        } catch (Exception exception) {
            throw new RuntimeException(exception);
        }
    }

    private String getColumnNameQuietly(ResultSetMetaData resultSetMetaData, int index) {
        try {
            return resultSetMetaData.getColumnName(index);
        } catch (SQLException exception) {
            throw new RuntimeException(exception);
        }
    }
}