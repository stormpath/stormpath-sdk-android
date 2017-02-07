package com.stormpath.sdk;

import android.app.Activity;
import android.net.Uri;
import android.os.Bundle;

/**
 * Created by edjiang on 1/5/17.
 */
public class CustomTabActivity extends Activity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Uri callbackUri = getIntent().getData();
        assert callbackUri != null;
        assert callbackUri.getScheme() == Stormpath.config.getUrlScheme();

        String stormpathToken = callbackUri.getQueryParameter("jwtResponse");

        Stormpath.socialLoginManager.handleCallback(stormpathToken);
        finish();
    }
}
