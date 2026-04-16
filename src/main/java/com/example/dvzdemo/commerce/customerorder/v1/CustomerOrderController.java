package com.example.dvzdemo.commerce.customerorder.v1;

import com.example.dvzdemo.commerce.customerorder.CustomerOrderService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/v1/customer-orders")
public class CustomerOrderController {

    private final CustomerOrderService customerOrderService;

    public CustomerOrderController(CustomerOrderService customerOrderService) {
        this.customerOrderService = customerOrderService;
    }

    @GetMapping
    public List<CustomerOrderResponse> findAll() {
        return customerOrderService.findAll().stream()
            .map(CustomerOrderResponse::from)
            .toList();
    }

    @GetMapping("/{id}")
    public CustomerOrderResponse findById(@PathVariable Long id) {
        return CustomerOrderResponse.from(customerOrderService.findById(id));
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public CustomerOrderResponse create(@Valid @RequestBody CustomerOrderRequest request) {
        return CustomerOrderResponse.from(customerOrderService.create(
            request.orderNumber(),
            request.customerName(),
            request.status(),
            request.orderedAt()
        ));
    }

    @PutMapping("/{id}")
    public CustomerOrderResponse update(@PathVariable Long id, @Valid @RequestBody CustomerOrderRequest request) {
        return CustomerOrderResponse.from(customerOrderService.update(
            id,
            request.orderNumber(),
            request.customerName(),
            request.status(),
            request.orderedAt()
        ));
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PathVariable Long id) {
        customerOrderService.delete(id);
    }
}
