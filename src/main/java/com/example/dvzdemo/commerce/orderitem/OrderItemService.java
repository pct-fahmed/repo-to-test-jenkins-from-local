package com.example.dvzdemo.commerce.orderitem;

import com.example.dvzdemo.commerce.customerorder.CustomerOrder;
import com.example.dvzdemo.commerce.customerorder.CustomerOrderService;
import com.example.dvzdemo.commerce.product.Product;
import com.example.dvzdemo.commerce.product.ProductService;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.math.BigDecimal;
import java.util.List;

@Service
@Transactional
public class OrderItemService {

    private final OrderItemRepository orderItemRepository;
    private final CustomerOrderService customerOrderService;
    private final ProductService productService;

    public OrderItemService(
        OrderItemRepository orderItemRepository,
        CustomerOrderService customerOrderService,
        ProductService productService
    ) {
        this.orderItemRepository = orderItemRepository;
        this.customerOrderService = customerOrderService;
        this.productService = productService;
    }

    @Transactional(readOnly = true)
    public List<OrderItem> findAll() {
        return orderItemRepository.findAllByOrderByIdAsc();
    }

    @Transactional(readOnly = true)
    public OrderItem findById(Long id) {
        return getOrderItemWithDetails(id);
    }

    public OrderItem create(Long orderId, Long productId, Integer quantity, BigDecimal unitPrice) {
        CustomerOrder order = customerOrderService.getCustomerOrder(orderId);
        Product product = productService.getProduct(productId);
        OrderItem orderItem = new OrderItem(order, product, quantity, unitPrice);
        OrderItem saved = orderItemRepository.save(orderItem);
        return getOrderItemWithDetails(saved.getId());
    }

    public OrderItem update(Long id, Long orderId, Long productId, Integer quantity, BigDecimal unitPrice) {
        CustomerOrder order = customerOrderService.getCustomerOrder(orderId);
        Product product = productService.getProduct(productId);
        OrderItem orderItem = getOrderItem(id);
        orderItem.update(order, product, quantity, unitPrice);
        return getOrderItemWithDetails(orderItem.getId());
    }

    public void delete(Long id) {
        orderItemRepository.delete(getOrderItem(id));
    }

    public OrderItem getOrderItem(Long id) {
        return orderItemRepository.findById(id)
            .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Order item not found: " + id));
    }

    public OrderItem getOrderItemWithDetails(Long id) {
        return orderItemRepository.findWithDetailsById(id)
            .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Order item not found: " + id));
    }
}
