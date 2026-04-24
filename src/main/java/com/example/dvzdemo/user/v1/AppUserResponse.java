package com.example.dvzdemo.user.v1;

import com.example.dvzdemo.user.AppUser;

public record AppUserResponse(
        Long id,
        String username,
        String givenName,
        String surname,
        String email) {

    public static AppUserResponse from(AppUser user) {
        return new AppUserResponse(
                user.getId(),
                user.getUsername(),
                user.getGivenName(),
                user.getSurname(),
                user.getEmail());

    }

}
