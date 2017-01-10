package com.stormpath.sdk.models;

import com.squareup.moshi.Json;

import java.io.Serializable;
import java.util.Map;

public class RegistrationForm implements Serializable {
    @Json(name = "givenName")
    private String givenName;

    @Json(name = "surname")
    private String surname;

    @Json(name = "email")
    private String email;

    @Json(name = "password")
    private String password;

    @Json(name = "username")
    private String username;

    @Json(name = "customData")
    private Map<String, String> customData;

    public RegistrationForm(String email, String password) {
        this.email = email;
        this.password = password;
    }

    public String getGivenName() {
        return givenName;
    }

    public RegistrationForm setGivenName(String givenName) {
        this.givenName = givenName;
        return this;
    }

    public String getSurname() {
        return surname;
    }

    public RegistrationForm setSurname(String surname) {
        this.surname = surname;
        return this;
    }

    public String getEmail() {
        return email;
    }

    public RegistrationForm setEmail(String email) {
        this.email = email;
        return this;
    }

    public String getPassword() {
        return password;
    }

    public RegistrationForm setPassword(String password) {
        this.password = password;
        return this;
    }

    public String getUsername() {
        return username;
    }

    public RegistrationForm setUsername(String username) {
        this.username = username;
        return this;
    }

    public Map<String, String> getCustomData() {
        return customData;
    }

    public RegistrationForm setCustomData(Map<String, String> customData) {
        this.customData = customData;
        return this;
    }
}
