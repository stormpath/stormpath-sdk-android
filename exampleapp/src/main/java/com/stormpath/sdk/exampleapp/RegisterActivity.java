package com.stormpath.sdk.exampleapp;

import com.stormpath.sdk.Stormpath;
import com.stormpath.sdk.StormpathCallback;
import com.stormpath.sdk.models.RegisterParams;
import com.stormpath.sdk.models.StormpathError;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class RegisterActivity extends AppCompatActivity {

    @Bind(R.id.toolbar)
    Toolbar toolbar;

    @Bind(R.id.input_firstname)
    EditText firstNameEditText;

    @Bind(R.id.input_surname)
    EditText surnameEditText;

    @Bind(R.id.input_username)
    EditText usernameEditText;

    @Bind(R.id.input_password)
    EditText passwordEditText;

    @Bind(R.id.progress_bar_register)
    ProgressBar progressBar;

    @Bind(R.id.button_register)
    Button registerButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //setContentView(R.layout.activity_register);
        ButterKnife.bind(this);

        setSupportActionBar(toolbar);
        setTitle("Register");
    }

    @OnClick(R.id.button_register)
    protected void onRegisterButtonClicked() {
        if (TextUtils.isEmpty(firstNameEditText.getText().toString())
                || TextUtils.isEmpty(surnameEditText.getText().toString())
                || TextUtils.isEmpty(usernameEditText.getText().toString())
                || TextUtils.isEmpty(passwordEditText.getText().toString())) {
            Snackbar.make(registerButton, "All fields are mandatory!", Snackbar.LENGTH_SHORT).show();
            return;
        }

        Stormpath.register(new RegisterParams(firstNameEditText.getText().toString(), surnameEditText.getText().toString(),
                usernameEditText.getText().toString(), passwordEditText.getText().toString()), new StormpathCallback<Void>() {
            @Override
            public void onSuccess(Void aVoid) {
                hideProgress();
                navigateToHome();
            }

            @Override
            public void onFailure(StormpathError error) {
                hideProgress();
                Snackbar.make(registerButton, error.message(), Snackbar.LENGTH_LONG).show();
            }
        });

        showProgress();
    }

    public void navigateToHome() {
        startActivity(new Intent(this, MainActivity.class));
        finish();
    }

    public void showProgress() {
        registerButton.setVisibility(View.INVISIBLE);
        progressBar.setVisibility(View.VISIBLE);
    }

    public void hideProgress() {
        registerButton.setVisibility(View.VISIBLE);
        progressBar.setVisibility(View.INVISIBLE);
    }
}
