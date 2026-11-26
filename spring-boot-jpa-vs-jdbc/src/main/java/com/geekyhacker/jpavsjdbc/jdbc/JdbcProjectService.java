package com.geekyhacker.jpavsjdbc.jdbc;

import java.util.List;

import com.geekyhacker.jpavsjdbc.common.Project;
import com.geekyhacker.jpavsjdbc.common.ProjectService;

import org.springframework.jdbc.core.simple.JdbcClient;
import org.springframework.stereotype.Service;

@Service
class JdbcProjectService implements ProjectService {

    private final JdbcClient jdbc;

    JdbcProjectService(JdbcClient jdbc) {
        this.jdbc = jdbc;
    }

    @Override
    public List<Project> topProjects() {
        return jdbc.sql("select id, name, stars from project order by stars desc limit 3")
                .query(Project.class)
                .list();
    }
}
