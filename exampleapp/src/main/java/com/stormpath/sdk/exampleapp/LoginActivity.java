package com.stormpath.sdk.exampleapp;

import com.stormpath.sdk.Stormpath;
import com.stormpath.sdk.StormpathCallback;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class LoginActivity extends AppCompatActivity {

    @Bind(R.id.input_username)
    EditText usernameInput;

    @Bind(R.id.input_password)
    EditText passwordInput;

    @Bind(R.id.progress_bar_login)
    ProgressBar progressBar;

    @Bind(R.id.button_login)
    Button loginButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        ButterKnife.bind(this);

        // TODO reset password button
    }

    @OnClick(R.id.button_login)
    protected void onLoginButtonClicked() {
        if (TextUtils.isEmpty(usernameInput.getText().toString()) || TextUtils.isEmpty(passwordInput.getText().toString())) {
            Snackbar.make(loginButton, "You need to fill in the username and password!", Snackbar.LENGTH_SHORT).show();
            return;
        }

        showProgress();

        Stormpath.login(usernameInput.getText().toString(), passwordInput.getText().toString(), new StormpathCallback<Void>() {
            @Override
            public void onSuccess(Void aVoid) {
                hideProgress();

                // login was successful, we can now show the main screen of the app
                navigateToHome();
            }

            @Override
            public void onFailure(Throwable t) {
                hideProgress();
                Snackbar.make(loginButton, t.getMessage(), Snackbar.LENGTH_LONG).show();
            }
        });
    }

    @OnClick(R.id.button_register)
    protected void onRegisterButtonClicked() {
        startActivity(new Intent(this, RegisterActivity.class));
        finish();
    }

    public void navigateToHome() {
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
