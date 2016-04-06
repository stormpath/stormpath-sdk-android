package com.stormpath.sdk.android;

import com.stormpath.sdk.Platform;
import com.stormpath.sdk.PreferenceStore;
import com.stormpath.sdk.R;
import com.stormpath.sdk.StormpathLogger;

import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.res.Resources;
import android.os.Handler;
import android.os.Looper;
import android.support.annotation.NonNull;

import java.util.concurrent.Executor;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.SynchronousQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

import okhttp3.internal.Util;

public class AndroidPlatform extends Platform {

    private static final ExecutorService DEFAULT_HTTP_EXECUTOR_SERVICE = new ThreadPoolExecutor(0, Integer.MAX_VALUE, 60, TimeUnit.SECONDS,
            new SynchronousQueue<Runnable>(), Util.threadFactory("Stormpath Http Dispatcher", false));

    private static final Executor DEFAULT_CALLBACK_EXECUTOR = new Executor() {

        private final Handler mainHandler = new Handler(Looper.getMainLooper());

        @Override
        public void execute(@NonNull Runnable command) {
            mainHandler.post(command);
        }
    };

    private final ExecutorService httpExecutor;

    private final Executor callbackExecutor;

    private final StormpathLogger logger;

    private final PreferenceStore preferenceStore;

    private final Resources resources;

    private String version;

    public AndroidPlatform(Context context) {
        this(context, DEFAULT_HTTP_EXECUTOR_SERVICE);
    }

    public AndroidPlatform(Context context, ExecutorService httpExecutorService) {
        this.preferenceStore = new SharedPrefsStore(context.getApplicationContext());
        this.httpExecutor = httpExecutorService;
        this.callbackExecutor = DEFAULT_CALLBACK_EXECUTOR;
        this.logger = new AndroidLogger();
        this.resources = context.getApplicationContext().getResources();
    }

    @Override
    public ExecutorService httpExecutorService() {
        return httpExecutor;
    }

    @Override
    public Executor callbackExecutor() {
        return callbackExecutor;
    }

    @Override
    public StormpathLogger logger() {
        return logger;
    }

    @Override
    public PreferenceStore preferenceStore() {
        return preferenceStore;
    }

    @Override
    public String unknownErrorMessage() {
        return resources.getString(R.string.stormpath_unknown_error_message);
    }

    @Override
    public String networkErrorMessage() {
        return resources.getString(R.string.stormpath_network_error_message);
    }
}
