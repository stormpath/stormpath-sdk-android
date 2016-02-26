package com.stormpath.sdk.ui;

import com.stormpath.sdk.Stormpath;
import com.stormpath.sdk.StormpathCallback;
import com.stormpath.sdk.models.StormpathError;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;

public class LoginFragment extends Fragment {

    protected EditText usernameInput;

    protected EditText passwordInput;

    protected ProgressBar progressBar;

    protected Button loginButton;

    protected Button registerButton;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.stormpath_fragment_login, container, false);

        usernameInput = (EditText) view.findViewById(R.id.stormpath_input_username);
        passwordInput = (EditText) view.findViewById(R.id.stormpath_input_password);
        progressBar = (ProgressBar) view.findViewById(R.id.stormpath_login_progress_bar);
        loginButton = (Button) view.findViewById(R.id.stormpath_login_button);
        registerButton = (Button) view.findViewById(R.id.stormpath_register_button);

        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onLoginButtonClicked();
            }
        });

        registerButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onRegisterButtonClicked();
            }
        });

        return view;
    }

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
            public void onFailure(StormpathError error) {
                hideProgress();
                Snackbar.make(loginButton, error.message(), Snackbar.LENGTH_LONG).show();
            }
        });
    }

    protected void onRegisterButtonClicked() {
        // TODO
    }

    public void navigateToHome() {
        // TODO
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
