package com.example.dvzdemo.commerce.inventory.v1;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record InventoryRequest(
    @NotBlank @Size(max = 64) String code,
    @NotBlank String name,
    @NotBlank String location
) {
}
