package com.stormpath.sdk.StormpathSocialLoginSample;

import com.facebook.FacebookSdk;
import com.stormpath.sdk.Stormpath;
import com.stormpath.sdk.StormpathConfiguration;
import com.stormpath.sdk.StormpathLogger;
import com.stormpath.sdk.StormpathSocialLoginSample.BuildConfig;

import android.app.Application;

public class StormpathSocialLoginSampleApp extends Application {

    @Override
    public void onCreate() {
        super.onCreate();

        FacebookSdk.sdkInitialize(this);

        if (BuildConfig.DEBUG) {
            // we only want to show the logs in debug builds, for easier debugging
            Stormpath.setLogLevel(StormpathLogger.VERBOSE);
        }

        // initialize stormpath
        StormpathConfiguration stormpathConfiguration = new StormpathConfiguration.Builder()
                .baseUrl("http://172.16.0.179:3000") //"https://stormpathnotes.herokuapp.com"
                .build();
        Stormpath.init(this, stormpathConfiguration);
    }
}
