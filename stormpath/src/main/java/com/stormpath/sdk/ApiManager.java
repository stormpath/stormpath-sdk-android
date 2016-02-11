package com.stormpath.sdk;

import com.stormpath.sdk.models.RegistrationParams;

import java.util.Map;
import java.util.concurrent.Executor;

import okhttp3.Dispatcher;
import okhttp3.OkHttpClient;

public class ApiManager {

    private OkHttpClient okHttpClient;

    private Executor callbackExecutor;

    private StormpathConfiguration config;

    ApiManager(StormpathConfiguration config, Platform platform) {
        this.config = config;
        this.callbackExecutor = platform.callbackExecutor();
        this.okHttpClient = new OkHttpClient.Builder()
                .dispatcher(new Dispatcher(platform.httpExecutorService()))
                .build();
        // TODO add logging interceptor and log via Stormpath.logger()
    }

    void login(String username, String password, StormpathCallback<String> callback) {
        // TODO
    }

    public void register(RegistrationParams registrationParams, StormpathCallback<Map<String, String>> callback) {
        // TODO
    }

    public void refreshAccessToken(StormpathCallback<Void> callback) {
        // TODO
    }

    public void getUserProfile(StormpathCallback<Map<String, String>> callback) {
        // TODO
    }

    public void resetPassword(String email, StormpathCallback<Void> callback) {
        // TODO
    }

    public void logout(StormpathCallback<String> callback) {
        // TODO
    }
}
