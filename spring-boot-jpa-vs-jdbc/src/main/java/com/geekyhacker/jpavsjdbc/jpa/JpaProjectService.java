package com.geekyhacker.jpavsjdbc.jpa;

import java.util.List;

import com.geekyhacker.jpavsjdbc.common.Project;
import com.geekyhacker.jpavsjdbc.common.ProjectService;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
class JpaProjectService implements ProjectService {

    private final ProjectJpaRepository repository;

    JpaProjectService(ProjectJpaRepository repository) {
        this.repository = repository;
    }

    @Override
    @Transactional(readOnly = true)
    public List<Project> topProjects() {
        return repository.findTop3ByOrderByStarsDesc().stream()
                .map(e -> new Project(e.getId(), e.getName(), e.getStars()))
                .toList();
    }
}
