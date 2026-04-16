package com.example.dvzdemo.commerce.inventory;

import com.example.dvzdemo.support.AbstractRestControllerIntegrationTest;
import org.junit.jupiter.api.Test;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MvcResult;

import java.util.Map;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

class InventoryControllerIntegrationTest extends AbstractRestControllerIntegrationTest {

    @Test
    void inventoryCrudLifecycle() throws Exception {
        String suffix = String.valueOf(System.nanoTime());

        MvcResult createResult = mockMvc.perform(post("/api/v1/inventories")
                .contentType(MediaType.APPLICATION_JSON)
                .content(toJson(Map.of(
                    "code", "INV-" + suffix,
                    "name", "Inventory " + suffix,
                    "location", "Test City"
                ))))
            .andExpect(status().isCreated())
            .andExpect(jsonPath("$.code").value("INV-" + suffix))
            .andReturn();

        long id = readId(createResult);

        mockMvc.perform(get("/api/v1/inventories/{id}", id))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.location").value("Test City"));

        mockMvc.perform(put("/api/v1/inventories/{id}", id)
                .contentType(MediaType.APPLICATION_JSON)
                .content(toJson(Map.of(
                    "code", "INV-" + suffix + "-UPD",
                    "name", "Updated Inventory " + suffix,
                    "location", "Updated City"
                ))))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.name").value("Updated Inventory " + suffix));

        mockMvc.perform(delete("/api/v1/inventories/{id}", id))
            .andExpect(status().isNoContent());

        mockMvc.perform(get("/api/v1/inventories/{id}", id))
            .andExpect(status().isNotFound());
    }
}
