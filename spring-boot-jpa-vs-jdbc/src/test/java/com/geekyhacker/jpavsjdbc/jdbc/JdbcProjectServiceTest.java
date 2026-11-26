package com.geekyhacker.jpavsjdbc.jdbc;

import static org.assertj.core.api.Assertions.assertThat;

import com.geekyhacker.jpavsjdbc.common.Project;
import com.geekyhacker.jpavsjdbc.common.ProjectService;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest(classes = JdbcApp.class)
class JdbcProjectServiceTest {

    @Autowired
    private ProjectService service;

    @Test
    void returnsTopThreeProjectsByStars() {
        assertThat(service.topProjects())
                .extracting(Project::name)
                .containsExactly("spring-boot", "quarkus", "hibernate-orm");
    }
}
