package com.example.dvzdemo.commerce.customerorder;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

import java.time.LocalDateTime;

@Entity
@Table(name = "customer_order")
public class CustomerOrder {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "order_number", nullable = false, unique = true, length = 64)
    private String orderNumber;

    @Column(name = "customer_name", nullable = false)
    private String customerName;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 32)
    private OrderStatus status;

    @Column(name = "ordered_at", nullable = false)
    private LocalDateTime orderedAt;

    protected CustomerOrder() {
    }

    public CustomerOrder(String orderNumber, String customerName, OrderStatus status, LocalDateTime orderedAt) {
        this.orderNumber = orderNumber;
        this.customerName = customerName;
        this.status = status;
        this.orderedAt = orderedAt;
    }

    public void update(String orderNumber, String customerName, OrderStatus status, LocalDateTime orderedAt) {
        this.orderNumber = orderNumber;
        this.customerName = customerName;
        this.status = status;
        this.orderedAt = orderedAt;
    }

    public Long getId() {
        return id;
    }

    public String getOrderNumber() {
        return orderNumber;
    }

    public String getCustomerName() {
        return customerName;
    }

    public OrderStatus getStatus() {
        return status;
    }

    public LocalDateTime getOrderedAt() {
        return orderedAt;
    }
}
