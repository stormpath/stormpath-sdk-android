package com.stormpath.sdk.StormpathAndroidExample;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.stormpath.sdk.Stormpath;
import com.stormpath.sdk.StormpathCallback;
import com.stormpath.sdk.models.RegistrationForm;
import com.stormpath.sdk.models.StormpathError;

/**
 * Created by edjiang on 1/6/17.
 */
public class RegisterActivity extends AppCompatActivity implements View.OnClickListener {
    private Button registerButton;
    private EditText firstNameInput;
    private EditText lastNameInput;
    private EditText emailInput;
    private EditText passwordInput;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        registerButton = (Button) findViewById(R.id.button_register);
        firstNameInput = (EditText) findViewById(R.id.input_firstName);
        lastNameInput = (EditText) findViewById(R.id.input_lastName);
        emailInput = (EditText) findViewById(R.id.input_email);
        passwordInput = (EditText) findViewById(R.id.input_password);

        registerButton.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        RegistrationForm registrationData = new RegistrationForm(emailInput.getText().toString(), passwordInput.getText().toString());
        registrationData.setGivenName(firstNameInput.getText().toString())
                .setSurname(lastNameInput.getText().toString());
        Stormpath.register(registrationData, new StormpathCallback<Void>() {
            @Override
            public void onSuccess(Void aVoid) {
                finish();
            }

            @Override
            public void onFailure(StormpathError error) {
                new AlertDialog.Builder(RegisterActivity.this)
                        .setTitle("Error")
                        .setMessage(error.message())
                        .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {

                            }
                        })
                        .show();
            }
        });
    }
}
