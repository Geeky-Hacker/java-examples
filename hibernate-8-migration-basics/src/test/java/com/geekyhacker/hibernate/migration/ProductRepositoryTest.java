package com.geekyhacker.hibernate.migration;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import java.util.List;
import org.junit.jupiter.api.Test;

class ProductRepositoryTest {

    @Test
    void constructorExpressionReplacesResultTransformer() {
        try (ProductRepository repository = new ProductRepository()) {
            repository.save(new Product("Monitor", 19900));
            repository.save(new Product("Cable", 900));

            List<ProductSummary> summaries = repository.findSummaries();

            assertEquals(List.of(
                    new ProductSummary("Cable", 900),
                    new ProductSummary("Monitor", 19900)), summaries);
        }
    }

    @Test
    void statelessInsertPersistsRow() {
        try (ProductRepository repository = new ProductRepository()) {
            repository.saveStateless(new Product("Keyboard", 4500));

            assertEquals(List.of(new ProductSummary("Keyboard", 4500)), repository.findSummaries());
            assertNotNull(repository.firstId());
        }
    }
}
