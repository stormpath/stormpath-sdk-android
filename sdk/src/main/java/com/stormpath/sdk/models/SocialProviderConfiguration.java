package com.stormpath.sdk.models;

/**
 * Created by ericlw on 3/29/16.
 */

// Configuration for a social provider (eg. Facebook, Google)
public class SocialProviderConfiguration {

    // URL Scheme the social provider will callback to
    public String urlScheme;

    // App ID for the social provider
    public String appId;

    //Scopes string formatted in the provider's format
    public String scopes;

    public SocialProviderConfiguration() {
    }

    public SocialProviderConfiguration(String urlScheme, String appId) {
        this.urlScheme = urlScheme;
        this.appId = appId;
    }
}
