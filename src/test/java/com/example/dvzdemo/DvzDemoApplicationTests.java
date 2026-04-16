package com.example.dvzdemo;

import com.example.dvzdemo.support.AbstractPostgreSqlContainerTest;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest(properties = "vaadin.devmode.enabled=false")
class DvzDemoApplicationTests extends AbstractPostgreSqlContainerTest {

    @Test
    void contextLoads() {
    }
}
