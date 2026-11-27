package com.geekyhacker.jpavsjdbc.jdbc;

import com.geekyhacker.jpavsjdbc.common.StartupReport;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Import;

@SpringBootApplication
@Import(StartupReport.class)
public class JdbcApp {

    public static void main(String[] args) {
        SpringApplication.run(JdbcApp.class, args);
    }
}
