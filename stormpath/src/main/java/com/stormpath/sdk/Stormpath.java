package com.stormpath.sdk;

import com.stormpath.sdk.models.RegistrationParams;

import android.content.Context;
import android.support.annotation.NonNull;

import java.util.Map;
import java.util.concurrent.ExecutorService;

public class Stormpath {

    private static StormpathConfiguration config;

    private static Platform platform;

    private static ApiManager apiManager;

    private Stormpath() {
        // no instantiations
    }

    public static void init(@NonNull Context context, @NonNull StormpathConfiguration config) {
        init(context, config, null);
    }

    /**
     * Used mainly for tests which need the networking calls to be synchronous. We could also expose this for users.
     */
    static void init(@NonNull Context context, @NonNull StormpathConfiguration configuration, ExecutorService httpExecutorService) {
        if (config != null && platform != null && apiManager != null) {
            throw new IllegalStateException("You may only initialize Stormpath once!");
        }

        if (httpExecutorService != null) {
            platform = new AndroidPlatform(context, httpExecutorService);
        } else {
            platform = new AndroidPlatform(context);
        }
        config = configuration;
        apiManager = new ApiManager(config, platform);
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
