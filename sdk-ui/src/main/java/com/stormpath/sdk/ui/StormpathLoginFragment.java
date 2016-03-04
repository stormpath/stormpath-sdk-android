package com.stormpath.sdk.ui;

import com.stormpath.sdk.Stormpath;
import com.stormpath.sdk.StormpathCallback;
import com.stormpath.sdk.models.StormpathError;

import android.app.Activity;
import android.content.Context;
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

public class StormpathLoginFragment extends Fragment {

    protected EditText usernameInput;

    protected EditText passwordInput;

    protected ProgressBar progressBar;

    protected Button loginButton;

    protected Button registerButton;

    protected Button resetPasswordButton;

    private StormpathLoginConfig loginConfig;

    private StormpathLoginFragmentListener loginFragmentListener;

    public static StormpathLoginFragment newInstance(Bundle configOptions) {
        StormpathLoginFragment lf = new StormpathLoginFragment();
        lf.setArguments(configOptions);
        return lf;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.stormpath_fragment_login, container, false);

        loginConfig = StormpathLoginConfig.fromBundle(getArguments());

        usernameInput = (EditText) view.findViewById(R.id.stormpath_input_username);
        passwordInput = (EditText) view.findViewById(R.id.stormpath_input_password);
        progressBar = (ProgressBar) view.findViewById(R.id.stormpath_login_progress_bar);
        loginButton = (Button) view.findViewById(R.id.stormpath_login_button);
        registerButton = (Button) view.findViewById(R.id.stormpath_register_button);
        resetPasswordButton = (Button) view.findViewById(R.id.stormpath_resetpw_button_button);

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

        resetPasswordButton.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                onForgotPasswordClicked();
            }
        });



        return view;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);

        final Activity activity = getActivity();
        if (activity instanceof StormpathLoginFragmentListener) {
            loginFragmentListener = (StormpathLoginFragmentListener) activity;
        } else {
            throw new IllegalArgumentException("Activity must implement StormpathLoginFragmentListener");
        }
    }

    protected void onLoginButtonClicked() {
        if (TextUtils.isEmpty(usernameInput.getText().toString()) || TextUtils.isEmpty(passwordInput.getText().toString())) {
            Snackbar.make(loginButton, R.string.stormpath_fill_in_credentials, Snackbar.LENGTH_SHORT).show();
            return;
        }

        showProgress();

        Stormpath.login(usernameInput.getText().toString(), passwordInput.getText().toString(), new StormpathCallback<Void>() {
            @Override
            public void onSuccess(Void aVoid) {
                hideProgress();

                loginFragmentListener.onLoginSuccess();
            }

            @Override
            public void onFailure(StormpathError error) {
                hideProgress();
                Snackbar.make(loginButton, error.message(), Snackbar.LENGTH_LONG).show();
            }
        });
    }

    protected void onRegisterButtonClicked() {
        loginFragmentListener.onRegisterClicked(usernameInput.getText().toString().trim(), passwordInput.getText().toString().trim());
    }

    protected void onForgotPasswordClicked() {
        loginFragmentListener.onForgotPasswordClicked(usernameInput.getText().toString().trim());
    }

    public void showProgress() {
        loginButton.setVisibility(View.INVISIBLE);
        progressBar.setVisibility(View.VISIBLE);
    }

    public void hideProgress() {
        loginButton.setVisibility(View.VISIBLE);
        progressBar.setVisibility(View.INVISIBLE);
    }

    public interface StormpathLoginFragmentListener {

        void onRegisterClicked(String username, String password);

        void onLoginSuccess();

        void onForgotPasswordClicked(String username);
    }
}
