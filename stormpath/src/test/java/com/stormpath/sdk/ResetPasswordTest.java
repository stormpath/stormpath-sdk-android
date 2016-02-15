package com.stormpath.sdk;

import org.junit.Test;

import java.net.HttpURLConnection;

import okhttp3.mockwebserver.RecordedRequest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

public class ResetPasswordTest extends BaseTest {

    @Override
    public void setUp() throws Exception {
        super.setUp();
        initWithDefaults();
    }

    @Test
    public void correctRequest() throws Exception {
        enqueueEmptyResponse(HttpURLConnection.HTTP_OK);
        Stormpath.resetPassword("john.deere@example.com", mock(StormpathCallback.class));

        RecordedRequest request = takeLastRequest();

        assertThat(request.getMethod()).isEqualTo("POST");
        assertThat(request.getPath()).isEqualTo("/forgot");
        assertThat(request.getHeader("Accept")).isEqualTo("application/json");
        assertThat(request.getBody().readUtf8()).isEqualTo("{\"email\":\"john.deere@example.com\"}");
    }

    @Test
    public void successfulResetCallsSuccess() throws Exception {
        enqueueEmptyResponse(HttpURLConnection.HTTP_OK);
        StormpathCallback<Void> callback = mock(StormpathCallback.class);
        Stormpath.resetPassword("john.deere@example.com", callback);

        verify(callback).onSuccess(null);
    }

    @Test
    public void failedResetCallsFailure() throws Exception {
        enqueueEmptyResponse(HttpURLConnection.HTTP_BAD_REQUEST);
        StormpathCallback<Void> callback = mock(StormpathCallback.class);
        Stormpath.resetPassword("john.deere@example.com", callback);

        verify(callback).onFailure(any(Throwable.class));
    }
}
