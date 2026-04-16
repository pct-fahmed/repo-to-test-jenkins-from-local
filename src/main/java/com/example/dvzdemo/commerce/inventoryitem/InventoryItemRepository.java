package com.example.dvzdemo.commerce.inventoryitem;

import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface InventoryItemRepository extends JpaRepository<InventoryItem, Long> {

    @EntityGraph(attributePaths = {"inventory", "product"})
    List<InventoryItem> findAllByOrderByIdAsc();

    @EntityGraph(attributePaths = {"inventory", "product"})
    Optional<InventoryItem> findWithDetailsById(Long id);
}
