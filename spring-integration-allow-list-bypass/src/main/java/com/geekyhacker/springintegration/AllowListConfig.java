package com.geekyhacker.springintegration;

import javax.sql.DataSource;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.integration.jdbc.store.JdbcMessageStore;
import org.springframework.jdbc.datasource.embedded.EmbeddedDatabase;
import org.springframework.jdbc.datasource.embedded.EmbeddedDatabaseBuilder;
import org.springframework.jdbc.datasource.embedded.EmbeddedDatabaseType;

/**
 * The configuration the advisory describes: a Spring-managed JdbcMessageStore with addAllowedPatterns().
 */
@Configuration
public class AllowListConfig {

    public static final String[] PATTERNS = {"org.springframework.messaging.*", "java.util.*", "java.lang.*"};

    @Bean
    public EmbeddedDatabase dataSource() {
        return new EmbeddedDatabaseBuilder()
                .generateUniqueName(true)
                .setType(EmbeddedDatabaseType.H2)
                .addScript("classpath:org/springframework/integration/jdbc/schema-h2.sql")
                .build();
    }

    @Bean
    public JdbcMessageStore messageStore(DataSource dataSource) {
        JdbcMessageStore store = new JdbcMessageStore(dataSource);
        store.addAllowedPatterns(PATTERNS);
        return store;
    }
}
