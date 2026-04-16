package com.example.dvzdemo.commerce.orderitem.v1;

import com.example.dvzdemo.commerce.orderitem.OrderItem;

import java.math.BigDecimal;

public record OrderItemResponse(
    Long id,
    Long orderId,
    String orderNumber,
    Long productId,
    String productSku,
    Integer quantity,
    BigDecimal unitPrice
) {
    public static OrderItemResponse from(OrderItem orderItem) {
        return new OrderItemResponse(
            orderItem.getId(),
            orderItem.getOrder().getId(),
            orderItem.getOrder().getOrderNumber(),
            orderItem.getProduct().getId(),
            orderItem.getProduct().getSku(),
            orderItem.getQuantity(),
            orderItem.getUnitPrice()
        );
    }
}
