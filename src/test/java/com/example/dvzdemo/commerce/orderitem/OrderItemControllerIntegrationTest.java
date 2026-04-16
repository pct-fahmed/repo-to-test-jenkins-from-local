package com.example.dvzdemo.commerce.orderitem;

import com.example.dvzdemo.commerce.customerorder.OrderStatus;
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

class OrderItemControllerIntegrationTest extends AbstractRestControllerIntegrationTest {

    @Test
    void orderItemCrudLifecycle() throws Exception {
        String suffix = String.valueOf(System.nanoTime());

        MvcResult productResult = mockMvc.perform(post("/api/v1/products")
                .contentType(MediaType.APPLICATION_JSON)
                .content(toJson(Map.of(
                    "sku", "SKU-OI-" + suffix,
                    "name", "Order Item Product " + suffix,
                    "description", "Dependency product",
                    "price", new BigDecimal("33.00")
                ))))
            .andExpect(status().isCreated())
            .andReturn();
        long productId = readId(productResult);

        MvcResult orderResult = mockMvc.perform(post("/api/v1/customer-orders")
                .contentType(MediaType.APPLICATION_JSON)
                .content(toJson(Map.of(
                    "orderNumber", "ORD-OI-" + suffix,
                    "customerName", "Order Item Customer",
                    "status", OrderStatus.NEW,
                    "orderedAt", LocalDateTime.of(2026, 4, 16, 14, 0)
                ))))
            .andExpect(status().isCreated())
            .andReturn();
        long orderId = readId(orderResult);

        MvcResult createResult = mockMvc.perform(post("/api/v1/order-items")
                .contentType(MediaType.APPLICATION_JSON)
                .content(toJson(Map.of(
                    "orderId", orderId,
                    "productId", productId,
                    "quantity", 2,
                    "unitPrice", new BigDecimal("33.00")
                ))))
            .andExpect(status().isCreated())
            .andExpect(jsonPath("$.orderId").value(orderId))
            .andReturn();

        long id = readId(createResult);

        mockMvc.perform(get("/api/v1/order-items/{id}", id))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.quantity").value(2));

        mockMvc.perform(put("/api/v1/order-items/{id}", id)
                .contentType(MediaType.APPLICATION_JSON)
                .content(toJson(Map.of(
                    "orderId", orderId,
                    "productId", productId,
                    "quantity", 3,
                    "unitPrice", new BigDecimal("31.50")
                ))))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.quantity").value(3));

        mockMvc.perform(delete("/api/v1/order-items/{id}", id))
            .andExpect(status().isNoContent());

        mockMvc.perform(get("/api/v1/order-items/{id}", id))
            .andExpect(status().isNotFound());

        mockMvc.perform(delete("/api/v1/customer-orders/{id}", orderId))
            .andExpect(status().isNoContent());
        mockMvc.perform(delete("/api/v1/products/{id}", productId))
            .andExpect(status().isNoContent());
    }
}
