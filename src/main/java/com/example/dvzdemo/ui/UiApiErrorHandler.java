package com.example.dvzdemo.ui;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClientResponseException;

@Component
public class UiApiErrorHandler {

    private final ObjectMapper objectMapper = new ObjectMapper();

    public RuntimeException toException(String fallbackMessage, RuntimeException exception) {
        if (exception instanceof RestClientResponseException responseException) {
            return new UiApiException(extractMessage(fallbackMessage, responseException), responseException);
        }
        return exception;
    }

    private String extractMessage(String fallbackMessage, RestClientResponseException exception) {
        String body = exception.getResponseBodyAsString();
        if (body != null && !body.isBlank()) {
            try {
                JsonNode jsonNode = objectMapper.readTree(body);
                if (jsonNode.hasNonNull("detail")) {
                    return jsonNode.get("detail").asText();
                }
                if (jsonNode.hasNonNull("message")) {
                    return jsonNode.get("message").asText();
                }
                if (jsonNode.hasNonNull("error")) {
                    return jsonNode.get("error").asText();
                }
            } catch (Exception ignored) {
                return fallbackMessage + ": " + body;
            }
        }
        return fallbackMessage + ": " + exception.getStatusCode();
    }
}
