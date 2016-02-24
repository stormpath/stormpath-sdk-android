package com.stormpath.sdk;

import com.stormpath.sdk.models.StormpathError;
import com.stormpath.sdk.utils.StormpathTestCallback;

import org.junit.Test;

import java.net.HttpURLConnection;

import okhttp3.mockwebserver.RecordedRequest;

import static org.assertj.core.api.Assertions.assertThat;
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
    public void correctRequest() throws Exception {
        String user = "testUserName";
        String pass = "testPass0&";

        enqueueResponse("stormpath-login-response.json");
        Stormpath.login(user, pass, mock(StormpathCallback.class));

        RecordedRequest request = takeLastRequest();

        assertThat(request.getMethod()).isEqualTo("POST");
        assertThat(request.getPath()).isEqualTo("/oauth/token");
        assertThat(request.getHeader("Accept")).isEqualTo("application/json");
        assertThat(request.getHeader("Content-Type")).isEqualTo("application/x-www-form-urlencoded");
        assertThat(request.getBody().readUtf8()).isEqualTo("username=" + user + "&password=" + "testPass0%26" + "&grant_type=password");
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
    public void successfulLoginCallsSuccess() throws Exception {
        enqueueResponse("stormpath-login-response.json");
        StormpathCallback<Void> callback = mock(StormpathCallback.class);
        Stormpath.login("user", "pass", callback);

        verify(callback).onSuccess(null);
    }

    @Test
    public void failedLoginCallsFailure() throws Exception {
        enqueueResponse("stormpath-login-400.json", HttpURLConnection.HTTP_BAD_REQUEST);
        StormpathTestCallback<Void> callback = new StormpathTestCallback<>();
        Stormpath.login("user", "pass", callback);

        assertThat(callback.error.code()).isEqualTo(7100);
        assertThat(callback.error.status()).isEqualTo(400);
        assertThat(callback.error.developerMessage()).isEqualTo("Login attempt failed because the specified password is incorrect.");
        assertThat(callback.error.message()).isEqualTo("Invalid username or password.");
        assertThat(callback.error.moreInfo()).isEqualTo("http://docs.stormpath.com/errors/7100");
    }

    @Test
    public void failedDeserializationCallsFailure() throws Exception {
        enqueueEmptyResponse(HttpURLConnection.HTTP_OK);
        StormpathTestCallback<Void> callback = new StormpathTestCallback<>();
        Stormpath.login("user", "pass", callback);

        assertThat(callback.error.message()).isEqualTo("There was an unexpected error, please try again later.");
        assertThat(callback.error.throwable()).isNotNull();
    }

    @Test
    public void missingAccessTokenCallsFailure() throws Exception {
        enqueueStringResponse("{}");
        StormpathCallback<Void> callback = mock(StormpathCallback.class);
        Stormpath.login("user", "pass", callback);

        verify(callback).onFailure(any(StormpathError.class));
    }
}
