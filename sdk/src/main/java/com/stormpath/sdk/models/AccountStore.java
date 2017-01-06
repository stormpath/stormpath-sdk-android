package com.stormpath.sdk.models;

import com.squareup.moshi.Json;

/**
 * Created by edjiang on 1/5/17.
 */
public class AccountStore {
    private String href;

    @Json(name = "provider.providerId")
    private String providerId;

    private String authorizeUri;

    public String getHref() {
        return href;
    }

    public String getProviderId() {
        return providerId;
    }

    public String getAuthorizeUri() {
        return authorizeUri;
    }
}
