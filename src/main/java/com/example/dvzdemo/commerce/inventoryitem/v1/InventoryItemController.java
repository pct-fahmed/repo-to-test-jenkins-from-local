package com.example.dvzdemo.commerce.inventoryitem.v1;

import com.example.dvzdemo.commerce.inventoryitem.InventoryItemService;
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
@RequestMapping("/api/v1/inventory-items")
public class InventoryItemController {

    private final InventoryItemService inventoryItemService;

    public InventoryItemController(InventoryItemService inventoryItemService) {
        this.inventoryItemService = inventoryItemService;
    }

    @GetMapping
    public List<InventoryItemResponse> findAll() {
        return inventoryItemService.findAll().stream()
            .map(InventoryItemResponse::from)
            .toList();
    }

    @GetMapping("/{id}")
    public InventoryItemResponse findById(@PathVariable Long id) {
        return InventoryItemResponse.from(inventoryItemService.findById(id));
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public InventoryItemResponse create(@Valid @RequestBody InventoryItemRequest request) {
        return InventoryItemResponse.from(inventoryItemService.create(
            request.inventoryId(),
            request.productId(),
            request.quantityOnHand(),
            request.reorderLevel()
        ));
    }

    @PutMapping("/{id}")
    public InventoryItemResponse update(@PathVariable Long id, @Valid @RequestBody InventoryItemRequest request) {
        return InventoryItemResponse.from(inventoryItemService.update(
            id,
            request.inventoryId(),
            request.productId(),
            request.quantityOnHand(),
            request.reorderLevel()
        ));
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PathVariable Long id) {
        inventoryItemService.delete(id);
    }
}
