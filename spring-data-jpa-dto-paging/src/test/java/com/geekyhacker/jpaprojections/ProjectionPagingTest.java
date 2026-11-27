package com.geekyhacker.jpaprojections;

import jakarta.persistence.EntityManagerFactory;
import org.hibernate.SessionFactory;
import org.hibernate.stat.Statistics;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest(properties = {
        "spring.jpa.properties.hibernate.generate_statistics=true",
        "spring.datasource.url=jdbc:h2:mem:projections;DB_CLOSE_DELAY=-1"
})
class ProjectionPagingTest {

    @Autowired
    private OrderRepository orderRepository;

    @Autowired
    private CustomerRepository customerRepository;

    @Autowired
    private OrderReadModel readModel;

    @Autowired
    private EntityManagerFactory emf;

    private Statistics stats;

    @BeforeEach
    void seed() {
        orderRepository.deleteAll();
        customerRepository.deleteAll();

        List<Customer> customers = customerRepository.saveAll(List.of(
                new Customer("Alice", "NL"),
                new Customer("Bob", "DE"),
                new Customer("Chloe", "NL")));

        List<PurchaseOrder> orders = new ArrayList<>();
        for (int i = 1; i <= 25; i++) {
            orders.add(new PurchaseOrder(i % 2 == 0 ? "SHIPPED" : "NEW",
                    BigDecimal.valueOf(i * 10L), customers.get(i % 3)));
        }
        orderRepository.saveAll(orders);

        stats = emf.unwrap(SessionFactory.class).getStatistics();
    }

    @Test
    void constructorExpressionFiresTwoStatements() {
        stats.clear();

        Page<OrderRow> page = orderRepository.findRows(null, null, PageRequest.of(0, 10, Sort.by("id")));

        assertThat(page.getContent()).hasSize(10);
        assertThat(page.getTotalElements()).isEqualTo(25);
        assertThat(stats.getPrepareStatementCount()).isEqualTo(2);
    }

    @Test
    void constructorExpressionAppliesOptionalFilters() {
        Page<OrderRow> page = orderRepository.findRows("SHIPPED", "DE", PageRequest.of(0, 10, Sort.by("id")));

        assertThat(page.getTotalElements()).isEqualTo(4);
        assertThat(page.getContent()).allMatch(r -> r.customerName().equals("Bob"));
    }

    @Test
    void interfaceProjectionFiresTwoStatements() {
        stats.clear();

        Page<OrderSummary> page = orderRepository.findByStatus("SHIPPED", PageRequest.of(0, 5, Sort.by("id")));

        assertThat(page.getContent()).hasSize(5);
        assertThat(page.getTotalElements()).isEqualTo(12);
        assertThat(page.getContent().get(0).getTotal()).isNotNull();
        assertThat(stats.getPrepareStatementCount()).isEqualTo(2);
    }

    @Test
    void readModelFiresTwoStatements() {
        stats.clear();

        Page<OrderRow> page = readModel.search(new OrderFilter(null, null), PageRequest.of(0, 10));

        assertThat(page.getContent()).hasSize(10);
        assertThat(page.getTotalElements()).isEqualTo(25);
        assertThat(stats.getPrepareStatementCount()).isEqualTo(2);
    }

    @Test
    void readModelAndQueryReturnSameRows() {
        PageRequest pageable = PageRequest.of(0, 10, Sort.by("id"));

        Page<OrderRow> fromQuery = orderRepository.findRows("SHIPPED", "NL", pageable);
        Page<OrderRow> fromReadModel = readModel.search(new OrderFilter("SHIPPED", "NL"), pageable);

        assertThat(fromReadModel.getTotalElements()).isEqualTo(fromQuery.getTotalElements());
        assertThat(fromReadModel.getContent()).isEqualTo(fromQuery.getContent());
    }
}
