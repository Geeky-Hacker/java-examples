package com.geekyhacker.hibernate.migration;

import java.util.List;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.StatelessSession;
import org.hibernate.cfg.Configuration;

public class ProductRepository implements AutoCloseable {

    private final SessionFactory sessionFactory;

    public ProductRepository() {
        this.sessionFactory = new Configuration()
                .setProperty("hibernate.connection.url", "jdbc:h2:mem:migration;DB_CLOSE_DELAY=-1")
                .setProperty("hibernate.connection.username", "sa")
                .setProperty("hibernate.connection.password", "")
                .setProperty("hibernate.hbm2ddl.auto", "create-drop")
                .addAnnotatedClass(Product.class)
                .buildSessionFactory();
    }

    public void save(Product product) {
        sessionFactory.inTransaction(session -> session.persist(product));
    }

    // insert() returns a value on 7.x and void on 8.0, so we ignore the result
    public void saveStateless(Product product) {
        try (StatelessSession stateless = sessionFactory.openStatelessSession()) {
            Transaction tx = stateless.beginTransaction();
            stateless.insert(product);
            tx.commit();
        }
    }

    public List<ProductSummary> findSummaries() {
        return sessionFactory.fromTransaction(session -> session.createQuery(
                "select new com.geekyhacker.hibernate.migration.ProductSummary(p.name, p.priceCents) "
                        + "from Product p order by p.name", ProductSummary.class).getResultList());
    }

    public Long firstId() {
        return sessionFactory.fromTransaction(session -> session.createQuery(
                "select min(p.id) from Product p", Long.class).getSingleResult());
    }

    @Override
    public void close() {
        sessionFactory.close();
    }
}
