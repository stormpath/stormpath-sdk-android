package com.stormpath.sdk.providers;

import com.stormpath.sdk.StormpathCallback;
import com.stormpath.sdk.models.SocialProviderConfiguration;
import com.stormpath.sdk.models.StormpathError;

import java.io.UnsupportedEncodingException;
import java.net.URL;
import java.net.URLEncoder;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;


/**
 * Created by ericlw on 3/29/16.
 */
public class FacebookLoginProvider extends BaseLoginProvider implements LoginProvider {


    @Override
    public void getResponseFromCallbackURL(String url, StormpathCallback<String> callback) {

        if(url.contains("error")){

            // do nothing because the user never started
            // the login process in the first place. Error is always because
            // people cancelled the FB login according to https://developers.facebook.com/docs/facebook-login/manually-build-a-login-flow
            callback.onFailure(new StormpathError("Unknown Error",
                    new IllegalStateException("access_token was not found, did you forget to login? See debug logs for details.")));
        }
        Map<String, List<String>> mMap = null;
        try {
           mMap = dictionaryFromFormEncodedString(url);
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }

        callback.onSuccess(mMap.get("access_token").get(0));

    }

    @Override
    public String authenticationRequestURL(SocialProviderConfiguration application) {

        Random state = new Random(10000000);

        String scopes = application.scopes != null ? application.scopes : "email";

        String queryString = "client_id=" + application.appId.substring(2) + "&redirect_uri=" + application.urlScheme
                + "://authorize&response_type=token&scope=" + scopes + "&state=" + Math.abs(state.nextInt()) + "&auth_type=rerequest";


        return "https://www.facebook.com/dialog/oauth?" + queryString;
    }
}
