package com.stormpath.sdk;

import org.junit.Test;

import java.net.HttpURLConnection;

import static org.mockito.Matchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

public class LoginTest extends BaseTest {

    @Override
    public void setUp() throws Exception {
        super.setUp();
        initWithDefaults();
    }

    @Test
    public void successfulLoginSavesTokens() throws Exception {
        enqueueResponse("stormpath-login-response.json");
        Stormpath.login("user", "pass", mock(StormpathCallback.class));

        verify(mockPlatform().preferenceStore()).setAccessToken("eyJraWQiOiI2WjA3NEJBQzhTM0tGWE5KOVhFTldEVUhGIiwiYWxnIjoiSFMyNTYifQ.eyJqdGk"
                + "iOiIzQjlJQmJGR0FMTWd5MTRER1V6NXpZIiwiaWF0IjoxNDU1MjE4Mzg0LCJpc3MiOiJodHRwczovL2FwaS5zdG9ybXBhdGguY29tL3YxL2FwcGxpY2F0aW9"
                + "ucy8yUnJxS25UaW91M1F3Tm50RTN0Y0VHIiwic3ViIjoiaHR0cHM6Ly9hcGkuc3Rvcm1wYXRoLmNvbS92MS9hY2NvdW50cy81TXNRVEE0UVI5c0hZNnlRQlF"
                + "FSXlLIiwiZXhwIjoxNDU1MjIxOTg0LCJydGkiOiIzQjlEclh4NG9RdFR6VWU0dFhCR0d5In0.DzSz2LlBa40vBTwxhk5K_A1foKsUy-FZRU8SV9JPDTc");
        verify(mockPlatform().preferenceStore()).setRefreshToken(
                "eyJraWQiOiI2WjA3NEJBQzhTM0tGWE5KOVhFTldEVUhGIiwiYWxnIjoiSFMyNTYifQ.eyJqdGkiOiIzQjlEclh4NG9RdFR6VWU0dFhCR0d5IiwiaWF0IjoxNDU"
                        + "1MjE4Mzg0LCJpc3MiOiJodHRwczovL2FwaS5zdG9ybXBhdGguY29tL3YxL2FwcGxpY2F0aW9ucy8yUnJxS25UaW91M1F3Tm50RTN0Y0VHIiwic3V"
                        + "iIjoiaHR0cHM6Ly9hcGkuc3Rvcm1wYXRoLmNvbS92MS9hY2NvdW50cy81TXNRVEE0UVI5c0hZNnlRQlFFSXlLIiwiZXhwIjoxNDYwNDAyMzg0fQ."
                        + "ltFz1735IRnD0vIgdeKC4oInxe8K0KzhDJ95RgRuqmA");
    }

    @Test
    public void successfulLoginCallsSuccessWithToken() throws Exception {
        enqueueResponse("stormpath-login-response.json");
        StormpathCallback<String> callback = mock(StormpathCallback.class);
        Stormpath.login("user", "pass", callback);

        verify(callback).onSuccess(
                "eyJraWQiOiI2WjA3NEJBQzhTM0tGWE5KOVhFTldEVUhGIiwiYWxnIjoiSFMyNTYifQ.eyJqdGkiOiIzQjlJQmJGR0FMTWd5MTRER1V6NXpZIiwiaWF0IjoxNDU"
                        + "1MjE4Mzg0LCJpc3MiOiJodHRwczovL2FwaS5zdG9ybXBhdGguY29tL3YxL2FwcGxpY2F0aW9ucy8yUnJxS25UaW91M1F3Tm50RTN0Y0VHIiwic3V"
                        + "iIjoiaHR0cHM6Ly9hcGkuc3Rvcm1wYXRoLmNvbS92MS9hY2NvdW50cy81TXNRVEE0UVI5c0hZNnlRQlFFSXlLIiwiZXhwIjoxNDU1MjIxOTg0LCJ"
                        + "ydGkiOiIzQjlEclh4NG9RdFR6VWU0dFhCR0d5In0.DzSz2LlBa40vBTwxhk5K_A1foKsUy-FZRU8SV9JPDTc");
    }

    @Test
    public void failedLoginCallsFailure() throws Exception {
        enqueueResponse("stormpath-login-400.json", HttpURLConnection.HTTP_BAD_REQUEST);
        StormpathCallback<String> callback = mock(StormpathCallback.class);
        Stormpath.login("user", "pass", callback);

        verify(callback).onFailure((Throwable) any());
    }

    @Test
    public void failedDeserializationCallsFailure() throws Exception {
        enqueueEmptyResponse(HttpURLConnection.HTTP_OK);
        StormpathCallback<String> callback = mock(StormpathCallback.class);
        Stormpath.login("user", "pass", callback);

        verify(callback).onFailure((Throwable) any());
    }

    @Test
    public void missingAccessTokenCallsFailure() throws Exception {
        enqueueStringResponse("{}");
        StormpathCallback<String> callback = mock(StormpathCallback.class);
        Stormpath.login("user", "pass", callback);

        verify(callback).onFailure((Throwable) any());
    }
}
