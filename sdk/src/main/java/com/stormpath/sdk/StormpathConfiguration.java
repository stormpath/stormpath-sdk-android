package com.stormpath.sdk;

import java.net.URL;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import okhttp3.HttpUrl;

public class StormpathConfiguration {

    public final static String VERSION = "1.0.5"; //has to be manually set (or via script) because it can't pull version from manifest, BuildConfig is not accessible, and getVersionFromGit() returns the parent app's version

    private final String apiUrl;

    StormpathConfiguration(Builder builder) {
        apiUrl = normalizeUrl(builder.apiUrl);
    }

    String getBaseUrl() {
        return apiUrl;
    }

    String getUrlScheme() {
        List<String> urlSchemeComponents = Arrays.asList(HttpUrl.parse(apiUrl).host().split("\\."));
        Collections.reverse(urlSchemeComponents);

        String result = "";
        for(String component : urlSchemeComponents) {
            result += component;
        }

        return result;
    }

    private static String normalizePath(String path) {
        if (!path.startsWith("/")) {
            path = "/" + path;
        }
        return path;
    }

    private static String normalizeUrl(String url) {
        if (url.endsWith("/")) {
            url = url.substring(0, url.length() - 1);
        }
        return url;
    }

    public static class Builder {

        String apiUrl;

        /**
         * @param the URL of your API, eg. "https://stormpath-notes.apps.stormpath.io/".
         */
        public Builder baseUrl(String baseUrl) {
            this.apiUrl = baseUrl;
            return this;
        }

        public StormpathConfiguration build() {
            // if incorrectly configured, fail fast!

            if (apiUrl == null) {
                throw new IllegalStateException("baseUrl == null");
            }

            return new StormpathConfiguration(this);
        }
    }
}
