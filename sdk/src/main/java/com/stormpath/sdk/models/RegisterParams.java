package com.stormpath.sdk.models;

import com.squareup.moshi.Json;

import java.io.Serializable;

public class RegisterParams implements Serializable {

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

    public RegisterParams(String givenName, String surname, String email, String password) {
        this(givenName, surname, email, password, null);
    }

    public RegisterParams(String givenName, String surname, String email, String password, String username) {
        this.givenName = givenName;
        this.surname = surname;
        this.email = email;
        this.password = password;
        this.username = username;
    }
}
