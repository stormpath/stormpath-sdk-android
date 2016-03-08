package com.stormpath.sdk.StormpathLoginWithStyle;

import com.stormpath.sdk.Stormpath;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;

import butterknife.ButterKnife;
import butterknife.OnClick;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);

        // TODO fetch user profile and show data
        // TODO display button to refresh token
}

    @OnClick(R.id.button_logout)
    protected void onLogoutClicked() {
        Stormpath.logout();
        startActivity(new Intent(this, SplashActivity.class));
        finish();
    }
}
