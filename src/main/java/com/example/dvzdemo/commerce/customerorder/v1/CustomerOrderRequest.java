package com.example.dvzdemo.commerce.customerorder.v1;

import com.example.dvzdemo.commerce.customerorder.OrderStatus;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

import java.time.LocalDateTime;

public record CustomerOrderRequest(
    @NotBlank @Size(max = 64) String orderNumber,
    @NotBlank String customerName,
    @NotNull OrderStatus status,
    @NotNull LocalDateTime orderedAt
) {
}
