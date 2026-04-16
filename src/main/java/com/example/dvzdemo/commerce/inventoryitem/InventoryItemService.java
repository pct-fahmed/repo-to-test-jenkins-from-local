package com.example.dvzdemo.commerce.inventoryitem;

import com.example.dvzdemo.commerce.inventory.Inventory;
import com.example.dvzdemo.commerce.inventory.InventoryService;
import com.example.dvzdemo.commerce.product.Product;
import com.example.dvzdemo.commerce.product.ProductService;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@Service
@Transactional
public class InventoryItemService {

    private final InventoryItemRepository inventoryItemRepository;
    private final InventoryService inventoryService;
    private final ProductService productService;

    public InventoryItemService(
        InventoryItemRepository inventoryItemRepository,
        InventoryService inventoryService,
        ProductService productService
    ) {
        this.inventoryItemRepository = inventoryItemRepository;
        this.inventoryService = inventoryService;
        this.productService = productService;
    }

    @Transactional(readOnly = true)
    public List<InventoryItem> findAll() {
        return inventoryItemRepository.findAllByOrderByIdAsc();
    }

    @Transactional(readOnly = true)
    public InventoryItem findById(Long id) {
        return getInventoryItemWithDetails(id);
    }

    public InventoryItem create(Long inventoryId, Long productId, Integer quantityOnHand, Integer reorderLevel) {
        Inventory inventory = inventoryService.getInventory(inventoryId);
        Product product = productService.getProduct(productId);
        InventoryItem inventoryItem = new InventoryItem(
            inventory,
            product,
            quantityOnHand,
            reorderLevel
        );
        InventoryItem saved = inventoryItemRepository.save(inventoryItem);
        return getInventoryItemWithDetails(saved.getId());
    }

    public InventoryItem update(Long id, Long inventoryId, Long productId, Integer quantityOnHand, Integer reorderLevel) {
        Inventory inventory = inventoryService.getInventory(inventoryId);
        Product product = productService.getProduct(productId);
        InventoryItem inventoryItem = getInventoryItem(id);
        inventoryItem.update(inventory, product, quantityOnHand, reorderLevel);
        return getInventoryItemWithDetails(inventoryItem.getId());
    }

    public void delete(Long id) {
        inventoryItemRepository.delete(getInventoryItem(id));
    }

    public InventoryItem getInventoryItem(Long id) {
        return inventoryItemRepository.findById(id)
            .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Inventory item not found: " + id));
    }

    public InventoryItem getInventoryItemWithDetails(Long id) {
        return inventoryItemRepository.findWithDetailsById(id)
            .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Inventory item not found: " + id));
    }
}
