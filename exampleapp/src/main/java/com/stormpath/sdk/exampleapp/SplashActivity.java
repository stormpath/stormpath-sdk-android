package com.stormpath.sdk.exampleapp;

import com.stormpath.sdk.Stormpath;
import com.stormpath.sdk.StormpathConfiguration;
import com.stormpath.sdk.ui.StormpathLoginActivity;
import com.stormpath.sdk.ui.StormpathLoginConfig;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;

public class SplashActivity extends AppCompatActivity {

    public static final int REQUEST_LOGIN = 422;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (Stormpath.accessToken() != null) {
            navigateToHome();
        } else {
            navigateToLogin();
        }
    }

    private void navigateToLogin() {

        Intent stormpathLoginFlowIntent = new Intent(this, StormpathLoginActivity.class);

        stormpathLoginFlowIntent.putExtras(new StormpathLoginConfig.Builder()
                .setBackgroundColor(-1)
                .setIcon(-1)
                .create());

        startActivityForResult(stormpathLoginFlowIntent, REQUEST_LOGIN);
    }

    private void navigateToHome() {
        startActivity(new Intent(this, MainActivity.class));
        finish();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch (requestCode) {
            case REQUEST_LOGIN:
                if (resultCode == Activity.RESULT_OK) {
                    // we are logged in so, let's navigate to home
                    navigateToHome();
                } else {
                    // looks like the user couldn't login and gave up by pressing the back button
                    finish();
                }
            default:
                super.onActivityResult(requestCode, resultCode, data);
        }
    }
}
