package com.stormpath.sdk;

import android.content.Context;
import android.support.annotation.NonNull;

import java.util.concurrent.Executor;

public class Stormpath {

    private static StormpathConfiguration config;

    private static Platform platform;

    private Stormpath() {
        // no instantiations
    }

    public static void init(@NonNull Context context, @NonNull StormpathConfiguration config) {
        if (Stormpath.config != null && Stormpath.platform != null) {
            throw new IllegalStateException("You may only initialize Stormpath once!");
        }

        Stormpath.platform = new AndroidPlatform(context);
        Stormpath.config = config;
    }

    /**
     * Used mainly for tests which need the networking calls to be synchronous. We could also expose this for users.
     */
    static void init(@NonNull Context context, @NonNull StormpathConfiguration config, Executor httpExecutor) {
        platform = new AndroidPlatform(context, httpExecutor);
        Stormpath.config = config;
    }

    public static void login(String username, String password, StormpathCallback<Void> callback) {
        ensureConfigured();
        // TODO
    }

    static void ensureConfigured() {
        if (config == null || platform == null) {
            throw new IllegalStateException(
                    "You need to initialize Stormpath before using it. To do that call Stormpath.init() with a valid configuration.");
        }
    }

    static StormpathLogger logger() {
        ensureConfigured();
        return platform.logger();
    }
}
