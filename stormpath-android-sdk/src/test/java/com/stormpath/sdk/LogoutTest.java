package com.stormpath.sdk;

import org.junit.Test;

import java.net.HttpURLConnection;

import okhttp3.mockwebserver.RecordedRequest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.stub;
import static org.mockito.Mockito.verify;

public class LogoutTest extends BaseTest {

    @Override
    public void setUp() throws Exception {
        super.setUp();
        initWithDefaults();
    }

    @Test
    public void correctRequest() throws Exception {
        String accessToken = "eyJraWQiOiI2WjA3NEJBQzhTM0tGWE5KOVhFTldEVUhGIiwiYWxnIjoiSFMyNTYifQ.eyJqdGkiOiIzQjlEclh4NG9RdFR6VWU0dFhCR0d5I"
                + "iwiaWF0IjoxNDU1MjE4Mzg0LCJpc3MiOiJodHRwczovL2FwaS5zdG9ybXBhdGguY29tL3YxL2FwcGxpY2F0aW9ucy8yUnJxS25UaW91M1F3Tm50RTN0Y0VHI"
                + "iwic3ViIjoiaHR0cHM6Ly9hcGkuc3Rvcm1wYXRoLmNvbS92MS9hY2NvdW50cy81TXNRVEE0UVI5c0hZNnlRQlFFSXlLIiwiZXhwIjoxNDYwNDAyMzg0fQ.lt"
                + "Fz1735IRnD0vIgdeKC4oInxe8K0KzhDJ95RgRuqmA";
        stub(mockPlatform().preferenceStore().getAccessToken()).toReturn(accessToken);

        enqueueEmptyResponse(HttpURLConnection.HTTP_OK);
        Stormpath.logout();

        RecordedRequest request = takeLastRequest();

        assertThat(request.getMethod()).isEqualTo("GET");
        assertThat(request.getPath()).isEqualTo("/logout");
        assertThat(request.getHeader("Authorization")).isEqualTo("Bearer " + accessToken);
        assertThat(request.getHeader("Accept")).isEqualTo("application/json");
    }

    @Test
    public void successfulLogoutDeletesTokens() throws Exception {
        stub(mockPlatform().preferenceStore().getAccessToken()).toReturn("abcdefghijklmnopqrstuvwyxz0123456789");

        enqueueEmptyResponse(HttpURLConnection.HTTP_OK);
        Stormpath.logout();

        verify(mockPlatform().preferenceStore()).clearAccessToken();
        verify(mockPlatform().preferenceStore()).clearRefreshToken();
    }

    @Test
    public void failedLogoutDeletesTokens() throws Exception {
        stub(mockPlatform().preferenceStore().getAccessToken()).toReturn("abcdefghijklmnopqrstuvwyxz0123456789");

        enqueueEmptyResponse(HttpURLConnection.HTTP_BAD_REQUEST);
        Stormpath.logout();

        // the access tokens still need to be cleared
        verify(mockPlatform().preferenceStore()).clearAccessToken();
        verify(mockPlatform().preferenceStore()).clearRefreshToken();
    }

    @Test
    public void missingAccessTokenPrintsErrorWithoutCallingApi() throws Exception {
        Stormpath.logout();

        assertThat(requestCount()).isZero();
    }
}
