package com.example.dvzdemo.ui.product;

import com.example.dvzdemo.commerce.product.v1.ProductRequest;
import com.example.dvzdemo.commerce.product.v1.ProductResponse;
import com.example.dvzdemo.ui.UiApiErrorHandler;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClient;

import java.util.List;

@Component
public class ProductApiClient {

    private static final ParameterizedTypeReference<List<ProductResponse>> PRODUCT_LIST =
        new ParameterizedTypeReference<>() {
        };

    private final RestClient restClient;
    private final UiApiErrorHandler errorHandler;

    public ProductApiClient(RestClient uiRestClient, UiApiErrorHandler errorHandler) {
        this.restClient = uiRestClient;
        this.errorHandler = errorHandler;
    }

    public List<ProductResponse> findAll() {
        try {
            List<ProductResponse> products = restClient.get()
                .uri("/api/v1/products")
                .retrieve()
                .body(PRODUCT_LIST);
            return products == null ? List.of() : products;
        } catch (RuntimeException exception) {
            throw errorHandler.toException("Product load failed", exception);
        }
    }

    public ProductResponse create(ProductRequest request) {
        try {
            return restClient.post()
                .uri("/api/v1/products")
                .body(request)
                .retrieve()
                .body(ProductResponse.class);
        } catch (RuntimeException exception) {
            throw errorHandler.toException("Product create failed", exception);
        }
    }

    public ProductResponse update(Long id, ProductRequest request) {
        try {
            return restClient.put()
                .uri("/api/v1/products/{id}", id)
                .body(request)
                .retrieve()
                .body(ProductResponse.class);
        } catch (RuntimeException exception) {
            throw errorHandler.toException("Product update failed", exception);
        }
    }

    public void delete(Long id) {
        try {
            restClient.delete()
                .uri("/api/v1/products/{id}", id)
                .retrieve()
                .toBodilessEntity();
        } catch (RuntimeException exception) {
            throw errorHandler.toException("Product delete failed", exception);
        }
    }
}
