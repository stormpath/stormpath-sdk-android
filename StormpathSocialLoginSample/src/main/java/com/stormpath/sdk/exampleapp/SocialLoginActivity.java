package com.stormpath.sdk.exampleapp;

import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.login.LoginManager;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;
import com.stormpath.sdk.Stormpath;
import com.stormpath.sdk.StormpathCallback;
import com.stormpath.sdk.models.SocialProvidersResponse;
import com.stormpath.sdk.models.StormpathError;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.Toast;

public class SocialLoginActivity extends AppCompatActivity implements FacebookCallback<LoginResult> {

    CallbackManager callbackManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_social_login);

        // Don't forget to set your fb app id in strings.xml!
        callbackManager = CallbackManager.Factory.create();
        LoginButton loginButton = (LoginButton) findViewById(R.id.login_button);
        LoginManager.getInstance().registerCallback(callbackManager, this);


    }

    @Override
    public void onSuccess(final LoginResult loginResult) {
        Stormpath.socialLogin(SocialProvidersResponse.FACEBOOK, loginResult.getAccessToken().getToken(),
                new StormpathCallback<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        // we are logged in via fb!
                        Toast.makeText(SocialLoginActivity.this, "Success! " + loginResult.getAccessToken().getToken(), Toast.LENGTH_LONG).show();
                    }

                    @Override
                    public void onFailure(StormpathError error) {
                        Toast.makeText(SocialLoginActivity.this, error.message(), Toast.LENGTH_LONG).show();
                    }
                });
    }

    @Override
    public void onCancel() {
        // fb login was cancelled ny the user
        Toast.makeText(SocialLoginActivity.this, "Canceled!", Toast.LENGTH_LONG).show();

    }

    @Override
    public void onError(FacebookException error) {
        // an error occurred while logging in via fb
        Toast.makeText(SocialLoginActivity.this, "Error!", Toast.LENGTH_LONG).show();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        callbackManager.onActivityResult(requestCode, resultCode, data);
    }
}
