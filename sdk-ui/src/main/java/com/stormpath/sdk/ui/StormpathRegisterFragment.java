package com.stormpath.sdk.ui;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
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

public class StormpathRegisterFragment extends Fragment {

    EditText firstNameEditText;

    EditText surnameEditText;

    EditText usernameEditText;

    EditText passwordEditText;

    ProgressBar progressBar;

    Button registerButton;

    private StormpathRegisterFragmentListener mListener;

    public StormpathRegisterFragment() {
        // Required empty public constructor
    }


    public static StormpathRegisterFragment newInstance() {
        StormpathRegisterFragment fragment = new StormpathRegisterFragment();
        Bundle args = new Bundle();
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

        firstNameEditText = (EditText) view.findViewById(R.id.stormpath_input_firstname);
        surnameEditText = (EditText) view.findViewById(R.id.stormpath_input_surname);
        usernameEditText = (EditText) view.findViewById(R.id.stormpath_input_username);
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

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p/>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface StormpathRegisterFragmentListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
    }


}
