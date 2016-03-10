package com.stormpath.sdk.exampleapp;

import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.login.LoginManager;
import com.facebook.login.LoginResult;
import com.stormpath.sdk.Stormpath;
import com.stormpath.sdk.StormpathCallback;
import com.stormpath.sdk.models.SocialProvidersResponse;
import com.stormpath.sdk.models.StormpathError;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

public class SocialLoginActivity extends AppCompatActivity implements FacebookCallback<LoginResult> {

    CallbackManager callbackManager;

    Button loginButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_social_login);

        // Don't forget to set your fb app id in strings.xml!
        callbackManager = CallbackManager.Factory.create();
        loginButton = (Button) findViewById(R.id.login_button);

        setUpButton(loginButton);

        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (isUserLoggedIn()) {
                    LoginManager.getInstance().logOut();
                    Stormpath.logout();
                    setUpButton(loginButton);
                } else {
                    LoginManager.getInstance().logInWithReadPermissions(SocialLoginActivity.this, null);
                }
            }
        });
        LoginManager.getInstance().registerCallback(callbackManager, this);
    }

    private boolean isUserLoggedIn() {
        return Stormpath.accessToken() != null;
    }

    private void setUpButton(Button loginButton) {
        if (isUserLoggedIn()) {
            loginButton.setText(R.string.log_out);
        } else {
            loginButton.setText(R.string.log_in_with_facebook);
        }
    }

    @Override
    public void onSuccess(final LoginResult loginResult) {
        Stormpath.socialLogin(SocialProvidersResponse.FACEBOOK, loginResult.getAccessToken().getToken(),
                new StormpathCallback<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        // we are logged in via fb!
                        Toast.makeText(SocialLoginActivity.this, "Successful login!", Toast.LENGTH_LONG).show();
                        setUpButton(loginButton);
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
