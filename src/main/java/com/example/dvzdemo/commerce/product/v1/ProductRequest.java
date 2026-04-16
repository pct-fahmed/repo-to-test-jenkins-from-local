package com.example.dvzdemo.commerce.product.v1;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

import java.math.BigDecimal;

public record ProductRequest(
    @NotBlank @Size(max = 64) String sku,
    @NotBlank String name,
    @Size(max = 500) String description,
    @NotNull @DecimalMin("0.0") BigDecimal price
) {
}
