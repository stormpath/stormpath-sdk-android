package com.stormpath.sdk.models;

import com.squareup.moshi.Json;

import java.io.Serializable;

public class Account implements Serializable {

    @Json(name = "href")
    private String href;

    @Json(name = "email")
    private String email;

    @Json(name = "givenName")
    private String givenName;

    @Json(name = "middleName")
    private String middleName;

    @Json(name = "surname")
    private String surname;

    @Json(name = "fullName")
    private String fullName;

    @Json(name = "status")
    private String status;

    @Json(name = "username")
    private String username;

    @Json(name = "customData")
    private Object customData;

    public String getHref() {
        return href;
    }

    public String getEmail() {
        return email;
    }

    public String getGivenName() {
        return givenName;
    }

    public String getMiddleName() {
        return middleName;
    }

    public String getSurname() {
        return surname;
    }

    public String getFullName() {
        return fullName;
    }

    public String getStatus() {
        return status;
    }

    public String getUsername() {
        return username;
    }

    public Object getCustomData() {
        return customData;
    }
}
