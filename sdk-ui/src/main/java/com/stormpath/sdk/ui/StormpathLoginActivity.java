package com.stormpath.sdk.ui;

import com.stormpath.sdk.Stormpath;

import android.app.Activity;
import android.content.pm.ActivityInfo;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.view.Window;
import android.view.WindowManager;


public class StormpathLoginActivity extends AppCompatActivity implements StormpathLoginFragment.StormpathLoginFragmentListener, StormpathRegisterFragment.StormpathRegisterFragmentListener, StormpathResetPasswordFragment.StormpathResetPasswordFragmentListener {

    private Bundle configOptions;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Disable landscape
        this.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);

        this.requestWindowFeature(Window.FEATURE_NO_TITLE);

        setContentView(R.layout.stormpath_activity_login);

        configOptions = getMergedOptions();

        if(getIntent().getExtras()!=null)
            getIntent().getExtras().putAll(configOptions);

        if (savedInstanceState == null) {
            FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
            ft.replace(R.id.stormpath_container, StormpathLoginFragment.newInstance(configOptions), null).commit();
        }
    }

    private Bundle getMergedOptions() {
        // Read activity metadata from AndroidManifest.xml
        ActivityInfo activityInfo = null;
        try {
            activityInfo = getPackageManager().getActivityInfo(this.getComponentName(), PackageManager.GET_META_DATA);
        } catch (PackageManager.NameNotFoundException e) {
            Stormpath.logger().w(e, "Error while parsing LoginActivity metadata!");
        }

        // The options specified in the Intent (from ParseLoginBuilder) will
        // override any duplicate options specified in the activity metadata
        Bundle mergedOptions = new Bundle();
        if (activityInfo != null && activityInfo.metaData != null) {
            mergedOptions.putAll(activityInfo.metaData);
        }
        if (getIntent().getExtras() != null) {
            mergedOptions.putAll(getIntent().getExtras());
        }

        return mergedOptions;
    }

    @Override
    public void onRegisterClicked(String username, String password) {
        // TODO
        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
        ft.replace(R.id.stormpath_container, StormpathRegisterFragment.newInstance(configOptions), null)
                .addToBackStack("register")
        .commit();

    }

    @Override
    public void onLoginSuccess() {
        setResult(RESULT_OK);
        finish();
    }

    @Override
    public void onForgotPasswordClicked(String username) {

        configOptions.putString("username", username);

        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
        ft.replace(R.id.stormpath_container, StormpathResetPasswordFragment.newInstance(configOptions), null)
                .addToBackStack("resetpw")
                .commit();


    }

    @Override
    public void onFragmentInteraction(Uri uri) {

    }
}
