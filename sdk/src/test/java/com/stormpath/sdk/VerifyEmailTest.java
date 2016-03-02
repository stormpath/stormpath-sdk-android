package com.stormpath.sdk;

import com.stormpath.sdk.models.StormpathError;

import org.junit.Test;

import java.net.HttpURLConnection;

import okhttp3.mockwebserver.RecordedRequest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

public class VerifyEmailTest extends BaseTest {

    @Override
    public void setUp() throws Exception {
        super.setUp();
        initWithDefaults();
    }

    @Test
    public void correctRequest() throws Exception {
        String sptoken = "OiqqtEzEHJZwrZ70SYJh8";

        enqueueEmptyResponse(HttpURLConnection.HTTP_OK);
        Stormpath.verifyEmail(sptoken, mock(StormpathCallback.class));

        RecordedRequest request = takeLastRequest();

        assertThat(request.getMethod()).isEqualTo("GET");
        assertThat(request.getPath()).isEqualTo("/verify?sptoken=OiqqtEzEHJZwrZ70SYJh8");
        assertThat(request.getHeader("Accept")).isEqualTo("application/json");
    }

    @Test
    public void successfulVerifyCallsSuccess() throws Exception {
        enqueueEmptyResponse(HttpURLConnection.HTTP_OK);
        StormpathCallback<Void> callback = mock(StormpathCallback.class);
        Stormpath.verifyEmail("OiqqtEzEHJZwrZ70SYJh8", callback);

        verify(callback).onSuccess(null);
    }

    @Test
    public void failedVerifyCallsFailure() throws Exception {
        enqueueResponse("stormpath-verify-400.json", HttpURLConnection.HTTP_BAD_REQUEST);
        StormpathCallback<Void> callback = mock(StormpathCallback.class);
        Stormpath.verifyEmail("OiqqtEzEHJZwrZ70SYJh8", callback);

        verify(callback).onFailure(any(StormpathError.class));
    }
}
