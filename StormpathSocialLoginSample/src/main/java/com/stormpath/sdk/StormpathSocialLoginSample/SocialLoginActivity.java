package com.stormpath.sdk.StormpathSocialLoginSample;

import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.login.LoginManager;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;
import com.stormpath.sdk.Stormpath;
import com.stormpath.sdk.StormpathCallback;
import com.stormpath.sdk.models.SocialProviderConfiguration;
import com.stormpath.sdk.models.SocialProvidersResponse;
import com.stormpath.sdk.models.StormpathError;
import com.stormpath.sdk.providers.FacebookLoginProvider;
import com.stormpath.sdk.providers.GoogleLoginProvider;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

public class SocialLoginActivity extends AppCompatActivity implements FacebookCallback<LoginResult> {

    CallbackManager callbackManager;
    Button fbDeepLinking;
    Button googDeepLinking;

    //used to intercept the social media oauth callbacks
    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        // getIntent() should always return the most recent
        setIntent(intent);

        //check contents of intent
        if (getIntent().getData() != null && getIntent().getData().getScheme() != null) {

            if (getIntent().getData().getScheme().contentEquals(getString(R.string.facebook_app_id))) {

            }
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_social_login);

        // Don't forget to set your fb app id in strings.xml!
        callbackManager = CallbackManager.Factory.create();
        LoginButton loginButton = (LoginButton) findViewById(R.id.login_button);
        LoginManager.getInstance().registerCallback(callbackManager, this);


        fbDeepLinking = (Button)findViewById(R.id.fb_login_button_deeplink);
        fbDeepLinking.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Stormpath.socialLoginFlow(SocialLoginActivity.this, SocialProvidersResponse.FACEBOOK, new SocialProviderConfiguration(getString(R.string.facebook_app_id), getString(R.string.facebook_app_id)));

            }
        });

        googDeepLinking = (Button)findViewById(R.id.goog_login_button_deeplink);
        googDeepLinking.setOnClickListener(new View.OnClickListener(){

            @Override
            public void onClick(View v) {

                Stormpath.socialLoginFlow(SocialLoginActivity.this, SocialProvidersResponse.GOOGLE, new SocialProviderConfiguration(getString(R.string.goog_app_id), getString(R.string.goog_app_id)));

            }
        });

        //check contents of intent
        if (getIntent()!=null && getIntent().getData() != null && getIntent().getData().getScheme() != null) {

            if (getIntent().getData().getScheme().contentEquals(getString(R.string.facebook_app_id))) {

                //tokenize and get access token
                FacebookLoginProvider mFbLogin = new FacebookLoginProvider();
                final String accessToken = mFbLogin.getResponseFromCallbackURL(getIntent().getData().toString()); //can be null
                Stormpath.socialLogin(SocialProvidersResponse.FACEBOOK, accessToken,
                        new StormpathCallback<Void>() {
                            @Override
                            public void onSuccess(Void aVoid) {
                                // we are logged in via fb!
                                Toast.makeText(SocialLoginActivity.this, "Success! " + accessToken, Toast.LENGTH_LONG).show(); //should not say "Success! null"

                                //change button name to logout

                                //implement logout

                            }

                            @Override
                            public void onFailure(StormpathError error) {
                                Toast.makeText(SocialLoginActivity.this, error.message(), Toast.LENGTH_LONG).show();
                            }
                        });

            }
            else if(getIntent().getData().getScheme().contentEquals(getString(R.string.goog_app_id))){
                GoogleLoginProvider mGoogLogin = new GoogleLoginProvider();

                //get code, might require method with callback
                mGoogLogin.getResponseFromCallbackURL(getIntent().getData().toString());
            }
        }

    }

    @Override
    public void onSuccess(final LoginResult loginResult) {
        Stormpath.socialLogin(SocialProvidersResponse.FACEBOOK, loginResult.getAccessToken().getToken(),
                new StormpathCallback<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        // we are logged in via fb!
                        Toast.makeText(SocialLoginActivity.this, "Success! " + loginResult.getAccessToken().getToken(), Toast.LENGTH_LONG).show();
                    }

                    @Override
                    public void onFailure(StormpathError error) {
                        Toast.makeText(SocialLoginActivity.this, error.message(), Toast.LENGTH_LONG).show();
                    }
                });
    }

    @Override
    public void onCancel() {
        // fb login was cancelled ny the user
        Toast.makeText(SocialLoginActivity.this, "Canceled!", Toast.LENGTH_LONG).show();

    }

    @Override
    public void onError(FacebookException error) {
        // an error occurred while logging in via fb
        Toast.makeText(SocialLoginActivity.this, "Error!", Toast.LENGTH_LONG).show();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        callbackManager.onActivityResult(requestCode, resultCode, data);
    }
}
