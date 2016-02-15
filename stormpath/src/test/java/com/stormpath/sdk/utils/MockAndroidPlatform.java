package com.stormpath.sdk.utils;

import com.stormpath.sdk.PreferenceStore;
import com.stormpath.sdk.android.AndroidPlatform;

import java.util.concurrent.Executor;
import java.util.concurrent.ExecutorService;

import static org.mockito.Mockito.mock;

public class MockAndroidPlatform extends AndroidPlatform {

    PreferenceStore preferenceStore = mock(PreferenceStore.class);

    ExecutorService synchronousExecutorService = new SynchronousExecutorService();

    public MockAndroidPlatform() {
        super(Mocks.appContext());
    }

    @Override
    public PreferenceStore preferenceStore() {
        return preferenceStore;
    }

    @Override
    public ExecutorService httpExecutorService() {
        return synchronousExecutorService;
    }

    @Override
    public Executor callbackExecutor() {
        return synchronousExecutorService;
    }
}
