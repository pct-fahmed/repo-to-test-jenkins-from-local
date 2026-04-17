package com.example.dvzdemo.commerce.product;

import com.example.dvzdemo.support.AbstractRestControllerIntegrationTest;
import org.junit.jupiter.api.Test;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MvcResult;

import java.math.BigDecimal;
import java.time.LocalDateTime;
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

    @Test
    void deleteProductReferencedByOrderItemReturnsConflict() throws Exception {
        String suffix = String.valueOf(System.nanoTime());

        MvcResult productResult = mockMvc.perform(post("/api/v1/products")
                .contentType(MediaType.APPLICATION_JSON)
                .content(toJson(Map.of(
                    "sku", "SKU-REF-" + suffix,
                    "name", "Referenced Product " + suffix,
                    "description", "Product used by an order item",
                    "price", new BigDecimal("19.99")
                ))))
            .andExpect(status().isCreated())
            .andReturn();
        long productId = readId(productResult);

        MvcResult orderResult = mockMvc.perform(post("/api/v1/customer-orders")
                .contentType(MediaType.APPLICATION_JSON)
                .content(toJson(Map.of(
                    "orderNumber", "ORD-REF-" + suffix,
                    "customerName", "Referenced Product Customer",
                    "status", "NEW",
                    "orderedAt", LocalDateTime.of(2026, 4, 17, 12, 0)
                ))))
            .andExpect(status().isCreated())
            .andReturn();
        long orderId = readId(orderResult);

        MvcResult orderItemResult = mockMvc.perform(post("/api/v1/order-items")
                .contentType(MediaType.APPLICATION_JSON)
                .content(toJson(Map.of(
                    "orderId", orderId,
                    "productId", productId,
                    "quantity", 1,
                    "unitPrice", new BigDecimal("19.99")
                ))))
            .andExpect(status().isCreated())
            .andReturn();

        long orderItemId = readId(orderItemResult);

        mockMvc.perform(delete("/api/v1/products/{id}", productId))
            .andExpect(status().isConflict())
            .andExpect(jsonPath("$.detail").value(
                "Cannot delete product 'SKU-REF-" + suffix + "' because it is referenced by inventory items or order items"
            ));

        mockMvc.perform(delete("/api/v1/order-items/{id}", orderItemId))
            .andExpect(status().isNoContent());
        mockMvc.perform(delete("/api/v1/customer-orders/{id}", orderId))
            .andExpect(status().isNoContent());
        mockMvc.perform(delete("/api/v1/products/{id}", productId))
            .andExpect(status().isNoContent());
    }
}
