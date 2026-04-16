package com.example.dvzdemo.commerce.inventory.v1;

import com.example.dvzdemo.commerce.inventory.Inventory;

public record InventoryResponse(
    Long id,
    String code,
    String name,
    String location
) {
    public static InventoryResponse from(Inventory inventory) {
        return new InventoryResponse(
            inventory.getId(),
            inventory.getCode(),
            inventory.getName(),
            inventory.getLocation()
        );
    }
}
