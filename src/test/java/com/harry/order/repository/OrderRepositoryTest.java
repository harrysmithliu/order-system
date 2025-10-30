package com.harry.order.repository;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
public class OrderRepositoryTest {

    @Autowired OrderRepository repo;

    @Test
    void findOrderSummaries_shouldWork() {
        var page = repo.findOrderSummaries(
                null, null, null, null, null, null,
                org.springframework.data.domain.PageRequest.of(0, 10)
        );
        assertThat(page).isNotNull();
        // 如果 H2 与 MySQL 语法不兼容、或没数据，可用 Testcontainers MySQL 替代
    }
}
