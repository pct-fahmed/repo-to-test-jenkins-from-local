package com.example.dvzdemo.commerce.orderitem.v1;

import com.example.dvzdemo.commerce.orderitem.OrderItemService;
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
@RequestMapping("/api/v1/order-items")
public class OrderItemController {

    private final OrderItemService orderItemService;

    public OrderItemController(OrderItemService orderItemService) {
        this.orderItemService = orderItemService;
    }

    @GetMapping
    public List<OrderItemResponse> findAll() {
        return orderItemService.findAll().stream()
            .map(OrderItemResponse::from)
            .toList();
    }

    @GetMapping("/{id}")
    public OrderItemResponse findById(@PathVariable Long id) {
        return OrderItemResponse.from(orderItemService.findById(id));
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public OrderItemResponse create(@Valid @RequestBody OrderItemRequest request) {
        return OrderItemResponse.from(orderItemService.create(
            request.orderId(),
            request.productId(),
            request.quantity(),
            request.unitPrice()
        ));
    }

    @PutMapping("/{id}")
    public OrderItemResponse update(@PathVariable Long id, @Valid @RequestBody OrderItemRequest request) {
        return OrderItemResponse.from(orderItemService.update(
            id,
            request.orderId(),
            request.productId(),
            request.quantity(),
            request.unitPrice()
        ));
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PathVariable Long id) {
        orderItemService.delete(id);
    }
}
