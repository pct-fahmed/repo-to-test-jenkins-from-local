package com.example.dvzdemo.appUser;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name = "app_user")
public class AppUser {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true, length = 64)
    private String username;

    @Column(name = "given_name", nullable = false)
    private String givenName;

    @Column(nullable = false)
    private String surname;

    @Column(nullable = false, unique = true)
    private String email;

    @Column(nullable = false)
    private String password;

    protected AppUser() {
    }

    public AppUser(String username, String givenName, String surname, String email, String password) {
        this.username = username;
        this.givenName = givenName;
        this.surname = surname;
        this.email = email;
        this.password = password;
    }

    public void update(String givenName, String surname, String email, String password) {
        this.givenName = givenName;
        this.surname = surname;
        this.email = email;
        this.password = password;
    }

    public Long getId() {
        return id;
    }

    public String getUsername() {
        return username;
    }

    public String getGivenName() {
        return givenName;
    }

    public String getSurname() {
        return surname;
    }

    public String getEmail() {
        return email;
    }

    public String getPassword() {
        return password;
    }

    @Override
    public String toString(){
        return String.format("AppUser with Id: %d, username: %s, email: %s", this.getId(), this.getUsername(), this.getEmail());
    }
}