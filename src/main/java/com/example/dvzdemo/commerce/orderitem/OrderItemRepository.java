package com.example.dvzdemo.commerce.orderitem;

import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface OrderItemRepository extends JpaRepository<OrderItem, Long> {

    boolean existsByProductId(Long productId);

    @EntityGraph(attributePaths = {"order", "product"})
    List<OrderItem> findAllByOrderByIdAsc();

    @EntityGraph(attributePaths = {"order", "product"})
    Optional<OrderItem> findWithDetailsById(Long id);
}
