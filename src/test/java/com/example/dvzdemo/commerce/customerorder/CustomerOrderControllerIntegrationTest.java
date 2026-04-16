package com.example.dvzdemo.commerce.customerorder;

import com.example.dvzdemo.support.AbstractRestControllerIntegrationTest;
import org.junit.jupiter.api.Test;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MvcResult;

import java.time.LocalDateTime;
import java.util.Map;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

class CustomerOrderControllerIntegrationTest extends AbstractRestControllerIntegrationTest {

    @Test
    void customerOrderCrudLifecycle() throws Exception {
        String suffix = String.valueOf(System.nanoTime());

        MvcResult createResult = mockMvc.perform(post("/api/v1/customer-orders")
                .contentType(MediaType.APPLICATION_JSON)
                .content(toJson(Map.of(
                    "orderNumber", "ORD-" + suffix,
                    "customerName", "Customer " + suffix,
                    "status", OrderStatus.NEW,
                    "orderedAt", LocalDateTime.of(2026, 4, 16, 10, 0)
                ))))
            .andExpect(status().isCreated())
            .andExpect(jsonPath("$.orderNumber").value("ORD-" + suffix))
            .andReturn();

        long id = readId(createResult);

        mockMvc.perform(get("/api/v1/customer-orders/{id}", id))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.customerName").value("Customer " + suffix));

        mockMvc.perform(put("/api/v1/customer-orders/{id}", id)
                .contentType(MediaType.APPLICATION_JSON)
                .content(toJson(Map.of(
                    "orderNumber", "ORD-" + suffix + "-UPD",
                    "customerName", "Updated Customer " + suffix,
                    "status", OrderStatus.PROCESSING,
                    "orderedAt", LocalDateTime.of(2026, 4, 16, 11, 30)
                ))))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.status").value("PROCESSING"));

        mockMvc.perform(delete("/api/v1/customer-orders/{id}", id))
            .andExpect(status().isNoContent());

        mockMvc.perform(get("/api/v1/customer-orders/{id}", id))
            .andExpect(status().isNotFound());
    }
}
