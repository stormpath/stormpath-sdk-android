package com.stormpath.sdk.models;

import com.squareup.moshi.Json;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Map;

public class SocialProvidersResponse {

    public static final String FACEBOOK = "facebook";

    public static final String GOOGLE = "google";

    public static final String LINKEDIN = "linkedin";

    public static final String GITHUB = "github";

    @Json(name = "socialProviders")
    private Map<String, SocialProvider> socialProviders;

    public List<SocialProvider> getSocialProviders() {
        List<SocialProvider> providers = new ArrayList<>();
        for (Map.Entry<String, SocialProvider> providerEntry : socialProviders.entrySet()) {
            SocialProvider provider = providerEntry.getValue();
            provider.providerId = providerEntry.getKey();
            providers.add(provider);
        }
        Collections.sort(providers, new Comparator<SocialProvider>() {
            @Override
            public int compare(SocialProvider lhs, SocialProvider rhs) {
                return String.CASE_INSENSITIVE_ORDER.compare(lhs.providerId, rhs.providerId);
            }
        });
        return providers;
    }

    public static class SocialProvider {

        @Json(name = "providerId")
        private String providerId;

        @Json(name = "clientId")
        private String clientId;

        @Json(name = "enabled")
        private boolean enabled;

        public String providerId() {
            return providerId;
        }

        public String clientId() {
            return clientId;
        }

        public boolean isEnabled() {
            return enabled;
        }
    }
}
