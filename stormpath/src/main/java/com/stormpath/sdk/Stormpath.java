package com.stormpath.sdk;

public class Stormpath {

    private String baseUrl;

    private String oauthPath;

    private String registerPath;

    private String passwordResetPath;

    private String verifyEmailPath;

    private String logoutPath;

    private String userProfilePath;

    Stormpath(Builder builder) {
        baseUrl = normalizeUrl(builder.baseUrl);
        oauthPath = normalizePath(builder.oauthPath);
        registerPath = normalizePath(builder.registerPath);
        passwordResetPath = normalizePath(builder.passwordResetPath);
        verifyEmailPath = normalizePath(builder.verifyEmailPath);
        logoutPath = normalizePath(builder.logoutPath);
        userProfilePath = normalizePath(builder.userProfilePath);
    }

    public String getBaseUrl() {
        return baseUrl;
    }

    public String getOauthPath() {
        return oauthPath;
    }

    public String getRegisterPath() {
        return registerPath;
    }

    public String getPasswordResetPath() {
        return passwordResetPath;
    }

    public String getVerifyEmailPath() {
        return verifyEmailPath;
    }

    public String getLogoutPath() {
        return logoutPath;
    }

    public String getUserProfilePath() {
        return userProfilePath;
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

        String oauthPath = "/auth/token";

        String registerPath = "/register";

        String passwordResetPath = "/forgot";

        String verifyEmailPath = "/verify";

        String logoutPath = "/logout";

        String userProfilePath = "/me";

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

        public Builder passwordResetPath(String passwordResetPath) {
            this.passwordResetPath = passwordResetPath;
            return this;
        }

        public Builder verifyEmailPath(String verifyEmailPath) {
            this.verifyEmailPath = verifyEmailPath;
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

        public Stormpath build() {
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
            if (passwordResetPath == null) {
                throw new IllegalStateException("passwordResetPath == null");
            }
            if (verifyEmailPath == null) {
                throw new IllegalStateException("verifyEmailPath == null");
            }
            if (logoutPath == null) {
                throw new IllegalStateException("logoutPath == null");
            }
            if (userProfilePath == null) {
                throw new IllegalStateException("userProfilePath == null");
            }

            return new Stormpath(this);
        }
    }
}
