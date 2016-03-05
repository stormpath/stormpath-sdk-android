package com.stormpath.sdk.ui;

import android.content.Context;
import android.net.Uri;
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

import com.stormpath.sdk.Stormpath;
import com.stormpath.sdk.StormpathCallback;
import com.stormpath.sdk.models.RegisterParams;
import com.stormpath.sdk.models.StormpathError;

public class StormpathResetPasswordFragment extends BaseFragment {

    EditText emailEditText;

    ProgressBar progressBar;

    Button sendButton;

    private StormpathResetPasswordFragmentListener mListener;

    public StormpathResetPasswordFragment() {
        // Required empty public constructor
    }


    public static StormpathResetPasswordFragment newInstance(String username) {
        StormpathResetPasswordFragment fragment = new StormpathResetPasswordFragment();
        Bundle args = new Bundle();

        args.putString("username", username);

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

        View view = inflater.inflate(R.layout.stormpath_fragment_resetpassword, container, false);

        emailEditText = (EditText) view.findViewById(R.id.stormpath_input_username);

        Bundle localBundle = getArguments();
        String username = null;
        if(localBundle!=null)
             username = localBundle.getString("username");

        if(username!=null)
            emailEditText.setText(username);

        progressBar = (ProgressBar) view.findViewById(R.id.stormpath_resetpw_progress_bar);
        sendButton = (Button) view.findViewById(R.id.stormpath_resetpw_button);
        sendButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onSend();
            }
        });

        return view;
    }

    protected void onSend() {
        if (TextUtils.isEmpty(emailEditText.getText().toString())) { //check for valid email
            Snackbar.make(sendButton, R.string.stormpath_all_fields_mandatory, Snackbar.LENGTH_SHORT).show();
            return;
        }

        Stormpath.resetPassword(emailEditText.getText().toString(), new StormpathCallback<Void>() {
            @Override
            public void onSuccess(Void aVoid) {
                hideProgress();
                Snackbar.make(sendButton, getString(R.string.stormpath_resetpassword_result), Snackbar.LENGTH_LONG).show();
            }

            @Override
            public void onFailure(StormpathError error) {
                hideProgress();
                Snackbar.make(sendButton, error.message(), Snackbar.LENGTH_LONG).show();
            }
        });

        showProgress();
    }

    public void showProgress() {
        sendButton.setVisibility(View.INVISIBLE);
        progressBar.setVisibility(View.VISIBLE);
    }

    public void hideProgress() {
        sendButton.setVisibility(View.VISIBLE);
        progressBar.setVisibility(View.INVISIBLE);
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof StormpathResetPasswordFragmentListener) {
            mListener = (StormpathResetPasswordFragmentListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement StormpathResetPasswordFragmentListener");
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
    public interface StormpathResetPasswordFragmentListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
    }


}
