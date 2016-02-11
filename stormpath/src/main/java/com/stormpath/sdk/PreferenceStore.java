package com.stormpath.sdk;

public interface PreferenceStore {

    void setAccessToken(String accessToken);

    String getAccessToken();

    void clearAccessToken();

    void setRefreshToken(String refreshToken);

    String getRefreshToken();

    void clearRefreshToken();
}
