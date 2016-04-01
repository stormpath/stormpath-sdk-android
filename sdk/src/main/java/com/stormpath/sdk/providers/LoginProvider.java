package com.stormpath.sdk.providers;

import com.stormpath.sdk.StormpathCallback;
import com.stormpath.sdk.models.SocialProviderConfiguration;

/**
 * Created by ericlw on 3/29/16.
 */
public interface LoginProvider {

    void getResponseFromCallbackURL(String url, StormpathCallback callback);

    String authenticationRequestURL(SocialProviderConfiguration application);

}
