package com.example.dvzdemo.commerce.inventory;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@Service
@Transactional
public class InventoryService {

    private final InventoryRepository inventoryRepository;

    public InventoryService(InventoryRepository inventoryRepository) {
        this.inventoryRepository = inventoryRepository;
    }

    @Transactional(readOnly = true)
    public List<Inventory> findAll() {
        return inventoryRepository.findAll();
    }

    @Transactional(readOnly = true)
    public Inventory findById(Long id) {
        return getInventory(id);
    }

    public Inventory create(String code, String name, String location) {
        Inventory inventory = new Inventory(code, name, location);
        return inventoryRepository.save(inventory);
    }

    public Inventory update(Long id, String code, String name, String location) {
        Inventory inventory = getInventory(id);
        inventory.update(code, name, location);
        return inventory;
    }

    public void delete(Long id) {
        inventoryRepository.delete(getInventory(id));
    }

    public Inventory getInventory(Long id) {
        return inventoryRepository.findById(id)
            .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Inventory not found: " + id));
    }
}
