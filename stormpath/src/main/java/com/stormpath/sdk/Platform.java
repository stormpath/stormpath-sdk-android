package com.stormpath.sdk;

import java.util.concurrent.Executor;

public abstract class Platform {

    public abstract Executor defaultHttpExecutor();

    public abstract Executor defaultCallbackExecutor();

    public abstract StormpathLogger logger();

    public abstract PreferenceStore preferenceStore();
}
