package com.stormpath.sdk;

import java.util.concurrent.Executor;
import java.util.concurrent.ExecutorService;

/**
 * An abstraction of the most important platform differences between Android and Java.
 * This is used so we have an option to also support Java client apps in the future.
 */
public abstract class Platform {

    /**
     * @return the executor which should be used to run http calls on. This can't be the main thread on Android.
     */
    public abstract ExecutorService httpExecutorService();

    /**
     * @return the executor which should be used for callbacks. This must be the main thread on Android.
     */
    public abstract Executor callbackExecutor();

    public abstract StormpathLogger logger();

    public abstract PreferenceStore preferenceStore();

    public abstract String unknownErrorMessage();

    public abstract String networkErrorMessage();

}
