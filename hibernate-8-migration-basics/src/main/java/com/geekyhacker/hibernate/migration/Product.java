package com.geekyhacker.hibernate.migration;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.SequenceGenerator;

@Entity
public class Product {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "product_seq")
    @SequenceGenerator(name = "product_seq", sequenceName = "product_seq", allocationSize = 1)
    private Long id;

    private String name;

    private long priceCents;

    protected Product() {
    }

    public Product(String name, long priceCents) {
        this.name = name;
        this.priceCents = priceCents;
    }

    public Long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public long getPriceCents() {
        return priceCents;
    }
}
