package com.stormpath.sdk;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.SharedPreferences;

public class SharedPrefsStore implements PreferenceStore {

    public static final String KEY_ACCESS_TOKEN = "stormpath-access-token";

    public static final String KEY_REFRESH_TOKEN = "stormpath-refresh-token";

    private final SharedPreferences sharedPreferences;

    public SharedPrefsStore(Context appContext) {
        sharedPreferences = appContext.getSharedPreferences("stormpath-shared-prefs", Context.MODE_PRIVATE);
    }

    @SuppressLint("CommitPrefEdits")
    private void saveStringPreference(String key, String value) {
        sharedPreferences.edit().putString(key, value).commit();
    }

    @SuppressLint("CommitPrefEdits")
    private void removeStringPreference(String key) {
        sharedPreferences.edit().remove(key).commit();
    }

    @Override
    public void setAccessToken(String accessToken) {
        saveStringPreference(KEY_ACCESS_TOKEN, accessToken);
    }

    @Override
    public String getAccessToken() {
        return sharedPreferences.getString(KEY_ACCESS_TOKEN, null);
    }

    @Override
    public void clearAccessToken() {
        removeStringPreference(KEY_ACCESS_TOKEN);
    }

    @Override
    public void setRefreshToken(String refreshToken) {
        saveStringPreference(KEY_REFRESH_TOKEN, refreshToken);
    }

    @Override
    public String getRefreshToken() {
        return sharedPreferences.getString(KEY_REFRESH_TOKEN, null);
    }

    @Override
    public void clearRefreshToken() {
        removeStringPreference(KEY_REFRESH_TOKEN);
    }
}
