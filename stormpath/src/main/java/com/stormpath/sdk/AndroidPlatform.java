package com.stormpath.sdk;

import android.content.Context;
import android.os.Handler;
import android.os.Looper;
import android.os.Process;
import android.support.annotation.NonNull;

import java.util.concurrent.Executor;
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadFactory;

public class AndroidPlatform extends Platform {

    private static final Executor DEFAULT_HTTP_EXECUTOR = Executors.newCachedThreadPool(new ThreadFactory() {
        @Override
        public Thread newThread(final Runnable r) {
            return new Thread(new Runnable() {
                @Override
                public void run() {
                    Thread.currentThread().setPriority(Process.THREAD_PRIORITY_BACKGROUND);
                    r.run();
                }
            }, "StormpathHttpThread");
        }
    });

    private static final Executor DEFAULT_CALLBACK_EXECUTOR = new Executor() {

        private final Handler mainHandler = new Handler(Looper.getMainLooper());

        @Override
        public void execute(@NonNull Runnable command) {
            mainHandler.post(command);
        }
    };

    private final Executor httpExecutor;

    private final Executor callbackExecutor;

    private final StormpathLogger logger;

    private final PreferenceStore preferenceStore;

    public AndroidPlatform(Context context) {
        this(context, DEFAULT_HTTP_EXECUTOR);
    }

    public AndroidPlatform(Context context, Executor httpExecutor) {
        this.preferenceStore = new SharedPrefsStore(context.getApplicationContext());
        this.httpExecutor = httpExecutor;
        this.callbackExecutor = DEFAULT_CALLBACK_EXECUTOR;
        this.logger = new AndroidLogger();
    }

    @Override
    public Executor defaultHttpExecutor() {
        return httpExecutor;
    }

    @Override
    public Executor defaultCallbackExecutor() {
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
}
