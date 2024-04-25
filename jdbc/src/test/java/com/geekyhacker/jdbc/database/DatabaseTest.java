package com.geekyhacker.jdbc.database;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
class DatabaseTest {

    @InjectMocks
    private Database database;

    @Test
    void shouldRetrieveAllColumnsNames() {
        List<String> columnNames = database.getTableColumnNames();
        assertThat(columnNames).containsAll(List.of("ID", "NAME", "AGE", "STUDY_YEAR"));
    }
}