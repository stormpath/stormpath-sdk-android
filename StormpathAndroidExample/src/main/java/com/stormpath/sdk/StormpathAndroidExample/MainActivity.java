package com.stormpath.sdk.StormpathAndroidExample;

import com.stormpath.sdk.Stormpath;
import com.stormpath.sdk.StormpathCallback;
import com.stormpath.sdk.models.Account;
import com.stormpath.sdk.models.StormpathError;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        final TextView welcomeTextView = (TextView) findViewById(R.id.tv_welcome);
        final TextView profileTextView = (TextView) findViewById(R.id.tv_profile);
        final EditText accessTokenEditText = (EditText) findViewById(R.id.input_access_token);
        final EditText refreshTokenEditText = (EditText) findViewById(R.id.input_refresh_token);

        Stormpath.getAccount(new StormpathCallback<Account>() {
            @Override
            public void onSuccess(Account account) {
                String profileInfoText = "Email: " + account.getEmail() + "\n" +
                        "Username: " + account.getUsername() + "\n" +
                        "Href: " + account.getHref();

                profileTextView.setText(profileInfoText);
                welcomeTextView.setText("Welcome, " + account.getGivenName());

                accessTokenEditText.setText(Stormpath.getAccessToken());
                refreshTokenEditText.setText(Stormpath.getRefreshToken());
            }

            @Override
            public void onFailure(StormpathError error) {
                Toast.makeText(MainActivity.this, error.message(), Toast.LENGTH_LONG).show();
            }
        });

        findViewById(R.id.logout_button).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onLogoutClicked();
            }
        });


        findViewById(R.id.button_refresh_token).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Stormpath.refreshAccessToken(new StormpathCallback<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        ((EditText) findViewById(R.id.input_access_token)).setText(Stormpath.getAccessToken());
                    }

                    @Override
                    public void onFailure(StormpathError error) {

                    }
                });
            }
        });
    }

    protected void onLogoutClicked() {
        Stormpath.logout();
        startActivity(new Intent(this, LoginActivity.class));
        finish();
    }
}
