package com.stormpath.sdk;

import com.squareup.moshi.Moshi;
import com.stormpath.sdk.models.LoginResponse;
import com.stormpath.sdk.models.RegistrationParams;
import com.stormpath.sdk.utils.StringUtils;

import java.io.IOException;
import java.util.Map;
import java.util.concurrent.Executor;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Dispatcher;
import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;
import okhttp3.logging.HttpLoggingInterceptor;

public class ApiManager {

    private OkHttpClient okHttpClient;

    private Executor callbackExecutor;

    private StormpathConfiguration config;

    private PreferenceStore preferenceStore;

    private Moshi moshi = new Moshi.Builder().build();

    ApiManager(StormpathConfiguration config, Platform platform) {
        this.config = config;
        this.preferenceStore = platform.preferenceStore();
        this.callbackExecutor = platform.callbackExecutor();
        HttpLoggingInterceptor httpLoggingInterceptor = new HttpLoggingInterceptor(new HttpLoggingInterceptor.Logger() {
            @Override
            public void log(String message) {
                Stormpath.logger().d(message);
            }
        });
        this.okHttpClient = new OkHttpClient.Builder()
                .dispatcher(new Dispatcher(platform.httpExecutorService()))
                .addNetworkInterceptor(httpLoggingInterceptor)
                .build();
    }

    void login(String username, String password, StormpathCallback<String> callback) {
        RequestBody formBody = new FormBody.Builder()
                .add("username", username)
                .add("password", password)
                .add("grant_type", "password")
                .build();

        Request request = new Request.Builder()
                .url(config.oauthUrl())
                .post(formBody)
                .build();

        okHttpClient.newCall(request).enqueue(new OkHttpCallback<String>(callback) {
            @Override
            protected void onSuccess(Response response, StormpathCallback<String> callback) {
                try {
                    LoginResponse loginResponse = moshi.adapter(LoginResponse.class).fromJson(response.body().source());
                    if (StringUtils.isBlank(loginResponse.getAccessToken())) {
                        failureCallback(new RuntimeException("access_token was not found in response. See debug logs for details."));
                        return;
                    }

                    if (StringUtils.isBlank(loginResponse.getRefreshToken())) {
                        Stormpath.logger().e("There was no refresh_token in the login response!");
                    }

                    preferenceStore.setAccessToken(loginResponse.getAccessToken());
                    preferenceStore.setRefreshToken(loginResponse.getRefreshToken());
                    successCallback(loginResponse.getAccessToken());

                } catch (Throwable t) {
                    failureCallback(t);
                }
            }
        });
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

    private abstract class OkHttpCallback<T> implements Callback {

        private StormpathCallback<T> stormpathCallback;

        public OkHttpCallback(StormpathCallback<T> stormpathCallback) {
            this.stormpathCallback = stormpathCallback;
        }

        @Override
        public void onFailure(Call call, IOException e) {
            failureCallback(e);
        }

        @Override
        public void onResponse(Call call, Response response) throws IOException {
            if (response.isSuccessful()) {
                onSuccess(response, stormpathCallback);
            } else {
                failureCallback(new RuntimeException(
                        String.format("Call to %s failed with http status: %s %s. See debug logs for details.", response.request().url(),
                                response.code(), response.message())));
            }
        }

        protected abstract void onSuccess(Response response, StormpathCallback<T> callback);

        void successCallback(final T t) {
            callbackExecutor.execute(new Runnable() {
                @Override
                public void run() {
                    stormpathCallback.onSuccess(t);
                }
            });
        }

        void failureCallback(final Throwable t) {
            callbackExecutor.execute(new Runnable() {
                @Override
                public void run() {
                    stormpathCallback.onFailure(t);
                }
            });
        }
    }
}
