# Stormpath Android SDK

[![License](https://img.shields.io/badge/license-Apache%202-blue.svg)](https://www.apache.org/licenses/LICENSE-2.0)
[![Download](https://api.bintray.com/packages/stormpath/maven/stormpath-sdk-android/images/download.svg)](https://bintray.com/stormpath/maven/stormpath-sdk-android/_latestVersion)
[![Coverage Status](https://coveralls.io/repos/github/stormpath/stormpath-sdk-android/badge.svg?branch=master)](https://coveralls.io/github/stormpath/stormpath-sdk-android?branch=master)
[![Stories in Ready](https://badge.waffle.io/stormpath/stormpath-sdk-android.png?label=ready&title=Ready)](https://waffle.io/stormpath/stormpath-sdk-android)
[![Slack Status](https://talkstormpath.shipit.xyz/badge.svg)](https://talkstormpath.shipit.xyz)

Stormpath's Android SDK allows developers utilizing Stormpath to quickly integrate authentication and token management into their apps. 

We're constantly iterating and improving the SDK, so please don't hesitate to send us your feedback! You can reach us via support@stormpath.com, or on the issue tracker for feature requests. 

<!-- START doctoc generated TOC please keep comment here to allow auto update -->
<!-- DON'T EDIT THIS SECTION, INSTEAD RE-RUN doctoc TO UPDATE -->
# **Table of Contents** 

- [Requirements](#requirements)
- [Installation](#installation)
- [Usage](#usage)
  - [Configuring Stormpath](#configuring-stormpath)
  - [User registration](#user-registration)
  - [Logging in](#logging-in)
  - [Logging in with Social Providers](#logging-in-with-social-providers)
    - [Configure Your Social Directory in Stormpath](#configure-your-social-directory-in-stormpath)
    - [Setting up your Android project](#setting-up-your-android-project)
    - [Initiating Social Login](#initiating-social-login)
    - [Using the Google or Facebook SDK](#using-the-google-or-facebook-sdk)
  - [User data](#user-data)
  - [Using the Access Token](#using-the-access-token)
  - [Logout](#logout)
  - [Reset password](#reset-password)
  - [Resend verification email](#resend-verification-email)
  - [Verify email](#verify-email)
- [License](#license)

<!-- END doctoc generated TOC please keep comment here to allow auto update -->


# Requirements

- Android 2.1+ (API level 7)

# Installation

Add the SDK as a dependency to your `build.gradle` to automatically download it from jcenter.

```groovy
compile 'com.stormpath.sdk:stormpath-sdk-android:2.0'
```

# Usage

To see the SDK in action, you can try running the `com.stormpath.sdk.StormpathAndroidExample` project. 

## Configuring Stormpath

The Android SDK (v2) leverages the [Stormpath Client API](https://docs.stormpath.com/client-api/product-guide/latest/index.html) for its authentication needs. You'll need to sign into the [Stormpath Admin Console](https://api.stormpath.com/) to get your Client API details. Go into your Application > Policies > Client API, and ensure that it's enabled. Copy your Client API URL, and set it in your Android project: 


```java
StormpathConfiguration stormpathConfiguration = new StormpathConfiguration.Builder()
        .baseUrl("https://stormpath-notes.apps.stormpath.io/")
        .build();
Stormpath.init(this, stormpathConfiguration);
```

You can also enable logging for your debug builds:

```java
if (BuildConfig.DEBUG) {
    // don't enable logging for release builds, the logs contain sensitive information!
    Stormpath.setLogLevel(StormpathLogger.VERBOSE);
}
```

## User registration

In order to register a user, instantiate a `RegistrationForm` object. Stormpath requires an `email` and `password` to register.

```java
RegistrationForm registrationForm = new RegistrationForm("user@example.com", "Pa55w0rd");
```

Then, just invoke the register method on `Stormpath` class:

```java
Stormpath.register(registrationData, new StormpathCallback<Void>() {
    @Override
    public void onSuccess(Void aVoid) {
        // Handle successful registration
    }

    @Override
    public void onFailure(StormpathError error) {
        // Handle registration error
    }
});
```

## Logging in

To log in, collect the email (or username) and password from the user, and then pass them to the login method:

```java
Stormpath.login("user@example.com", "Pa55w0rd", new StormpathCallback<Void>() {
    @Override
    public void onSuccess(Void aVoid) {
        // Handle successful login
    }

    @Override
    public void onFailure(StormpathError error) {
        // Handle login error
    }
});
```

After login succeeds, the accessToken will be saved by the SDK. You can then get it by calling `Stormpath.accessToken()`.

## Logging in with Social Providers

Stormpath also supports logging in with a variety of social providers Facebook, Google, LinkedIn, GitHub, and more. There are two flows for enabling this:

1. Let Stormpath handle the social login.
2. Use the social provider's iOS SDK to get an access token, and pass it to Stormpath to log in.

We've made it extremely easy to set up social login without using the social provider SDKs, but if you need to use their SDKs for more features besides logging in, you should use flow #2 (and skip directly to [Using a social provider SDK](#using-a-social-provider-sdk)). 

### Configure Your Social Directory in Stormpath

To set up your social directory, read more about [social login in the Stormpath Client API Guide](https://docs.stormpath.com/client-api/product-guide/latest/social_login.html#before-you-start).


### Setting up your Android project

In your `AndroidManifest.xml`, you'll need to add Stormpath's login handler activity and configure it with an intent filter to recieve login callbacks from Stormpath. 

Add this to your manifest:

```xml
<activity android:name="com.stormpath.sdk.CustomTabActivity"
    android:exported="true">
    <intent-filter>
        <action android:name="android.intent.action.VIEW" />
        <category android:name="android.intent.category.DEFAULT" />
        <category android:name="android.intent.category.BROWSABLE" />
        <data android:scheme="REPLACE-ME-HERE" />
    </intent-filter>
</activity>
```

For the `data android:scheme` tag, type in your Client API's DNS label, but reversed. For instance, if your Client API DNS Label is `edjiang.apps.stormpath.io`, type in `io.stormpath.apps.edjiang`. 

In the [Stormpath Admin Console](https://api.stormpath.com)'s Application settings, add that URL as an "authorized callback URL", appending `://stormpathCallback`. Following my earlier example, I would use `io.stormpath.apps.edjiang`. 

### Initiating Social Login

Now, you can initiate the login screen by calling: 

```java
Stormpath.loginWithProvider(Provider.FACEBOOK, this, new StormpathCallback<Void>() {
    @Override
    public void onSuccess(Void aVoid) {
        // Handle successful login
    }

    @Override
    public void onFailure(StormpathError error) {
        // Handle login error
    }
});
```

Valid `Provider` enum values are: `FACEBOOK, GOOGLE, LINKEDIN, GITHUB, TWITTER`, or you can enter a string. 

### Using the Google or Facebook SDK

If you're using the Facebook SDK or Google SDK for your app, follow their setup instructions instead. Once you successfully sign in with their SDK, utilize the following methods to send your access token to Stormpath, and log in your user:

```java
Stormpath.loginWithProvider(Provider.FACEBOOK, accessToken, new StormpathCallback<Void>() {
    @Override
    public void onSuccess(Void aVoid) {
        // Handle successful login
    }

    @Override
    public void onFailure(StormpathError error) {
        // Handle login error
    }
});
```

## User data

You can fetch the user data for the currently logged in user:

```java
Stormpath.getAccount(new StormpathCallback<Account>() {
    @Override
    public void onSuccess(Account account) {
        // Do things with the account
    }

    @Override
    public void onFailure(StormpathError error) {
        // Account request failed
    }
});
```

## Using the Access Token

You can utilize the access token to access any of your API endpoints that require authentication. It's stored as a property on the Stormpath object as `Stormpath.getAccessToken()`. You can use the access token by adding it to your `Authorization` header using the `Bearer scheme`. This looks like the following:

`Authorization: Bearer ACCESSTOKEN`

When the access token expires, you may need to refresh it. Expiration times are configurable in the Stormpath application settings. 

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

## Logout

You can also log the current user out:

```java
Stormpath.logout();
```

This will clear the saved accessToken and invalidate the token with the server.

## Reset password

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

## Resend verification email

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

## Verify email

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
