package com.stormpath.sdk;

import com.stormpath.sdk.models.AccountStore;
import com.stormpath.sdk.models.LoginModel;
import com.stormpath.sdk.models.StormpathError;

import org.junit.Test;

import java.net.HttpURLConnection;

import okhttp3.mockwebserver.RecordedRequest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.stub;
import static org.mockito.Mockito.verify;

public class SocialProvidersTest extends BaseTest {

    @Override
    public void setUp() throws Exception {
        super.setUp();
        initWithDefaults();
    }

    @Test
    public void correctRequest() throws Exception {
        enqueueResponse("stormpath-social-providers-response.json");
        Stormpath.apiManager.getLoginModel(mock(StormpathCallback.class));

        RecordedRequest request = takeLastRequest();

        assertThat(request.getMethod()).isEqualTo("GET");
        assertThat(request.getPath()).isEqualTo("/spa-config");
        assertThat(request.getHeader("Accept")).isEqualTo("application/json");
    }

    @Test
    public void successfulFetchCallsSuccess() throws Exception {
        enqueueResponse("stormpath-social-providers-response.json");
        StormpathCallback<LoginModel> callback = mock(StormpathCallback.class);
        Stormpath.apiManager.getLoginModel(callback);

        verify(callback).onSuccess(any(LoginModel.class));
    }

    @Test
    public void successfulFetchReturnsCorrectData() throws Exception {
        stub(mockPlatform().preferenceStore().getAccessToken()).toReturn("abcdefghijklmnopqrstuvwyxz0123456789");

        SocialProvidersCallback callback = new SocialProvidersCallback();
        enqueueResponse("stormpath-social-providers-response.json");

        Stormpath.apiManager.getLoginModel(callback);
        assertThat(callback.loginModel.getAccountStores()).hasSize(3);
        AccountStore facebook = callback.loginModel.getAccountStores().get(0);
        AccountStore google = callback.loginModel.getAccountStores().get(1);
        AccountStore linkedin = callback.loginModel.getAccountStores().get(2);

        assertThat(facebook.getProviderId()).isEqualTo("facebook");
        assertThat(facebook.getHref()).isEqualTo("1234567890123456");

        assertThat(google.getProviderId()).isEqualTo("google");
        assertThat(google.getHref()).isEqualTo("123456789012-abcdefghijklmnopqrstuwvxyz123456.apps.googleusercontent.com");

        assertThat(linkedin.getProviderId()).isEqualTo("linkedin");
        assertThat(linkedin.getHref()).isEqualTo("abcdef12345678");
    }

    @Test
    public void failedFetchCallsFailure() throws Exception {
        enqueueEmptyResponse(HttpURLConnection.HTTP_BAD_REQUEST);
        StormpathCallback<LoginModel> callback = mock(StormpathCallback.class);
        Stormpath.apiManager.getLoginModel(callback);

        verify(callback).onFailure(any(StormpathError.class));
    }

    @Test
    public void failedDeserializationCallsFailure() throws Exception {
        enqueueEmptyResponse(HttpURLConnection.HTTP_OK);
        StormpathCallback<LoginModel> callback = mock(StormpathCallback.class);
        Stormpath.apiManager.getLoginModel(callback);

        verify(callback).onFailure(any(StormpathError.class));
    }

    private static class SocialProvidersCallback implements StormpathCallback<LoginModel> {

        public LoginModel loginModel;

        @Override
        public void onSuccess(LoginModel loginModel) {
            this.loginModel = loginModel;
        }

        @Override
        public void onFailure(StormpathError error) {
            throw new RuntimeException("onFailure was called but it shouldn't have been!");
        }
    }
}
