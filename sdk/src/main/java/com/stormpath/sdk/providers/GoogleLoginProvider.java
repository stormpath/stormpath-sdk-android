package com.stormpath.sdk.providers;

import com.stormpath.sdk.ApiManager;
import com.stormpath.sdk.PreferenceStore;
import com.stormpath.sdk.Stormpath;
import com.stormpath.sdk.StormpathCallback;
import com.stormpath.sdk.models.SessionTokens;
import com.stormpath.sdk.models.SocialProviderConfiguration;
import com.stormpath.sdk.models.SocialProviderConfigurationSingleton;
import com.stormpath.sdk.models.StormpathError;
import com.stormpath.sdk.utils.StringUtils;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.StringTokenizer;

import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;


/**
 * Created by ericlw on 3/29/16.
 */
public class GoogleLoginProvider extends BaseLoginProvider implements LoginProvider {

    @Override
    public void getResponseFromCallbackURL(String url, final StormpathCallback<String> callback) {

        if(url.contains("error")){

            //do nothing because the user never started
            // the login process in the first place. Error is always because
            // people cancelled the FB login according to https://developers.facebook.com/docs/facebook-login/manually-build-a-login-flow
            callback.onFailure(new StormpathError("Unknown Error",
                    new IllegalStateException("no code or access_token was not found, did you forget to login? See debug logs for details.")));
        }

        //restore application, or use singleton

        Map<String, List<String>> mMap = null;
        try {
           mMap = dictionaryFromFormEncodedString(url);
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }

        if(!mMap.containsKey(SocialProviderConfigurationSingleton.getInstance().urlScheme + ":/oauth2callback" + "?code"))
        {
            callback.onFailure(new StormpathError("Unknown Error",
                    new IllegalStateException("no code or access_token was not found, did you forget to login? See debug logs for details.")));
        }
        else {

            String authorizationCode = mMap.get(SocialProviderConfigurationSingleton.getInstance().urlScheme + ":/oauth2callback" + "?code").get(0);


            Stormpath.socialGoogleCodeAuth(authorizationCode, SocialProviderConfigurationSingleton.getInstance(), new StormpathCallback<String>() {
                @Override
                public void onSuccess(String response) {

                    //need the value back
                    JSONObject responseJson = null;
                    String accessToken = null;
                    try {
                        responseJson =  new JSONObject(response);
                        accessToken = responseJson.getString("access_token");
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }


                    callback.onSuccess(accessToken);

                }

                @Override
                public void onFailure(StormpathError error) {

                    callback.onFailure(error);

                }
            });

        }

        //return mMap.get("access_token").get(0);
    }

    @Override
    public String authenticationRequestURL(SocialProviderConfiguration application) {

        //store socialproviderconfiguration
        SocialProviderConfigurationSingleton.getInstance().appId = application.appId;
        SocialProviderConfigurationSingleton.getInstance().urlScheme = application.urlScheme;
        SocialProviderConfigurationSingleton.getInstance().scopes = application.scopes;

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
    }

}
