package com.geekyhacker.jpavsjdbc.jpa;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

public interface ProjectJpaRepository extends JpaRepository<ProjectEntity, Long> {

    List<ProjectEntity> findTop3ByOrderByStarsDesc();
}
