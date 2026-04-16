package com.example.dvzdemo.commerce.customerorder;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.time.LocalDateTime;
import java.util.List;

@Service
@Transactional
public class CustomerOrderService {

    private final CustomerOrderRepository customerOrderRepository;

    public CustomerOrderService(CustomerOrderRepository customerOrderRepository) {
        this.customerOrderRepository = customerOrderRepository;
    }

    @Transactional(readOnly = true)
    public List<CustomerOrder> findAll() {
        return customerOrderRepository.findAll();
    }

    @Transactional(readOnly = true)
    public CustomerOrder findById(Long id) {
        return getCustomerOrder(id);
    }

    public CustomerOrder create(String orderNumber, String customerName, OrderStatus status, LocalDateTime orderedAt) {
        CustomerOrder order = new CustomerOrder(
            orderNumber,
            customerName,
            status,
            orderedAt
        );
        return customerOrderRepository.save(order);
    }

    public CustomerOrder update(Long id, String orderNumber, String customerName, OrderStatus status, LocalDateTime orderedAt) {
        CustomerOrder order = getCustomerOrder(id);
        order.update(orderNumber, customerName, status, orderedAt);
        return order;
    }

    public void delete(Long id) {
        customerOrderRepository.delete(getCustomerOrder(id));
    }

    public CustomerOrder getCustomerOrder(Long id) {
        return customerOrderRepository.findById(id)
            .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Customer order not found: " + id));
    }
}
