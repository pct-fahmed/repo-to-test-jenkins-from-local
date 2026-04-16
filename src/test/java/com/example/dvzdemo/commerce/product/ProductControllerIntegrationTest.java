package com.example.dvzdemo.commerce.product;

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

class ProductControllerIntegrationTest extends AbstractRestControllerIntegrationTest {

    @Test
    void productCrudLifecycle() throws Exception {
        String suffix = String.valueOf(System.nanoTime());

        MvcResult createResult = mockMvc.perform(post("/api/v1/products")
                .contentType(MediaType.APPLICATION_JSON)
                .content(toJson(Map.of(
                    "sku", "SKU-" + suffix,
                    "name", "Product " + suffix,
                    "description", "Created by integration test",
                    "price", new BigDecimal("42.50")
                ))))
            .andExpect(status().isCreated())
            .andExpect(jsonPath("$.sku").value("SKU-" + suffix))
            .andReturn();

        long id = readId(createResult);

        mockMvc.perform(get("/api/v1/products/{id}", id))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.name").value("Product " + suffix));

        mockMvc.perform(put("/api/v1/products/{id}", id)
                .contentType(MediaType.APPLICATION_JSON)
                .content(toJson(Map.of(
                    "sku", "SKU-" + suffix + "-UPD",
                    "name", "Updated Product " + suffix,
                    "description", "Updated by integration test",
                    "price", new BigDecimal("55.00")
                ))))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.name").value("Updated Product " + suffix))
            .andExpect(jsonPath("$.price").value(55.00));

        mockMvc.perform(delete("/api/v1/products/{id}", id))
            .andExpect(status().isNoContent());

        mockMvc.perform(get("/api/v1/products/{id}", id))
            .andExpect(status().isNotFound());
    }
}
