package com.stormpath.sdk;

public interface PreferenceStore {

    void setAccessToken(String accessToken);

    void clearAccessToken();

    void setRefreshToken(String refreshToken);

    void clearRefreshToken();
}
