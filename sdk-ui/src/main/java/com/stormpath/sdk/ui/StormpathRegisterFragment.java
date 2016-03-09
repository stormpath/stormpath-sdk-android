package com.stormpath.sdk.ui;

import android.annotation.TargetApi;
import android.content.Context;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.stormpath.sdk.Stormpath;
import com.stormpath.sdk.StormpathCallback;
import com.stormpath.sdk.models.RegisterParams;
import com.stormpath.sdk.models.StormpathError;

public class StormpathRegisterFragment extends BaseFragment {


    private StormpathRegisterFragmentListener mListener;

    public StormpathRegisterFragment() {
        // Required empty public constructor
    }


    public static StormpathRegisterFragment newInstance(Bundle args) {
        StormpathRegisterFragment fragment = new StormpathRegisterFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {


        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.stormpath_fragment_register, container, false);

        loginConfig = StormpathLoginConfig.fromBundle(getArguments());

        firstNameEditText = (EditText) view.findViewById(R.id.stormpath_input_firstname);
        surnameEditText = (EditText) view.findViewById(R.id.stormpath_input_surname);
        emailEditText = (EditText) view.findViewById(R.id.stormpath_input_email);

        emailEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @TargetApi(8)
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

                //do email validation
                if(android.os.Build.VERSION.SDK_INT >= Build.VERSION_CODES.FROYO) {

                    if (android.util.Patterns.EMAIL_ADDRESS.matcher(s).matches()) {

                        emailEditText.getBackground().clearColorFilter();

                    }
                    else{
                        //set underline color
                        emailEditText.getBackground().setColorFilter(Color.RED, PorterDuff.Mode.SRC_IN);
                    }
                }


            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        passwordEditText = (EditText) view.findViewById(R.id.stormpath_input_password);
        progressBar = (ProgressBar) view.findViewById(R.id.stormpath_register_progress_bar);
        registerButton = (Button) view.findViewById(R.id.stormpath_register_button);
        registerButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onRegisterButtonClicked();
            }
        });

        return view;
    }

    protected void onRegisterButtonClicked() {
        if (TextUtils.isEmpty(firstNameEditText.getText().toString())
                || TextUtils.isEmpty(surnameEditText.getText().toString())
                || TextUtils.isEmpty(emailEditText.getText().toString())
                || TextUtils.isEmpty(passwordEditText.getText().toString())) {
            Snackbar.make(registerButton, R.string.stormpath_all_fields_mandatory, Snackbar.LENGTH_SHORT).show();
            return;
        }

        if(android.os.Build.VERSION.SDK_INT >= Build.VERSION_CODES.FROYO) {

            if (!android.util.Patterns.EMAIL_ADDRESS.matcher(emailEditText.getText().toString()).matches()) {
                Snackbar.make(registerButton, R.string.stormpath_requires_valid_email, Snackbar.LENGTH_SHORT).show();
            }
        }

        Stormpath.register(new RegisterParams(firstNameEditText.getText().toString(), surnameEditText.getText().toString(),
                emailEditText.getText().toString(), passwordEditText.getText().toString()), new StormpathCallback<Void>() {
            @Override
            public void onSuccess(Void aVoid) {
                hideProgress();
            }

            @Override
            public void onFailure(StormpathError error) {
                hideProgress();
                Snackbar.make(registerButton, error.message(), Snackbar.LENGTH_LONG).show();
            }
        });

        showProgress();
    }

    public void showProgress() {
        registerButton.setVisibility(View.INVISIBLE);
        progressBar.setVisibility(View.VISIBLE);
    }

    public void hideProgress() {
        registerButton.setVisibility(View.VISIBLE);
        progressBar.setVisibility(View.INVISIBLE);
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof StormpathRegisterFragmentListener) {
            mListener = (StormpathRegisterFragmentListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement StormpathRegisterFragmentListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    public interface StormpathRegisterFragmentListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
    }


}
