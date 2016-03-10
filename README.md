[![Stories in Ready](https://badge.waffle.io/stormpath/stormpath-sdk-android.png?label=ready&title=Ready)](https://waffle.io/stormpath/stormpath-sdk-android)
# Stormpath Android SDK

[![License](https://img.shields.io/badge/license-Apache%202-blue.svg)](https://www.apache.org/licenses/LICENSE-2.0)
[![Download](https://api.bintray.com/packages/stormpath/maven/stormpath-sdk-android/images/download.svg)](https://bintray.com/stormpath/maven/stormpath-sdk-android/_latestVersion)
[![Coverage Status](https://coveralls.io/repos/github/stormpath/stormpath-sdk-android/badge.svg?branch=master)](https://coveralls.io/github/stormpath/stormpath-sdk-android?branch=master)

The Android SDK for [Stormpath](https://stormpath.com/), a framework for authentication & authorization.

Currently it's known only to work against the [Express-Stormpath](https://github.com/stormpath/express-stormpath) integration.

# Requirements

- Android 4.0+ (API level 14)

# Installation

Add the SDK as a dependency to your `build.gradle` to automatically download it from jcenter.

```groovy
compile 'com.stormpath.sdk:stormpath-sdk-android:0.1.0'
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
    public void onFailure(Throwable t) {
        // registration failed
    }
});
```

## 3. User login

To log the user in, call `Stormpath.login()` with the credentials:

```java
Stormpath.login("user@example.com", "Pa55w0rd", new StormpathCallback<Void>() {
    @Override
    public void onSuccess(Void aVoid) {
        // login was successful, we can now show the main screen of the app
    }

    @Override
    public void onFailure(Throwable t) {
        // something went wrong
    }
});
```

After login succeeds, the accessToken will be saved by the SDK. You can then get it by calling `Stormpath.accessToken()`.

## 4. User data

You can fetch the user data for the currently logged in user:

```java
Stormpath.getUserProfile(new StormpathCallback<UserProfile>() {
    @Override
    public void onSuccess(UserProfile userProfile) {
        // user data ready
    }

    @Override
    public void onFailure(Throwable t) {
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
    public void onFailure(Throwable t) {
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
    public void onFailure(Throwable t) {
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
    public void onFailure(Throwable t) {
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
    public void onFailure(Throwable t) {
        // something went wrong
    }
});
```

# License

This project is open source and uses the Apache 2.0 License. See [LICENSE file](LICENSE) for details.
