package com.example.dvzdemo.user.v1;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import com.example.dvzdemo.user.AppUserService;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/v1/app-users")
public class AppUserController {

    private final AppUserService appUserService;

    public AppUserController(AppUserService appUserService) {
        this.appUserService = appUserService;
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public AppUserResponse create(@Valid @RequestBody AppUserRequest request) {
        return AppUserResponse.from(appUserService.create(
                request.username(),
                request.givenName(),
                request.surname(),
                request.email(),
                request.password()));

        // AppUserRequest Objekt aus FE
        // repository save (Requestobjekt auslesen) --> gibt AppUser zurück
        // AppUserResponse erzeugen mithilfe der builder-Methode, wird von Controller
        // zurückgegeben
    }

    @GetMapping("/{id}")
    public AppUserResponse findById(@PathVariable Long id) {
        return AppUserResponse.from(appUserService.findById(id));
    }

    @GetMapping
    public List<AppUserResponse> findAll() {
        return appUserService.findAll().stream()
                .map(AppUserResponse::from)
                .toList();
    }

    @PutMapping("/{id}")
    public AppUserResponse update(@PathVariable Long id, @Valid @RequestBody AppUserRequest request) {
        return AppUserResponse.from(appUserService.update(
                id,
                request.givenName(),
                request.surname(),
                request.email(),
                request.password()));
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PathVariable Long id) {
        appUserService.delete(id);
    }

}