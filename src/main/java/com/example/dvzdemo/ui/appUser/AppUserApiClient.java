package com.example.dvzdemo.ui.appUser;

import java.util.List;

import org.springframework.core.ParameterizedTypeReference;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClient;

import com.example.dvzdemo.appUser.v1.AppUserRequest;
import com.example.dvzdemo.appUser.v1.AppUserResponse;
import com.example.dvzdemo.ui.UiApiErrorHandler;

@Component
public class AppUserApiClient {

    private static final ParameterizedTypeReference<List<AppUserResponse>> USER_LIST = new ParameterizedTypeReference<List<AppUserResponse>>() {
    };
    // ermöglicht Rückgabe einer typisierten Liste

    private final RestClient restClient;
    private final UiApiErrorHandler errorHandler;

    public AppUserApiClient(RestClient uiRestClient, UiApiErrorHandler errorHandler) {
        this.restClient = uiRestClient;
        this.errorHandler = errorHandler;
    }

    public AppUserResponse create(AppUserRequest request) {
        return restClient.post()
                .uri("/api/v1/app-users")
                .body(request) // request wird zu JSON-Objekt & als req-body mitgeschickt
                .retrieve() // HTTP req wird an BE-Controller abgeschickt
                .body(AppUserResponse.class); // JSON res kommt vom C. zurück, Umwandlung in AppUserResponse
    }

    public AppUserResponse findById(Long id) {
        try {
            return restClient.get()
                    .uri("/api/v1/app-users/{id}", id)
                    .retrieve()
                    .body(AppUserResponse.class);
        } catch (RuntimeException e) {
            throw errorHandler.toException("User load by id failed", e);
        }
    }

    public List<AppUserResponse> findAll() {
        try {
            List<AppUserResponse> users = restClient.get()
                    .uri("/api/v1/app-users")
                    .retrieve()
                    .body(USER_LIST);
            return users == null ? List.of() : users;
        } catch (RuntimeException e) {
            throw errorHandler.toException("Users load failed", e);
        }
    }

    public AppUserResponse update(Long id, AppUserRequest request) {
        try {
            return restClient.put()
                    .uri("/api/v1/app-users/{id}", id)
                    .body(request) // Achtung als Requestbody, nicht Parameter übergeben!
                    .retrieve()
                    .body(AppUserResponse.class);
        } catch (RuntimeException e) {
            throw errorHandler.toException("Update user failed", e);
        }
    }

    public void delete(Long id) {
        try {
            restClient.delete()
                    .uri("/api/v1/app-users/{id}", id)
                    .retrieve()
                    .toBodilessEntity();
        } catch (RuntimeException e) {
            throw errorHandler.toException("Delete user failed", e);
        }
    }
    

}
