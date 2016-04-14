package com.stormpath.sdk.models;

/**
 * Created by ericlw on 4/14/16.
 */
public class SocialProviderConfigurationSingleton extends SocialProviderConfiguration {

    public SocialProviderConfigurationSingleton(String urlScheme, String appId) {
        super(urlScheme, appId);
    }

    private static SocialProviderConfigurationSingleton instance = null;

    protected SocialProviderConfigurationSingleton() {
        // Exists only to defeat instantiation.
    }

    public static SocialProviderConfigurationSingleton getInstance() {
        if(instance == null) {
            instance = new SocialProviderConfigurationSingleton();
        }
        return instance;
    }
}
