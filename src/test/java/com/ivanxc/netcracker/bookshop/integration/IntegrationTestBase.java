package com.ivanxc.netcracker.bookshop.integration;

import org.junit.jupiter.api.BeforeAll;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.springframework.test.context.jdbc.Sql;
import org.testcontainers.containers.PostgreSQLContainer;

@Sql(scripts = "/db/truncate_tables_filled_by_flyway_migrations.sql")
@ActiveProfiles("test")
public abstract class IntegrationTestBase {

    private static final PostgreSQLContainer<?> container =
        new PostgreSQLContainer<>("postgres:14.3")
            .withUsername("postgres")
            .withPassword("lab6-test")
            .withDatabaseName("lab6-tests");

    @BeforeAll
    static void runContainer() {
        container.start();
    }

    @DynamicPropertySource
    static void postgresProperties(DynamicPropertyRegistry registry) {
        registry.add("spring.datasource.url", container::getJdbcUrl);
        registry.add("spring.datasource.username", container::getUsername);
        registry.add("spring.datasource.password", container::getPassword);
    }

}
