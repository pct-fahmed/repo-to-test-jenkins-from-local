package com.example.dvzdemo.ui.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestClient;

@Configuration
public class UiApiClientConfig {

    @Bean
    public RestClient uiRestClient(
        @Value("${ui.api.base-url:http://localhost:${server.port}}") String baseUrl
    ) {
        return RestClient.builder()
            .baseUrl(baseUrl)
            .build();
    }
}
