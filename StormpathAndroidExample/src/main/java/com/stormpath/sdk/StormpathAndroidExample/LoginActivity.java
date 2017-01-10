package com.stormpath.sdk.StormpathAndroidExample;

import com.stormpath.sdk.Provider;
import com.stormpath.sdk.Stormpath;
import com.stormpath.sdk.StormpathCallback;
import com.stormpath.sdk.models.StormpathError;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

public class LoginActivity extends AppCompatActivity implements View.OnClickListener {

    private EditText usernameInput;
    private EditText passwordInput;
    private Button loginButton;
    private Button loginWithFacebookButton;
    private Button loginWithGoogleButton;
    private Button forgotPasswordButton;
    private Button registerButton;

    private StormpathCallback<Void> loginCallback = new StormpathCallback<Void>() {
        @Override
        public void onSuccess(Void aVoid) {
            navigateToHome();
        }

        @Override
        public void onFailure(StormpathError error) {
            new AlertDialog.Builder(LoginActivity.this)
                    .setTitle("Error")
                    .setMessage(error.message())
                    .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {

                        }
                    })
                    .show();
        }
    };


    private StormpathCallback<Void> forgotPasswordCallback = new StormpathCallback<Void>() {
        @Override
        public void onSuccess(Void aVoid) {
            new AlertDialog.Builder(LoginActivity.this)
                    .setTitle("Password Reset Sent!")
                    .setMessage("Please check your email for the password reset email!")
                    .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {

                        }
                    })
                    .show();
        }

        @Override
        public void onFailure(StormpathError error) {
            new AlertDialog.Builder(LoginActivity.this)
                    .setTitle("Error")
                    .setMessage(error.message())
                    .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {

                        }
                    })
                    .show();
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        usernameInput = (EditText) findViewById(R.id.input_username);
        passwordInput = (EditText) findViewById(R.id.input_password);

        loginButton = (Button) findViewById(R.id.button_login);
        loginWithFacebookButton = (Button) findViewById(R.id.button_login_facebook);
        loginWithGoogleButton = (Button) findViewById(R.id.button_login_google);
        forgotPasswordButton = (Button) findViewById(R.id.button_forgot_password);
        registerButton = (Button) findViewById(R.id.button_register);

        if (!TextUtils.isEmpty(Stormpath.getAccessToken())) {
            // if we already have an accessToken saved, the user should be logged in
            navigateToHome();
        }

        loginButton.setOnClickListener(this);
        loginWithFacebookButton.setOnClickListener(this);
        loginWithGoogleButton.setOnClickListener(this);
        forgotPasswordButton.setOnClickListener(this);
        registerButton.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch(v.getId()) {
            case R.id.button_login:
                onLoginButtonClicked();
                break;
            case R.id.button_login_facebook:
                Stormpath.loginWithProvider(Provider.FACEBOOK, this, loginCallback);
                break;
            case R.id.button_login_google:
                Stormpath.loginWithProvider(Provider.GOOGLE, this, loginCallback);
                break;
            case R.id.button_forgot_password:
                Stormpath.resetPassword(usernameInput.getText().toString(), forgotPasswordCallback);
                break;
            case R.id.button_register:
                startActivity(new Intent(this, RegisterActivity.class));
                break;
        }
    }

    protected void onLoginButtonClicked() {

        Stormpath.login(usernameInput.getText().toString(), passwordInput.getText().toString(), loginCallback);
    }

    private void navigateToHome() {
        startActivity(new Intent(this, MainActivity.class));
        finish();
    }
}

