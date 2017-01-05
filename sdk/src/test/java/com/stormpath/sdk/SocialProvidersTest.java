package com.stormpath.sdk;

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
        Stormpath.getSocialProviders(mock(StormpathCallback.class));

        RecordedRequest request = takeLastRequest();

        assertThat(request.getMethod()).isEqualTo("GET");
        assertThat(request.getPath()).isEqualTo("/spa-config");
        assertThat(request.getHeader("Accept")).isEqualTo("application/json");
    }

    @Test
    public void successfulFetchCallsSuccess() throws Exception {
        enqueueResponse("stormpath-social-providers-response.json");
        StormpathCallback<SocialProvidersResponse> callback = mock(StormpathCallback.class);
        Stormpath.getSocialProviders(callback);

        verify(callback).onSuccess(any(SocialProvidersResponse.class));
    }

    @Test
    public void successfulFetchReturnsCorrectData() throws Exception {
        stub(mockPlatform().preferenceStore().getAccessToken()).toReturn("abcdefghijklmnopqrstuvwyxz0123456789");

        SocialProvidersCallback callback = new SocialProvidersCallback();
        enqueueResponse("stormpath-social-providers-response.json");

        Stormpath.getSocialProviders(callback);
        assertThat(callback.socialProvidersResponse.getSocialProviders()).hasSize(3);
        SocialProvidersResponse.SocialProvider facebook = callback.socialProvidersResponse.getSocialProviders().get(0);
        SocialProvidersResponse.SocialProvider google = callback.socialProvidersResponse.getSocialProviders().get(1);
        SocialProvidersResponse.SocialProvider linkedin = callback.socialProvidersResponse.getSocialProviders().get(2);

        assertThat(facebook.providerId()).isEqualTo("facebook");
        assertThat(facebook.clientId()).isEqualTo("1234567890123456");
        assertThat(facebook.isEnabled()).isEqualTo(true);

        assertThat(google.providerId()).isEqualTo("google");
        assertThat(google.clientId()).isEqualTo("123456789012-abcdefghijklmnopqrstuwvxyz123456.apps.googleusercontent.com");
        assertThat(google.isEnabled()).isEqualTo(true);

        assertThat(linkedin.providerId()).isEqualTo("linkedin");
        assertThat(linkedin.clientId()).isEqualTo("abcdef12345678");
        assertThat(linkedin.isEnabled()).isEqualTo(false);
    }

    @Test
    public void failedFetchCallsFailure() throws Exception {
        enqueueEmptyResponse(HttpURLConnection.HTTP_BAD_REQUEST);
        StormpathCallback<SocialProvidersResponse> callback = mock(StormpathCallback.class);
        Stormpath.getSocialProviders(callback);

        verify(callback).onFailure(any(StormpathError.class));
    }

    @Test
    public void failedDeserializationCallsFailure() throws Exception {
        enqueueEmptyResponse(HttpURLConnection.HTTP_OK);
        StormpathCallback<SocialProvidersResponse> callback = mock(StormpathCallback.class);
        Stormpath.getSocialProviders(callback);

        verify(callback).onFailure(any(StormpathError.class));
    }

    private static class SocialProvidersCallback implements StormpathCallback<SocialProvidersResponse> {

        public SocialProvidersResponse socialProvidersResponse;

        @Override
        public void onSuccess(SocialProvidersResponse socialProvidersResponse) {
            this.socialProvidersResponse = socialProvidersResponse;
        }

        @Override
        public void onFailure(StormpathError error) {
            throw new RuntimeException("onFailure was called but it shouldn't have been!");
        }
    }
}
