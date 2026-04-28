package com.example.dvzdemo.appUser;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.Map;

import org.junit.jupiter.api.Test;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MvcResult;

import com.example.dvzdemo.appUser.v1.AppUserRequest;
import com.example.dvzdemo.support.AbstractRestControllerIntegrationTest;

public class AppUserControllerIntegrationTest extends AbstractRestControllerIntegrationTest {

    @Test
    void userCrudLifecycle() throws Exception {
        String suffix = String.valueOf(System.nanoTime());

        AppUserRequest user = new AppUserRequest("Testuser-" + suffix, "Maxi-" + suffix, "Musteruser-" + suffix,
                "maxi" + suffix + "@musteruser.de", "Pass123#");

        MvcResult createResult = mockMvc.perform(post("/api/v1/app-users")
                .contentType(MediaType.APPLICATION_JSON)
                .content(toJson(user)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.username").value(user.username()))
                .andReturn();

        Long id = readId(createResult);

        mockMvc.perform(get("/api/v1/app-users/{id}", id))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.givenName").value(user.givenName()));

        mockMvc.perform(put("/api/v1/app-users/{id}", id)
                .contentType(MediaType.APPLICATION_JSON)
                .content(toJson(Map.of(
                        "username", "Testuser-" + suffix,
                        "givenName", "updated givenName" + suffix,
                        "surname", "updated surname" + suffix,
                        "email", "maxi" + suffix + "@musteruser.de",
                        "password", "Pass123#" + suffix))))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.givenName").value("updated givenName" + suffix))
                .andExpect(jsonPath("$.surname").value("updated surname" + suffix))
                .andExpect(jsonPath("$.email").value("maxi" + suffix + "@musteruser.de"))
                .andExpect(jsonPath("$.password").value("Pass123#" + suffix));

        mockMvc.perform(delete("/api/v1/app-users/{id}", id))
                .andExpect(status().isNoContent());

        mockMvc.perform(get("/api/v1/app-users/{id}", id))
                .andExpect(status().isNotFound());
    }

}
