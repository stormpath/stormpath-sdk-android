package com.stormpath.sdk;

import android.app.Activity;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.customtabs.CustomTabsIntent;

import com.stormpath.sdk.Stormpath;
import com.stormpath.sdk.StormpathCallback;
import com.stormpath.sdk.android.CustomTabActivityHelper;
import com.stormpath.sdk.android.WebviewFallback;
import com.stormpath.sdk.models.AccountStore;
import com.stormpath.sdk.models.LoginModel;
import com.stormpath.sdk.models.StormpathError;

import okhttp3.HttpUrl;

/**
 * Created by edjiang on 1/5/17.
 */
class SocialLoginManager {
    private StormpathCallback<Void> queuedCallback;

    void loginWithProvider(@NonNull final String providerId, final Activity activity, final StormpathCallback<Void> callback) {
        Stormpath.apiManager.getLoginModel(new StormpathCallback<LoginModel>() {
            @Override
            public void onSuccess(LoginModel loginModel) {
                for (AccountStore accountStore : loginModel.getAccountStores()) {
                    if (accountStore.getProviderId().equalsIgnoreCase(providerId)) {
                        loginWithAccountStore(accountStore.getHref(), activity, callback);
                        return;
                    }
                }
            }

            @Override
            public void onFailure(StormpathError error) {
                callback.onFailure(error);
            }
        });
    }

    void loginWithAccountStore(@NonNull String href, Activity activity, StormpathCallback<Void> callback) {
        String authorizeUrl = HttpUrl.parse(Stormpath.config.getBaseUrl() + "/authorize").newBuilder()
                .addQueryParameter("response_type", "stormpath_token")
                .addQueryParameter("account_store_href", href)
                .addQueryParameter("redirect_uri", Stormpath.config.getUrlScheme() + "://stormpathCallback")
                .build()
                .toString();

        CustomTabsIntent customTabsIntent = new CustomTabsIntent.Builder().build();
        queuedCallback = callback;
        CustomTabActivityHelper.openCustomTab(activity, customTabsIntent, Uri.parse(authorizeUrl), new WebviewFallback());
    }

    void handleCallback(@NonNull String stormpathToken) {
        Stormpath.apiManager.loginWithStormpathToken(stormpathToken, new StormpathCallback<Void>() {
            @Override
            public void onSuccess(Void aVoid) {
                queuedCallback.onSuccess(aVoid);
                queuedCallback = null;
            }

            @Override
            public void onFailure(StormpathError error) {
                queuedCallback.onFailure(error);
            }
        });
    }
}
