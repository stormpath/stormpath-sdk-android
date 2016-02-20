package com.stormpath.sdk;

import org.junit.Test;

import java.net.HttpURLConnection;

import okhttp3.mockwebserver.RecordedRequest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

public class ResendVerificationEmailTest extends BaseTest {

    @Override
    public void setUp() throws Exception {
        super.setUp();
        initWithDefaults();
    }

    @Test
    public void correctRequest() throws Exception {
        String email = "john.deere@example.com";

        enqueueEmptyResponse(HttpURLConnection.HTTP_OK);
        Stormpath.resendVerificationEmail(email, mock(StormpathCallback.class));

        RecordedRequest request = takeLastRequest();

        assertThat(request.getMethod()).isEqualTo("POST");
        assertThat(request.getPath()).isEqualTo("/verify");
        assertThat(request.getHeader("Accept")).isEqualTo("application/json");
        assertThat(request.getHeader("Content-Type")).isEqualTo("application/x-www-form-urlencoded");
        assertThat(request.getBody().readUtf8()).isEqualTo("login=john.deere%40example.com");
    }

    @Test
    public void successfulResendCallsSuccess() throws Exception {
        enqueueEmptyResponse(HttpURLConnection.HTTP_OK);
        StormpathCallback<Void> callback = mock(StormpathCallback.class);
        Stormpath.resendVerificationEmail("john.deere@example.com", callback);

        verify(callback).onSuccess(null);
    }

    @Test
    public void failedResendCallsFailure() throws Exception {
        enqueueEmptyResponse(HttpURLConnection.HTTP_BAD_REQUEST);
        StormpathCallback<Void> callback = mock(StormpathCallback.class);
        Stormpath.resendVerificationEmail("john.deere@example.com", callback);

        verify(callback).onFailure(any(Throwable.class));
    }
}
