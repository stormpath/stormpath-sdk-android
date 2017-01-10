package com.stormpath.sdk.StormpathLoginBasic;

import com.stormpath.sdk.Stormpath;
import com.stormpath.sdk.StormpathCallback;
import com.stormpath.sdk.models.Account;
import com.stormpath.sdk.models.StormpathError;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        final TextView userProfileText = (TextView) findViewById(R.id.user_profile_text);

        Stormpath.getUserProfile(new StormpathCallback<Account>() {
            @Override
            public void onSuccess(Account account) {
                String profileInfoText = String
                        .format("%s\n%s\n%s\n%s", account.getHref(), account.getFullName(), account.getUsername(), account.getStatus());
                userProfileText.setText(profileInfoText);
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
    }

    protected void onLogoutClicked() {
        Stormpath.logout();
        startActivity(new Intent(this, LoginActivity.class));
        finish();
    }
}
