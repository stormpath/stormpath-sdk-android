package com.stormpath.sdk.exampleapp;

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

public class LoginActivity extends AppCompatActivity {

    protected EditText usernameInput;

    protected EditText passwordInput;

    protected ProgressBar progressBar;

    protected Button loginButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        usernameInput = (EditText) findViewById(R.id.input_username);
        passwordInput = (EditText) findViewById(R.id.input_password);
        progressBar = (ProgressBar) findViewById(R.id.login_progress_bar);
        loginButton = (Button) findViewById(R.id.login_button);

        if (!TextUtils.isEmpty(Stormpath.accessToken())) {
            // if we already have an accessToken saved, the user should be logged in
            navigateToHome();
        }

        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onLoginButtonClicked();
            }
        });
    }

    protected void onLoginButtonClicked() {
        if (TextUtils.isEmpty(usernameInput.getText().toString()) || TextUtils.isEmpty(passwordInput.getText().toString())) {
            Toast.makeText(this, "You need to fill in the username and password!", Toast.LENGTH_SHORT).show();
            return;
        }

        showProgress();

        Stormpath.login(usernameInput.getText().toString(), passwordInput.getText().toString(), new StormpathCallback<Void>() {
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
        });
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

