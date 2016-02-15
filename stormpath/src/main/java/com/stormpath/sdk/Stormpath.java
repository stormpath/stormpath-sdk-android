package com.stormpath.sdk;

import com.stormpath.sdk.android.AndroidPlatform;
import com.stormpath.sdk.models.RegisterParams;
import com.stormpath.sdk.models.UserProfile;

import android.content.Context;
import android.support.annotation.NonNull;

public class Stormpath {

    private static StormpathConfiguration config;

    private static Platform platform;

    private static ApiManager apiManager;

    @StormpathLogger.LogLevel
    private static int logLevel = StormpathLogger.SILENT;

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
        Stormpath.platform.logger().setLogLevel(logLevel);
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

    public static void login(String username, String password, StormpathCallback<Void> callback) {
        ensureConfigured();
        apiManager.login(username, password, callback);
    }

    public static void register(RegisterParams registerParams, StormpathCallback<Void> callback) {
        ensureConfigured();
        apiManager.register(registerParams, callback);
    }

    public static void refreshAccessToken(StormpathCallback<Void> callback) {
        ensureConfigured();
        apiManager.refreshAccessToken(callback);
    }

    public static void getUserProfile(StormpathCallback<UserProfile> callback) {
        ensureConfigured();
        apiManager.getUserProfile(callback);
    }

    public static void resetPassword(String email, StormpathCallback<Void> callback) {
        ensureConfigured();
        apiManager.resetPassword(email, callback);
    }

    public static void logout(StormpathCallback<Void> callback) {
        ensureConfigured();
        apiManager.logout(callback);
    }

    public static String accessToken() {
        ensureConfigured();
        return platform.preferenceStore().getAccessToken();
    }

    public static void setLogLevel(@StormpathLogger.LogLevel int logLevel) {
        Stormpath.logLevel = logLevel;
        if (platform != null) {
            platform.logger().setLogLevel(Stormpath.logLevel);
        }
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
