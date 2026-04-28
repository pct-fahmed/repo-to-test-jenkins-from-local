package com.example.dvzdemo.appUser.v1;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record AppUserRequest(
    @NotBlank @Size(max = 64) String username,        
    @NotBlank String givenName, 
    @NotBlank String surname, 
    @NotBlank @Email String email, 
    @NotBlank @Size(min = 8) String password
){}


