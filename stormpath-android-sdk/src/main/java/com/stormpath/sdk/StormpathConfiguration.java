package com.stormpath.sdk;

public class StormpathConfiguration {

    private String baseUrl;

    private String oauthPath;

    private String registerPath;

    private String verifyEmailPath;

    private String passwordResetPath;

    private String logoutPath;

    private String userProfilePath;

    StormpathConfiguration(Builder builder) {
        baseUrl = normalizeUrl(builder.baseUrl);
        oauthPath = normalizePath(builder.oauthPath);
        registerPath = normalizePath(builder.registerPath);
        verifyEmailPath = normalizePath(builder.verifyEmailPath);
        passwordResetPath = normalizePath(builder.passwordResetPath);
        logoutPath = normalizePath(builder.logoutPath);
        userProfilePath = normalizePath(builder.userProfilePath);
    }

    String baseUrl() {
        return baseUrl;
    }

    String oauthPath() {
        return oauthPath;
    }

    String registerPath() {
        return registerPath;
    }

    String verifyEmailPath() {
        return verifyEmailPath;
    }
    String passwordResetPath() {
        return passwordResetPath;
    }

    String logoutPath() {
        return logoutPath;
    }

    String userProfilePath() {
        return userProfilePath;
    }

    String oauthUrl() {
        return baseUrl + oauthPath;
    }

    String registerUrl() {
        return baseUrl + registerPath;
    }

    String verifyEmailUrl() {
        return baseUrl + verifyEmailPath;
    }

    String passwordResetUrl() {
        return baseUrl + passwordResetPath;
    }

    String logoutUrl() {
        return baseUrl + logoutPath;
    }

    String userProfileUrl() {
        return baseUrl + userProfilePath;
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

        String baseUrl;

        String oauthPath = "/oauth/token";

        String registerPath = "/register";

        String verifyEmailPath = "/verify";

        String passwordResetPath = "/forgot";

        String logoutPath = "/logout";

        String userProfilePath = "/me";

        /**
         * @param baseUrl the base URL of your API, eg. "https://api.stormpath.com".
         */
        public Builder baseUrl(String baseUrl) {
            this.baseUrl = baseUrl;
            return this;
        }

        public Builder oauthPath(String oauthPath) {
            this.oauthPath = oauthPath;
            return this;
        }

        public Builder registerPath(String registerPath) {
            this.registerPath = registerPath;
            return this;
        }

        public Builder verifyEmailPath(String verifyEmailPath) {
            this.verifyEmailPath = verifyEmailPath;
            return this;
        }

        public Builder passwordResetPath(String passwordResetPath) {
            this.passwordResetPath = passwordResetPath;
            return this;
        }

        public Builder logoutPath(String logoutPath) {
            this.logoutPath = logoutPath;
            return this;
        }

        public Builder userProfilePath(String userProfilePath) {
            this.userProfilePath = userProfilePath;
            return this;
        }

        public StormpathConfiguration build() {
            // if incorrectly configured, fail fast!

            if (baseUrl == null) {
                throw new IllegalStateException("baseUrl == null");
            }
            if (oauthPath == null) {
                throw new IllegalStateException("oauthPath == null");
            }
            if (registerPath == null) {
                throw new IllegalStateException("registerPath == null");
            }
            if (verifyEmailPath == null) {
                throw new IllegalStateException("verifyEmailPath == null");
            }
            if (passwordResetPath == null) {
                throw new IllegalStateException("passwordResetPath == null");
            }
            if (logoutPath == null) {
                throw new IllegalStateException("logoutPath == null");
            }
            if (userProfilePath == null) {
                throw new IllegalStateException("userProfilePath == null");
            }

            return new StormpathConfiguration(this);
        }
    }
}
