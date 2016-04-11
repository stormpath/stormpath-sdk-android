package com.stormpath.sdk.providers;

import com.stormpath.sdk.models.SocialProviderConfiguration;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.StringTokenizer;


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

        StringTokenizer multiTokenizer = new StringTokenizer(application.appId, ".");
        int tokens = multiTokenizer.countTokens();
        ArrayList<String> tokenArray = new ArrayList<String>();
        while(multiTokenizer.hasMoreTokens()){
            tokenArray.add(multiTokenizer.nextToken());
        }

        String clientId = "";
        for(int i = tokenArray.size()-1; i > -1; i--) {
            if(i != tokenArray.size()-1){
                clientId = clientId + "." + tokenArray.get(i);
            }
            else
            {
                clientId = clientId + tokenArray.get(i);
            }

        }

        String queryString =  "response_type=code&scope=" + scopes + "&redirect_uri=" + application.urlScheme + ":/oauth2callback&client_id="
                + clientId + "&verifier=" + Math.abs(state.nextInt());
        return "https://accounts.google.com/o/oauth2/auth?" + queryString;
// android - https://accounts.google.com/o/oauth2/auth?response_type=code&scope=email profile&redirect_uri=com.googleusercontent.apps.120814890096-1dt9ac0f83eng66troavm0a6dt9dgsp4:/oauth2callback&client_id=com.googleusercontent.apps.120814890096-1dt9ac0f83eng66troavm0a6dt9dgsp4&verifier=1975323259
// ios - https://accounts.google.com/o/oauth2/auth?response_type=code&scope=email%20profile&redirect_uri=com.googleusercontent.apps.120814890096-1dt9ac0f83eng66troavm0a6dt9dgsp4:/oauth2callback&client_id=120814890096-1dt9ac0f83eng66troavm0a6dt9dgsp4.apps.googleusercontent.com&verifier=4888242
    }
}
