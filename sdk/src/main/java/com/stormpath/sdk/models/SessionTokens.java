package com.stormpath.sdk.models;

import com.squareup.moshi.Json;

import java.io.Serializable;

public class SessionTokens implements Serializable {

    @Json(name = "access_token")
    private String accessToken;

    @Json(name = "refresh_token")
    private String refreshToken;

    public String getAccessToken() {
        return accessToken;
    }

    public String getRefreshToken() {
        return refreshToken;
    }
}
