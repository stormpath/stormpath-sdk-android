# Stormpath Android SDK

[![License](https://img.shields.io/badge/license-Apache%202-blue.svg)](https://www.apache.org/licenses/LICENSE-2.0)
[![Download](https://api.bintray.com/packages/stormpath/maven/stormpath-sdk-android/images/download.svg)](https://bintray.com/stormpath/maven/stormpath-sdk-android/_latestVersion)
[![Coverage Status](https://coveralls.io/repos/github/stormpath/stormpath-sdk-android/badge.svg?branch=master)](https://coveralls.io/github/stormpath/stormpath-sdk-android?branch=master)
[![Stories in Ready](https://badge.waffle.io/stormpath/stormpath-sdk-android.png?label=ready&title=Ready)](https://waffle.io/stormpath/stormpath-sdk-android)
[![Slack Status](https://talkstormpath.shipit.xyz/badge.svg)](https://talkstormpath.shipit.xyz)

The Android SDK for [Stormpath](https://stormpath.com/), a framework for authentication & authorization.

This SDK will not send direct requests to Stormpath, and instead assumes that you'll have a backend that conforms to the Stormpath Framework Spec. Currently the [Express](https://github.com/stormpath/express-stormpath) (v3.0) and [Laravel](https://github.com/stormpath/stormpath-laravel) (v0.3) implementations of the Stormpath server-side framework are compatible. With one of these backends, you'll be able to configure Stormpath so it fits your needs.

<!-- START doctoc generated TOC please keep comment here to allow auto update -->
<!-- DON'T EDIT THIS SECTION, INSTEAD RE-RUN doctoc TO UPDATE -->
# **Table of Contents** 

- [Requirements](#requirements)
- [Installation](#installation)
- [Usage](#usage)
    - [1. Setting up](#1-setting-up)
    - [2. User registration](#2-user-registration)
    - [3. User login](#3-user-login)
    - [4. User data](#4-user-data)
    - [5. Refresh accessToken](#5-refresh-accesstoken)
    - [6. Logout](#6-logout)
    - [7. Reset password](#7-reset-password)
    - [8. Resend verification email](#8-resend-verification-email)
    - [9. Verify email](#9-verify-email)
- [License](#license)

<!-- END doctoc generated TOC please keep comment here to allow auto update -->


# Requirements

- Android 2.1+ (API level 7)

# Installation

Add the SDK as a dependency to your `build.gradle` to automatically download it from jcenter.

```groovy
compile 'com.stormpath.sdk:stormpath-sdk-android:1.1.2'
```

If you intend to use the UI components for an integrated Login and Registration flow, add the following dependency as well

```groovy
compile 'com.stormpath.sdk:stormpath-sdk-android-ui:1.1.2'
```

# Usage

# 1. Setting up

To set up the SDK call `Stormpath.init()` in your Applications `onCreate()` method:

```java
StormpathConfiguration stormpathConfiguration = new StormpathConfiguration.Builder()
        .baseUrl("https://api.example.com")
        .build();
Stormpath.init(this, stormpathConfiguration);
```

You can set custom paths via `StormpathConfiguration.Builder` methods if you need to.

You can also enable logging for your debug builds:

```java
if (BuildConfig.DEBUG) {
    // don't enable logging for release builds, the logs contain sensitive information!
    Stormpath.setLogLevel(StormpathLogger.VERBOSE);
}
```

## 2. User registration

To register a user create a `RegisterParams` object with valid data:

```java
RegisterParams registerParams = new RegisterParams("firstName", "surname", "user@example.com", "Pa55w0rd");
```

and use it to call `Stormpath.register()`:

```java
Stormpath.register(registerParams, new StormpathCallback<Void>() {
    @Override
    public void onSuccess(Void aVoid) {
        // user successfully registered
    }

    @Override
    public void onFailure(StormpathError error) {
        // registration failed
    }
});
```

## 3a. User login

To log the user in, call `Stormpath.login()` with the credentials:

```java
Stormpath.login("user@example.com", "Pa55w0rd", new StormpathCallback<Void>() {
    @Override
    public void onSuccess(Void aVoid) {
        // login was successful, we can now show the main screen of the app
    }

    @Override
    public void onFailure(StormpathError error) {
        // something went wrong
    }
});
```

After login succeeds, the accessToken will be saved by the SDK. You can then get it by calling `Stormpath.accessToken()`.

## 3b. User login with Facebook or Google

Stormpath also supports logging in with Facebook or Google. There are two flows for enabling this:

1. Let Stormpath handle the Facebook / Google log in.
2. Use the Facebook / Google iOS SDK to get an access token, and pass it to Stormpath to log in.

We've made it extremely easy to set up social login without using the Facebook / Google SDK, but if you need to use their SDKs for more features besides logging in, you skip to the section about "Using the Google or Facebook SDK"

First, make sure your StormpathConfiguration's baseUrl is pointing at your server. If it is pointing at an example project's server you will not get accurate indication of your keys working. You will not be able to retrieve an access token.

### Setting up Facebook Login

To get started, you first need to [register an application](https://developers.facebook.com/?advanced_app_create=true) with Facebook. After registering your app, go into your app dashboard's settings page. Click "Add Platform", and fill in your android package name, generate hashes for your android keys (typically your android debug key as well as the one you will be signing your app with), and turn "Single Sign On" on. 

Then, [sign into Stormpath](https://api.stormpath.com/login) and add a Facebook directory to your account. Fill the App ID and Secret with the values given to you in the Facebook app dashboard. 

Add the Facebook directory to your Stormpath application.

Finally, open up your App's project and go to the project's strings.xml. Add a key called facebook_app_id type in `fb[APP_ID_HERE]`, replacing `[APP_ID_HERE]` with your Facebook App ID. ex. <string name="facebook_app_id">fbyourappid</string>

Then, you can initiate the login screen by calling: 

```java
Stormpath.socialLoginFlow(SocialLoginActivity.this, SocialProvidersResponse.FACEBOOK, new SocialProviderConfiguration(getString(R.string.facebook_app_id), getString(R.string.facebook_app_id)));
```

SocialProviderConfiguration takes an urlScheme and appId. For Facebook and Google the Stormpath SDK is currently configured to manipulate the app_id strings to conform with both urlScheme and appId.

### Setting up Google Login

To get started, you first need to [register an application](https://console.developers.google.com/project) with Google. Click "Enable and Manage APIs", and then the credentials tab. Create two sets of OAuth Client IDs, one as "Web Application", and one as "Android". 

Edit the "Web Application", to specify the authorized redirect URI, which will be the Client ID tokens in reverse order + :/oauth2callback. ie. if client ID  58993jddjd.apps.googleusercontent.com then your redirect URI here will be com.googleusercontent.apps.58993jddjd:/oauth2callback

Copy your reverse order client id ("com.googleusercontent.apps.58993jddjd" continuing the example), and paste it in your android strings.xml file in a key called goog_app_id

Then, [sign into Stormpath](https://api.stormpath.com/login) and add a Google directory to your account. Fill in the Client ID and Secret with the values given to you for the web client. (You can fill in "Google Authorized Redirect URI" with the same redirect URI created in the Google Developers Console. 

Then, add the directory to your Stormpath application. 

Finally, you can initiate the login screen by calling: 

```java
Stormpath.socialLoginFlow(SocialLoginActivity.this, SocialProvidersResponse.FACEBOOK, new SocialProviderConfiguration(getString(R.string.goog_app_id), getString(R.string.goog_app_id)));
```

### Using the Google or Facebook SDK

If you're using the Facebook SDK or Google SDK for your app, follow their setup instructions instead. Once you successfully sign in with their SDK, utilize the following methods to send your access token to Stormpath, and log in your user:

```java
Stormpath.socialLogin(SocialProvidersResponse.FACEBOOK, /*access token from SDK*/, null,
                new StormpathCallback<Void>() {
    @Override
    public void onSuccess(Void aVoid) {
        /*your code here*/
    }

    @Override
    public void onFailure(StormpathError error) {
        /*your code here*/
    }
});
```

SocialProvidersResponse.GOOGLE is a valid provider for authenticating with Google.

## 4. User data

You can fetch the user data for the currently logged in user:

```java
Stormpath.getUserProfile(new StormpathCallback<UserProfile>() {
    @Override
    public void onSuccess(UserProfile userProfile) {
        // user data ready
    }

    @Override
    public void onFailure(StormpathError error) {
        // something went wrong
    }
});
```

## 5. Refresh accessToken

If the accessToken expires, you can try to refresh it:

```java
Stormpath.refreshAccessToken(new StormpathCallback<Void>() {
    @Override
    public void onSuccess(Void aVoid) {
        // success, your new accessToken has been saved
    }

    @Override
    public void onFailure(StormpathError error) {
        // something went wrong - the user will have to log in again
    }
});
```

## 6. Logout

You can also log the current user out:

```java
Stormpath.logout();
```

This will clear the saved accessToken.

## 7. Reset password

To reset a password for a user, use their email address to call `Stormpath.resetPassword()`:

```java
Stormpath.resetPassword("user@example.com", new StormpathCallback<Void>() {
    @Override
    public void onSuccess(Void aVoid) {
        // success!
    }

    @Override
    public void onFailure(StormpathError error) {
        // something went wrong
    }
});
```

## 8. Resend verification email

You can resend a verification email if the email verification flow is enabled:

```java
Stormpath.resendVerificationEmail("user@example.com", new StormpathCallback<Void>() {
    @Override
    public void onSuccess(Void aVoid) {
        // success!
    }

    @Override
    public void onFailure(StormpathError error) {
        // something went wrong
    }
});
```

## 9. Verify email

If you need to verify the user via the API, you can do so using the `sptoken` from the verification email:

```java
Stormpath.verifyEmail(sptoken, new StormpathCallback<Void>() {
    @Override
    public void onSuccess(Void aVoid) {
        // success!
    }

    @Override
    public void onFailure(StormpathError error) {
        // something went wrong
    }
});
```

# License

This project is open source and uses the Apache 2.0 License. See [LICENSE file](LICENSE) for details.
