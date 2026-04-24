package com.example.dvzdemo.user;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

@Service
@Transactional
public class AppUserService {

    private final AppUserRepository appUserRepository;

    public AppUserService(AppUserRepository appUserRepository) {
        this.appUserRepository = appUserRepository;
    }

    public AppUser create(String username, String givenName, String surname, String email, String password) {
        AppUser newUser = new AppUser(username, givenName, surname, email, password);
        return appUserRepository.save(newUser);
    }

    @Transactional(readOnly = true) // keine Schreiboption, Perfomance-Optimierung
    public List<AppUser> findAll() {
        return appUserRepository.findAll();
    }

    @Transactional(readOnly = true)
    public AppUser findById(Long id) {
        return getAppUser(id);
    }

    public AppUser update(Long id, String givenName, String surname, String email, String password) {
        AppUser appUser = getAppUser(id);
        appUser.update(givenName, surname, email, password);
        return appUser;
    }

    public void delete(Long id) {
        appUserRepository.delete(getAppUser(id));
    }

    // Absicherung Existenz: wenn Id nicht existiert, wird 404 geworfen
    public AppUser getAppUser(Long id) {
        return appUserRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "AppUser not found. ID: " + id));
    }

}