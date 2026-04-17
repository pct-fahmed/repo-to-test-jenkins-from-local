package com.example.dvzdemo.ui.customer.order;

import com.example.dvzdemo.commerce.customerorder.v1.CustomerOrderRequest;
import com.example.dvzdemo.commerce.customerorder.v1.CustomerOrderResponse;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClient;

import java.util.List;

@Component
public class CustomerOrderApiClient {

    private static final ParameterizedTypeReference<List<CustomerOrderResponse>> CUSTOMER_ORDER_LIST =
        new ParameterizedTypeReference<>() {
        };

    private final RestClient restClient;

    public CustomerOrderApiClient(RestClient uiRestClient) {
        this.restClient = uiRestClient;
    }

    public List<CustomerOrderResponse> findAll() {
        List<CustomerOrderResponse> orders = restClient.get()
            .uri("/api/v1/customer-orders")
            .retrieve()
            .body(CUSTOMER_ORDER_LIST);
        return orders == null ? List.of() : orders;
    }

    public CustomerOrderResponse create(CustomerOrderRequest request) {
        return restClient.post()
            .uri("/api/v1/customer-orders")
            .body(request)
            .retrieve()
            .body(CustomerOrderResponse.class);
    }

    public CustomerOrderResponse update(Long id, CustomerOrderRequest request) {
        return restClient.put()
            .uri("/api/v1/customer-orders/{id}", id)
            .body(request)
            .retrieve()
            .body(CustomerOrderResponse.class);
    }

    public void delete(Long id) {
        restClient.delete()
            .uri("/api/v1/customer-orders/{id}", id)
            .retrieve()
            .toBodilessEntity();
    }
}
