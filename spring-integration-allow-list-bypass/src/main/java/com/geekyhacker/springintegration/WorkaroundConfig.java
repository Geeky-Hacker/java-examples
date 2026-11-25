package com.geekyhacker.springintegration;

import javax.sql.DataSource;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.serializer.DefaultDeserializer;
import org.springframework.core.serializer.Deserializer;
import org.springframework.integration.jdbc.store.JdbcMessageStore;
import org.springframework.jdbc.datasource.embedded.EmbeddedDatabase;
import org.springframework.jdbc.datasource.embedded.EmbeddedDatabaseBuilder;
import org.springframework.jdbc.datasource.embedded.EmbeddedDatabaseType;

/**
 * Workaround from the advisory: set the deserializer explicitly so setBeanClassLoader() keeps it.
 */
@Configuration
public class WorkaroundConfig {

    @Bean
    public EmbeddedDatabase dataSource() {
        return new EmbeddedDatabaseBuilder()
                .generateUniqueName(true)
                .setType(EmbeddedDatabaseType.H2)
                .addScript("classpath:org/springframework/integration/jdbc/schema-h2.sql")
                .build();
    }

    @Bean
    @SuppressWarnings({"unchecked", "rawtypes"})
    public JdbcMessageStore messageStore(DataSource dataSource) {
        JdbcMessageStore store = new JdbcMessageStore(dataSource);
        store.setDeserializer((Deserializer) new DefaultDeserializer(getClass().getClassLoader()));
        store.addAllowedPatterns(AllowListConfig.PATTERNS);
        return store;
    }
}
