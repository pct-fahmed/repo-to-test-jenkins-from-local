package com.example.dvzdemo.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.License;
import io.swagger.v3.oas.models.servers.Server;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.List;

@Configuration
public class OpenApiConfig {

    @Bean
    public OpenAPI commerceApi() {
        return new OpenAPI()
            .info(new Info()
                .title("Commerce API")
                .version("v1")
                .description("Versioned REST API for products, inventories, orders, and related items.")
                .contact(new Contact().name("Commerce Admin"))
                .license(new License().name("Internal Use")))
            .servers(List.of(new Server().url("/").description("Current server")));
    }
}
