package com.stormpath.sdk.providers;

import com.stormpath.sdk.models.SocialProviderConfiguration;

import java.io.UnsupportedEncodingException;
import java.util.List;
import java.util.Map;
import java.util.Random;


/**
 * Created by ericlw on 3/29/16.
 */
public class GoogleLoginProvider extends BaseLoginProvider implements LoginProvider {

    @Override
    public String getResponseFromCallbackURL(String url) {

        if(url.contains("error")){

            //do nothing because the user never started
            // the login process in the first place. Error is always because
            // people cancelled the FB login according to https://developers.facebook.com/docs/facebook-login/manually-build-a-login-flow

        }
        Map<String, List<String>> mMap = null;
        try {
           mMap = dictionaryFromFormEncodedString(url);
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }

        return mMap.get("access_token").get(0);
    }

    @Override
    public String authenticationRequestURL(SocialProviderConfiguration application) {

        Random state = new Random(10000000);

        String scopes = application.scopes != null ? application.scopes : "email profile";

        String queryString =  "response_type=code&scope=" + scopes + "&redirect_uri=" + application.urlScheme + "://oauth2callback&client_id="
                + application.appId + "&verifier=" + Math.abs(state.nextInt());
        return "https://accounts.google.com/o/oauth2/auth?" + queryString;

    }
}
