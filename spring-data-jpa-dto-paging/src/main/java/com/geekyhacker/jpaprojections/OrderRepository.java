package com.geekyhacker.jpaprojections;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface OrderRepository extends JpaRepository<PurchaseOrder, Long> {

    // 1. Constructor expression with an explicit count query
    @Query(value = """
            select new com.geekyhacker.jpaprojections.OrderRow(o.id, o.status, o.total, c.name)
            from PurchaseOrder o join o.customer c
            where (:status is null or o.status = :status)
              and (:country is null or c.country = :country)
            """,
            countQuery = """
            select count(o) from PurchaseOrder o join o.customer c
            where (:status is null or o.status = :status)
              and (:country is null or c.country = :country)
            """)
    Page<OrderRow> findRows(@Param("status") String status,
                            @Param("country") String country,
                            Pageable pageable);

    // 2. Closed interface projection, count query is derived
    Page<OrderSummary> findByStatus(String status, Pageable pageable);
}
