package com.stormpath.sdk;

public class StormpathConfiguration {

    public final static String VERSION = "1.0.5"; //has to be manually set (or via script) because it can't pull version from manifest, BuildConfig is not accessible, and getVersionFromGit() returns the parent app's version

    private final String loginPath;

    private final String baseUrl;

    private final String oauthPath;

    private final String registerPath;

    private final String verifyEmailPath;

    private final String passwordResetPath;

    private final String logoutPath;

    private final String userProfilePath;

    private final String socialProvidersPath;

    StormpathConfiguration(Builder builder) {
        baseUrl = normalizeUrl(builder.baseUrl);
        oauthPath = normalizePath(builder.oauthPath);
        loginPath = normalizePath(builder.loginPath);
        registerPath = normalizePath(builder.registerPath);
        verifyEmailPath = normalizePath(builder.verifyEmailPath);
        passwordResetPath = normalizePath(builder.passwordResetPath);
        logoutPath = normalizePath(builder.logoutPath);
        userProfilePath = normalizePath(builder.userProfilePath);
        socialProvidersPath = normalizePath(builder.socialProvidersPath);
    }

    String baseUrl() {
        return baseUrl;
    }

    String oauthPath() {
        return oauthPath;
    }

    String loginPath() {
        return loginPath;
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

    String socialProvidersPath() {
        return socialProvidersPath;
    }

    String oauthUrl() {
        return baseUrl + oauthPath;
    }

    String loginUrl() {
        return baseUrl + loginPath;
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

    String socialProvidersUrl() {
        return baseUrl + socialProvidersPath;
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

        String loginPath = "/login";

        String registerPath = "/register";

        String verifyEmailPath = "/verify";

        String passwordResetPath = "/forgot";

        String logoutPath = "/logout";

        String userProfilePath = "/me";

        String socialProvidersPath = "/spa-config";

        /**
         * @param baseUrl the base URL of your API, eg. "https://api.stormpath.com".
         */
        public Builder baseUrl(String baseUrl) {
            this.baseUrl = baseUrl;
            return this;
        }

        /**
         * @param oauthPath the path used for logging in and refreshing accessToken
         */
        public Builder oauthPath(String oauthPath) {
            this.oauthPath = oauthPath;
            return this;
        }

        /**
         * @param loginPath the path used for registering a new user
         */
        public Builder loginPath(String loginPath) {
            this.loginPath = loginPath;
            return this;
        }

        /**
         * @param registerPath the path used for registering a new user
         */
        public Builder registerPath(String registerPath) {
            this.registerPath = registerPath;
            return this;
        }

        /**
         * @param verifyEmailPath the path used for verifying a user and resending the verification email (if the email verification
         *                        workflow is enabled)
         */
        public Builder verifyEmailPath(String verifyEmailPath) {
            this.verifyEmailPath = verifyEmailPath;
            return this;
        }

        /**
         * @param passwordResetPath the path used for resetting the password
         */
        public Builder passwordResetPath(String passwordResetPath) {
            this.passwordResetPath = passwordResetPath;
            return this;
        }

        /**
         * @param logoutPath the path used for logging the user out
         */
        public Builder logoutPath(String logoutPath) {
            this.logoutPath = logoutPath;
            return this;
        }

        /**
         * @param userProfilePath the path used for fetching user data
         */
        public Builder userProfilePath(String userProfilePath) {
            this.userProfilePath = userProfilePath;
            return this;
        }

        /**
         * @param socialProvidersPath the path used for fetching social providers
         */
        public Builder socialProvidersPath(String socialProvidersPath) {
            this.socialProvidersPath = socialProvidersPath;
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
            if (loginPath == null) {
                throw new IllegalStateException("loginPath == null");
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
            if (socialProvidersPath == null) {
                throw new IllegalStateException("socialProvidersPath == null");
            }

            return new StormpathConfiguration(this);
        }
    }
}
