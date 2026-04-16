package com.example.dvzdemo.commerce.customerorder.v1;

import com.example.dvzdemo.commerce.customerorder.CustomerOrder;
import com.example.dvzdemo.commerce.customerorder.OrderStatus;

import java.time.LocalDateTime;

public record CustomerOrderResponse(
    Long id,
    String orderNumber,
    String customerName,
    OrderStatus status,
    LocalDateTime orderedAt
) {
    public static CustomerOrderResponse from(CustomerOrder order) {
        return new CustomerOrderResponse(
            order.getId(),
            order.getOrderNumber(),
            order.getCustomerName(),
            order.getStatus(),
            order.getOrderedAt()
        );
    }
}
