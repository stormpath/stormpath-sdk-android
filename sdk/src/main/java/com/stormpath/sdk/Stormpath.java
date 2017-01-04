package com.stormpath.sdk;

import com.stormpath.sdk.android.AndroidPlatform;
import com.stormpath.sdk.android.CustomTabActivityHelper;
import com.stormpath.sdk.android.WebviewFallback;
import com.stormpath.sdk.models.RegisterParams;
import com.stormpath.sdk.models.SocialProviderConfiguration;
import com.stormpath.sdk.models.SocialProvidersResponse;
import com.stormpath.sdk.models.UserProfile;
import com.stormpath.sdk.providers.FacebookLoginProvider;
import com.stormpath.sdk.providers.GoogleLoginProvider;

import android.app.Activity;
import android.content.Context;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.customtabs.CustomTabsIntent;

public class Stormpath {

    static StormpathConfiguration config;

    static Platform platform;

    static ApiManager apiManager;

    @StormpathLogger.LogLevel
    static int logLevel = StormpathLogger.SILENT;

    private Stormpath() {
        // no instantiations
    }

    /**
     * Initializes the Stormpath SDK with the given configuration. You should call this in your Application onCreate() method.
     */
    public static void init(@NonNull Context context, @NonNull StormpathConfiguration config) {
        init(new AndroidPlatform(context), config);
    }

    /**
     * @return true if the the SDK was initialized, false otherwise
     */
    public static boolean isInitialized() {
        return config != null && Stormpath.platform != null && apiManager != null;
    }

    /**
     * Used for tests, we need to be able to mock the {@link Platform}.
     */
    static void init(@NonNull Platform platform, @NonNull StormpathConfiguration configuration) {
        if (isInitialized()) {
            throw new IllegalStateException("You may only initialize Stormpath once!");
        }

        Stormpath.platform = platform;
        Stormpath.platform.logger().setLogLevel(logLevel);
        config = configuration;
        apiManager = new ApiManager(config, platform);

        Stormpath.logger().v("Initialized Stormpath SDK with baseUrl: " + config.getBaseUrl());
    }
    
    /**
     * Used only for tests, we need to reset the initialization after each test.
     */
    public static void reset() {
        Stormpath.platform = null;
        config = null;
        apiManager = null;
        logLevel = StormpathLogger.SILENT;
    }

    /**
     * Logs in a user and stores the user session tokens for later use. By default it uses path /oauth/token which can be overridden via
     * {@link StormpathConfiguration}.
     */
    public static void login(String username, String password, StormpathCallback<Void> callback) {
        ensureConfigured();
        apiManager.login(username, password, callback);
    }

    /**
     * This method registers a user from the data provided. By default it uses path /register which can be overridden via
     * {@link StormpathConfiguration}.
     */
    public static void register(RegisterParams registerParams, StormpathCallback<Void> callback) {
        ensureConfigured();
        apiManager.register(registerParams, callback);
    }

    /**
     * Logs in a user and stores the user session tokens for later use. By default it uses path /oauth/token which can be overridden via
     * {@link StormpathConfiguration}.
     *
     * @param accessToken the accessToken/code received from social provider after login
     */
    public static void socialLogin(String providerId, String accessToken, String code, StormpathCallback<Void> callback) {
        ensureConfigured();
        if (SocialProvidersResponse.GOOGLE.equalsIgnoreCase(providerId)) {
            if(code!=null)
                apiManager.socialLogin(providerId, null, code, callback);

            if(accessToken!=null){
                apiManager.socialLogin(providerId, accessToken, null, callback);
            }
        }
        else if (SocialProvidersResponse.FACEBOOK.equalsIgnoreCase(providerId)){
            apiManager.socialLogin(providerId, accessToken, null, callback);
        }
    }

    public static void socialLoginFlow(@NonNull Activity activity, String providerId, SocialProviderConfiguration providerConfig){
        ensureConfigured();

        CustomTabsIntent customTabsIntent = new CustomTabsIntent.Builder().build();

        if (SocialProvidersResponse.GOOGLE.equalsIgnoreCase(providerId)) {

            //configure social provider to get the url

            //launch custom client
            GoogleLoginProvider mGoogLogin = new GoogleLoginProvider();

            CustomTabActivityHelper.openCustomTab(activity, customTabsIntent, Uri.parse(mGoogLogin.authenticationRequestURL(providerConfig)), new WebviewFallback());


        } else if(SocialProvidersResponse.FACEBOOK.equalsIgnoreCase(providerId)) {

            FacebookLoginProvider mFbLogin = new FacebookLoginProvider();


            CustomTabActivityHelper.openCustomTab(
                    activity, customTabsIntent, Uri.parse(mFbLogin.authenticationRequestURL(providerConfig)), new WebviewFallback());

        }
    }

    /**
     * Refreshes the access token and stores the new value which you can access via {@link Stormpath#accessToken()}. By default it uses
     * path /oauth/token which can be overridden via {@link StormpathConfiguration}.
     */
    public static void refreshAccessToken(StormpathCallback<Void> callback) {
        ensureConfigured();
        apiManager.refreshAccessToken(callback);
    }

    /**
     * Fetches the user profile data and returns it via the provided callback. By default it uses path /me which can be overridden via
     * {@link StormpathConfiguration}.
     */
    public static void getUserProfile(StormpathCallback<UserProfile> callback) {
        ensureConfigured();
        apiManager.getUserProfile(callback);
    }

    /**
     * Resets the password for the provided email address. By default it uses path /forgot which can be overridden via {@link
     * StormpathConfiguration}.
     */
    public static void resetPassword(String email, StormpathCallback<Void> callback) {
        ensureConfigured();
        apiManager.resetPassword(email, callback);
    }

    /**
     * Tries to verify an email using the provided sptoken which the user gets in the verification email.
     */
    public static void verifyEmail(String sptoken, StormpathCallback<Void> callback) {
        ensureConfigured();
        apiManager.verifyEmail(sptoken, callback);
    }

    /**
     * Re-sends the verification email for the account associated with the provided email address.
     */
    public static void resendVerificationEmail(String email, StormpathCallback<Void> callback) {
        ensureConfigured();
        apiManager.resendVerificationEmail(email, callback);
    }

    public static void socialGoogleCodeAuth(String code, SocialProviderConfiguration application, StormpathCallback<String> callback){
        ensureConfigured();
        apiManager.socialGoogleCodeAuth(code, application, callback);
    }

    /**
     * Logs the user out and deletes his session tokens. By default it uses path /logout which can be overridden via {@link
     * StormpathConfiguration}.
     */
    public static void logout() {
        ensureConfigured();
        apiManager.logout();
    }

    /**
     * @return the accessToken if it was saved, null otherwise
     */
    @Nullable
    public static String accessToken() {
        ensureConfigured();
        return platform.preferenceStore().getAccessToken();
    }

    public static StormpathLogger logger() {
        ensureConfigured();
        return platform.logger();
    }

    /**
     * Sets the log level for Stormpath, by default nothing is logged.
     *
     * @param logLevel one of {@link StormpathLogger#SILENT}, {@link StormpathLogger#VERBOSE}, {@link StormpathLogger#DEBUG}, {@link
     *                 StormpathLogger#INFO}, {@link StormpathLogger#WARN}, {@link StormpathLogger#ERROR}, {@link StormpathLogger#ASSERT}
     */
    public static void setLogLevel(@StormpathLogger.LogLevel int logLevel) {
        Stormpath.logLevel = logLevel;
        if (platform != null) {
            platform.logger().setLogLevel(Stormpath.logLevel);
        }
    }

    static void ensureConfigured() {
        if (!isInitialized()) {
            throw new IllegalStateException(
                    "You need to initialize Stormpath before using it. To do that call Stormpath.init() with a valid configuration.");
        }
    }
}
