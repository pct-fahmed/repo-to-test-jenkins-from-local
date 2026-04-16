package com.example.dvzdemo.commerce.product.v1;

import com.example.dvzdemo.commerce.product.Product;

import java.math.BigDecimal;

public record ProductResponse(
    Long id,
    String sku,
    String name,
    String description,
    BigDecimal price
) {
    public static ProductResponse from(Product product) {
        return new ProductResponse(
            product.getId(),
            product.getSku(),
            product.getName(),
            product.getDescription(),
            product.getPrice()
        );
    }
}
