package com.example.dvzdemo;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;

@SpringBootTest
class DvzDemoApplicationTests {

    @DynamicPropertySource
    static void configureDatasource(DynamicPropertyRegistry registry) {
        String datasourceUrl = System.getenv("SPRING_DATASOURCE_URL");

        if (datasourceUrl != null && !datasourceUrl.isBlank()) {
            registry.add("spring.datasource.url", () -> datasourceUrl);
            registry.add("spring.datasource.driver-class-name", () -> "org.postgresql.Driver");
            registry.add("spring.datasource.username", () -> System.getenv().getOrDefault("SPRING_DATASOURCE_USERNAME", "dvz"));
            registry.add("spring.datasource.password", () -> System.getenv().getOrDefault("SPRING_DATASOURCE_PASSWORD", "dvz"));
        } else {
            registry.add("spring.datasource.url", () -> "jdbc:h2:mem:testdb;MODE=PostgreSQL;DB_CLOSE_DELAY=-1");
            registry.add("spring.datasource.driver-class-name", () -> "org.h2.Driver");
            registry.add("spring.datasource.username", () -> "sa");
            registry.add("spring.datasource.password", () -> "");
        }

        registry.add("spring.jpa.open-in-view", () -> "false");
    }

    @Test
    void contextLoads() {
    }
}
