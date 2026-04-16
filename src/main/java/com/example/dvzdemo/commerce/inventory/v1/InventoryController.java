package com.example.dvzdemo.commerce.inventory.v1;

import com.example.dvzdemo.commerce.inventory.InventoryService;
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
@RequestMapping("/api/v1/inventories")
public class InventoryController {

    private final InventoryService inventoryService;

    public InventoryController(InventoryService inventoryService) {
        this.inventoryService = inventoryService;
    }

    @GetMapping
    public List<InventoryResponse> findAll() {
        return inventoryService.findAll().stream()
            .map(InventoryResponse::from)
            .toList();
    }

    @GetMapping("/{id}")
    public InventoryResponse findById(@PathVariable Long id) {
        return InventoryResponse.from(inventoryService.findById(id));
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public InventoryResponse create(@Valid @RequestBody InventoryRequest request) {
        return InventoryResponse.from(inventoryService.create(
            request.code(),
            request.name(),
            request.location()
        ));
    }

    @PutMapping("/{id}")
    public InventoryResponse update(@PathVariable Long id, @Valid @RequestBody InventoryRequest request) {
        return InventoryResponse.from(inventoryService.update(
            id,
            request.code(),
            request.name(),
            request.location()
        ));
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PathVariable Long id) {
        inventoryService.delete(id);
    }
}
