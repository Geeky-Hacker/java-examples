package com.geekyhacker.springintegration;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.util.UUID;

import javax.sql.DataSource;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.integration.jdbc.store.JdbcMessageStore;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.datasource.embedded.EmbeddedDatabase;
import org.springframework.jdbc.datasource.embedded.EmbeddedDatabaseBuilder;
import org.springframework.jdbc.datasource.embedded.EmbeddedDatabaseType;
import org.springframework.messaging.Message;
import org.springframework.messaging.support.GenericMessage;

class JdbcMessageStoreAllowListTest {

    @BeforeEach
    void resetCounter() {
        HarmlessGadget.READ_COUNT.set(0);
    }

    @Test
    void allowListSurvivesContainerStartup() {
        try (var context = new AnnotationConfigApplicationContext(AllowListConfig.class)) {
            JdbcMessageStore store = context.getBean(JdbcMessageStore.class);
            UUID id = GadgetSupport.insertGadgetRow(new JdbcTemplate(context.getBean(DataSource.class)));

            assertThrows(Exception.class, () -> store.getMessage(id));
            assertEquals(0, HarmlessGadget.READ_COUNT.get(), "gadget must be rejected by the allow-list");
        }
    }

    @Test
    void explicitDeserializerKeepsAllowList() {
        try (var context = new AnnotationConfigApplicationContext(WorkaroundConfig.class)) {
            JdbcMessageStore store = context.getBean(JdbcMessageStore.class);
            UUID id = GadgetSupport.insertGadgetRow(new JdbcTemplate(context.getBean(DataSource.class)));

            assertThrows(Exception.class, () -> store.getMessage(id));
            assertEquals(0, HarmlessGadget.READ_COUNT.get(), "gadget must be rejected by the allow-list");
        }
    }

    @Test
    void controlGadgetIsDeserializedWhenAllowed() {
        EmbeddedDatabase database = new EmbeddedDatabaseBuilder()
                .generateUniqueName(true)
                .setType(EmbeddedDatabaseType.H2)
                .addScript("classpath:org/springframework/integration/jdbc/schema-h2.sql")
                .build();
        try {
            JdbcMessageStore store = new JdbcMessageStore(database);
            store.addAllowedPatterns(HarmlessGadget.class.getName());
            UUID id = GadgetSupport.insertGadgetRow(new JdbcTemplate(database));

            assertThrows(Exception.class, () -> store.getMessage(id));
            assertEquals(1, HarmlessGadget.READ_COUNT.get(), "harness must detect a deserialization");
        }
        finally {
            database.shutdown();
        }
    }

    @Test
    void regularMessagesStillRoundTrip() {
        try (var context = new AnnotationConfigApplicationContext(AllowListConfig.class)) {
            JdbcMessageStore store = context.getBean(JdbcMessageStore.class);
            Message<String> message = new GenericMessage<>("hello");
            store.addMessage(message);

            Message<?> loaded = store.getMessage(message.getHeaders().getId());
            assertEquals("hello", loaded.getPayload());
        }
    }
}
