package com.example.dvzdemo.commerce.inventoryitem;

import com.example.dvzdemo.support.AbstractRestControllerIntegrationTest;
import org.junit.jupiter.api.Test;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MvcResult;

import java.math.BigDecimal;
import java.util.Map;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

class InventoryItemControllerIntegrationTest extends AbstractRestControllerIntegrationTest {

    @Test
    void inventoryItemCrudLifecycle() throws Exception {
        String suffix = String.valueOf(System.nanoTime());

        MvcResult inventoryResult = mockMvc.perform(post("/api/v1/inventories")
                .contentType(MediaType.APPLICATION_JSON)
                .content(toJson(Map.of(
                    "code", "INV-II-" + suffix,
                    "name", "Inventory Item Inventory " + suffix,
                    "location", "Test Location"
                ))))
            .andExpect(status().isCreated())
            .andReturn();

        long inventoryId = readId(inventoryResult);

        MvcResult productResult = mockMvc.perform(post("/api/v1/products")
                .contentType(MediaType.APPLICATION_JSON)
                .content(toJson(Map.of(
                    "sku", "SKU-II-" + suffix,
                    "name", "Inventory Item Product " + suffix,
                    "description", "Dependency product",
                    "price", new BigDecimal("10.00")
                ))))
            .andExpect(status().isCreated())
            .andReturn();

        long productId = readId(productResult);

        MvcResult createResult = mockMvc.perform(post("/api/v1/inventory-items")
                .contentType(MediaType.APPLICATION_JSON)
                .content(toJson(Map.of(
                    "inventoryId", inventoryId,
                    "productId", productId,
                    "quantityOnHand", 20,
                    "reorderLevel", 5
                ))))
            .andExpect(status().isCreated())
            .andExpect(jsonPath("$.productId").value(productId))
            .andReturn();

        long id = readId(createResult);

        mockMvc.perform(get("/api/v1/inventory-items/{id}", id))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.quantityOnHand").value(20));

        mockMvc.perform(put("/api/v1/inventory-items/{id}", id)
                .contentType(MediaType.APPLICATION_JSON)
                .content(toJson(Map.of(
                    "inventoryId", inventoryId,
                    "productId", productId,
                    "quantityOnHand", 15,
                    "reorderLevel", 4
                ))))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.reorderLevel").value(4));

        mockMvc.perform(delete("/api/v1/inventory-items/{id}", id))
            .andExpect(status().isNoContent());

        mockMvc.perform(get("/api/v1/inventory-items/{id}", id))
            .andExpect(status().isNotFound());

        mockMvc.perform(delete("/api/v1/products/{id}", productId))
            .andExpect(status().isNoContent());
        mockMvc.perform(delete("/api/v1/inventories/{id}", inventoryId))
            .andExpect(status().isNoContent());
    }
}
