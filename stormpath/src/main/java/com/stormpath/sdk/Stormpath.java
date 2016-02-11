package com.stormpath.sdk;

import com.stormpath.sdk.models.RegistrationParams;

import android.content.Context;
import android.support.annotation.NonNull;

import java.util.Map;

public class Stormpath {

    private static StormpathConfiguration config;

    private static Platform platform;

    private static ApiManager apiManager;

    private Stormpath() {
        // no instantiations
    }

    public static void init(@NonNull Context context, @NonNull StormpathConfiguration config) {
        init(new AndroidPlatform(context), config);
    }

    /**
     * Used for tests.
     */
    static void init(@NonNull Platform platform, @NonNull StormpathConfiguration configuration) {
        if (config != null && Stormpath.platform != null && apiManager != null) {
            throw new IllegalStateException("You may only initialize Stormpath once!");
        }

        Stormpath.platform = platform;
        config = configuration;
        apiManager = new ApiManager(config, platform);
    }

    /**
     * Used for tests.
     */
    static void reset() {
        Stormpath.platform = null;
        config = null;
        apiManager = null;
    }

    public static void login(String username, String password, StormpathCallback<String> callback) {
        ensureConfigured();
        apiManager.login(username, password, callback);
    }

    public static void register(RegistrationParams registrationParams, StormpathCallback<Map<String, String>> callback) {
        ensureConfigured();
        apiManager.register(registrationParams, callback);
    }

    public static void refreshAccessToken(StormpathCallback<Void> callback) {
        ensureConfigured();
        apiManager.refreshAccessToken(callback);
    }

    public static void getUserProfile(StormpathCallback<Map<String, String>> callback) {
        ensureConfigured();
        apiManager.getUserProfile(callback);
    }

    public static void resetPassword(String email, StormpathCallback<Void> callback) {
        ensureConfigured();
        apiManager.resetPassword(email, callback);
    }

    public static void logout(StormpathCallback<String> callback) {
        ensureConfigured();
        apiManager.logout(callback);
    }

    public static String getAccessToken() {
        ensureConfigured();
        return platform.preferenceStore().getAccessToken();
    }

    static void ensureConfigured() {
        if (config == null || platform == null || apiManager == null) {
            throw new IllegalStateException(
                    "You need to initialize Stormpath before using it. To do that call Stormpath.init() with a valid configuration.");
        }
    }

    static StormpathLogger logger() {
        ensureConfigured();
        return platform.logger();
    }
}
