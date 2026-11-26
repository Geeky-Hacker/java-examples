package com.geekyhacker.jpavsjdbc.jpa;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name = "project")
public class ProjectEntity {

    @Id
    private Long id;
    private String name;
    private int stars;

    protected ProjectEntity() {
    }

    public Long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public int getStars() {
        return stars;
    }
}
