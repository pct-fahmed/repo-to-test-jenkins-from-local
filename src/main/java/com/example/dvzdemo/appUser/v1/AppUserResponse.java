package com.example.dvzdemo.appUser.v1;

import com.example.dvzdemo.appUser.AppUser;

public record AppUserResponse(
        Long id,
        String username,
        String givenName,
        String surname,
        String email,
        String password) {

    public static AppUserResponse from(AppUser user) {
        return new AppUserResponse(
                user.getId(),
                user.getUsername(),
                user.getGivenName(),
                user.getSurname(),
                user.getEmail(),
                user.getPassword());

    }

}
