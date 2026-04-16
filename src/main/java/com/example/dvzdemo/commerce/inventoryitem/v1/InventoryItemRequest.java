package com.example.dvzdemo.commerce.inventoryitem.v1;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;

public record InventoryItemRequest(
    @NotNull Long inventoryId,
    @NotNull Long productId,
    @NotNull @Min(0) Integer quantityOnHand,
    @NotNull @Min(0) Integer reorderLevel
) {
}
