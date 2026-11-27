package com.geekyhacker.jpaprojections;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Join;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Root;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.support.PageableExecutionUtils;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;

/**
 * 3. Read model built once with the Criteria API. A null filter adds no predicate
 * and the customer join is only added to the count query when it is needed.
 */
@Repository
public class OrderReadModel {

    @PersistenceContext
    private EntityManager em;

    public Page<OrderRow> search(OrderFilter filter, Pageable pageable) {
        CriteriaBuilder cb = em.getCriteriaBuilder();

        CriteriaQuery<OrderRow> query = cb.createQuery(OrderRow.class);
        Root<PurchaseOrder> o = query.from(PurchaseOrder.class);
        Join<PurchaseOrder, Customer> c = o.join("customer");
        query.select(cb.construct(OrderRow.class,
                        o.get("id"), o.get("status"), o.get("total"), c.get("name")))
                .where(predicates(cb, o, c, filter))
                .orderBy(cb.asc(o.get("id")));

        List<OrderRow> rows = em.createQuery(query)
                .setFirstResult((int) pageable.getOffset())
                .setMaxResults(pageable.getPageSize())
                .getResultList();

        return PageableExecutionUtils.getPage(rows, pageable, () -> count(cb, filter));
    }

    private long count(CriteriaBuilder cb, OrderFilter filter) {
        CriteriaQuery<Long> query = cb.createQuery(Long.class);
        Root<PurchaseOrder> o = query.from(PurchaseOrder.class);
        Join<PurchaseOrder, Customer> c = filter.country() != null ? o.join("customer") : null;
        query.select(cb.count(o)).where(predicates(cb, o, c, filter));
        return em.createQuery(query).getSingleResult();
    }

    private Predicate[] predicates(CriteriaBuilder cb, Root<PurchaseOrder> o,
                                   Join<PurchaseOrder, Customer> c, OrderFilter filter) {
        List<Predicate> predicates = new ArrayList<>();
        if (filter.status() != null) {
            predicates.add(cb.equal(o.get("status"), filter.status()));
        }
        if (filter.country() != null) {
            predicates.add(cb.equal(c.get("country"), filter.country()));
        }
        return predicates.toArray(new Predicate[0]);
    }
}
