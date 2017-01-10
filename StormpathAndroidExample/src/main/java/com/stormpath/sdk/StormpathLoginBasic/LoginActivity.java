package com.stormpath.sdk.StormpathLoginBasic;

import com.stormpath.sdk.Provider;
import com.stormpath.sdk.Stormpath;
import com.stormpath.sdk.StormpathCallback;
import com.stormpath.sdk.models.StormpathError;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

public class LoginActivity extends AppCompatActivity implements View.OnClickListener {

    private EditText usernameInput;
    private EditText passwordInput;
    private ProgressBar progressBar;
    private Button loginButton;
    private Button loginWithFacebookButton;
    private Button loginWithGoogleButton;
    private Button forgotPasswordButton;
    private Button registerButton;

    private StormpathCallback<Void> loginCallback = new StormpathCallback<Void>() {
        @Override
        public void onSuccess(Void aVoid) {
            hideProgress();
            navigateToHome();
        }

        @Override
        public void onFailure(StormpathError error) {
            hideProgress();
            Toast.makeText(LoginActivity.this, error.message(), Toast.LENGTH_LONG).show();
        }
    };


    private StormpathCallback<Void> forgotPasswordCallback = new StormpathCallback<Void>() {
        @Override
        public void onSuccess(Void aVoid) {

        }

        @Override
        public void onFailure(StormpathError error) {

        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        usernameInput = (EditText) findViewById(R.id.input_username);
        passwordInput = (EditText) findViewById(R.id.input_password);
        progressBar = (ProgressBar) findViewById(R.id.login_progress_bar);

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
                Stormpath.resendVerificationEmail(usernameInput.getText().toString(), forgotPasswordCallback);
                break;
            case R.id.button_register:
                startActivity(new Intent(this, RegisterActivity.class));
                break;
        }
    }

    protected void onLoginButtonClicked() {
        if (TextUtils.isEmpty(usernameInput.getText().toString()) || TextUtils.isEmpty(passwordInput.getText().toString())) {
            Toast.makeText(this, "You need to fill in the username and password!", Toast.LENGTH_SHORT).show();
            return;
        }

        showProgress();

        Stormpath.login(usernameInput.getText().toString(), passwordInput.getText().toString(), loginCallback);
    }

    private void navigateToHome() {
        startActivity(new Intent(this, MainActivity.class));
        finish();
    }

    public void showProgress() {
        loginButton.setVisibility(View.INVISIBLE);
        progressBar.setVisibility(View.VISIBLE);
    }

    public void hideProgress() {
        loginButton.setVisibility(View.VISIBLE);
        progressBar.setVisibility(View.INVISIBLE);
    }
}

