package com.example.dvzdemo.commerce.inventoryitem.v1;

import com.example.dvzdemo.commerce.inventoryitem.InventoryItem;

public record InventoryItemResponse(
    Long id,
    Long inventoryId,
    String inventoryCode,
    Long productId,
    String productSku,
    Integer quantityOnHand,
    Integer reorderLevel
) {
    public static InventoryItemResponse from(InventoryItem inventoryItem) {
        return new InventoryItemResponse(
            inventoryItem.getId(),
            inventoryItem.getInventory().getId(),
            inventoryItem.getInventory().getCode(),
            inventoryItem.getProduct().getId(),
            inventoryItem.getProduct().getSku(),
            inventoryItem.getQuantityOnHand(),
            inventoryItem.getReorderLevel()
        );
    }
}
