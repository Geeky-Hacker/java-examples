package com.geekyhacker.jpaprojections;

import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;

import java.math.BigDecimal;

@Entity
public class PurchaseOrder {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String status;

    private BigDecimal total;

    @ManyToOne(fetch = FetchType.LAZY)
    private Customer customer;

    protected PurchaseOrder() {
    }

    public PurchaseOrder(String status, BigDecimal total, Customer customer) {
        this.status = status;
        this.total = total;
        this.customer = customer;
    }

    public Long getId() {
        return id;
    }

    public String getStatus() {
        return status;
    }

    public BigDecimal getTotal() {
        return total;
    }

    public Customer getCustomer() {
        return customer;
    }
}
