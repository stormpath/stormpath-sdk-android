package com.stormpath.sdk.models;

import com.squareup.moshi.Json;

import java.util.Map;

/**
 * Created by edjiang on 1/5/17.
 */
public class AccountStore {
    private String href;

    private Map<String, String> provider;

    private String authorizeUri;

    public String getHref() {
        return href;
    }

    public String getProviderId() {
        return provider.get("providerId");
    }

    public String getAuthorizeUri() {
        return authorizeUri;
    }
}
