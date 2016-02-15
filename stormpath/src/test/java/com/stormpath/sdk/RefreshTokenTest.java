package com.stormpath.sdk;

import org.junit.Test;

import java.net.HttpURLConnection;

import okhttp3.mockwebserver.RecordedRequest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.stub;
import static org.mockito.Mockito.verify;

public class RefreshTokenTest extends BaseTest {

    @Override
    public void setUp() throws Exception {
        super.setUp();
        initWithDefaults();
    }

    @Test
    public void correctRequest() throws Exception {
        String refreshToken = "eyJraWQiOiI2WjA3NEJBQzhTM0tGWE5KOVhFTldEVUhGIiwiYWxnIjoiSFMyNTYifQ.eyJqdGkiOiIzQjlEclh4NG9RdFR6VWU0dFhCR0d5I"
                + "iwiaWF0IjoxNDU1MjE4Mzg0LCJpc3MiOiJodHRwczovL2FwaS5zdG9ybXBhdGguY29tL3YxL2FwcGxpY2F0aW9ucy8yUnJxS25UaW91M1F3Tm50RTN0Y0VHI"
                + "iwic3ViIjoiaHR0cHM6Ly9hcGkuc3Rvcm1wYXRoLmNvbS92MS9hY2NvdW50cy81TXNRVEE0UVI5c0hZNnlRQlFFSXlLIiwiZXhwIjoxNDYwNDAyMzg0fQ.lt"
                + "Fz1735IRnD0vIgdeKC4oInxe8K0KzhDJ95RgRuqmA";
        stub(mockPlatform().preferenceStore().getRefreshToken()).toReturn(refreshToken);

        enqueueResponse("stormpath-refresh-token-response.json");
        Stormpath.refreshAccessToken(mock(StormpathCallback.class));

        RecordedRequest request = takeLastRequest();

        assertThat(request.getMethod()).isEqualTo("POST");
        assertThat(request.getPath()).isEqualTo("/oauth/token");
        assertThat(request.getHeader("Accept")).isEqualTo("application/json");
        assertThat(request.getBody().readUtf8()).isEqualTo("refresh_token=" + refreshToken + "&grant_type=refresh_token");
    }

    @Test
    public void successfulRefreshSavesTokens() throws Exception {
        stub(mockPlatform().preferenceStore().getRefreshToken()).toReturn("abcdefghijklmnopqrstuwyxz0123456789");

        enqueueResponse("stormpath-refresh-token-response.json");
        Stormpath.refreshAccessToken(mock(StormpathCallback.class));

        verify(mockPlatform().preferenceStore()).setAccessToken("eyJraWQiOiI2WjA3NEJBQzhTM0tGWE5KOVhFTldEVUhGIiwiYWxnIjoiSFMyNTYifQ.eyJqdGk"
                + "iOiI1aW5La0tGNzhTeFg4T2cxbnRwbjZJIiwiaWF0IjoxNDU1NTI1MTg1LCJpc3MiOiJodHRwczovL2FwaS5zdG9ybXBhdGguY29tL3YxL2FwcGxpY2F0aW9"
                + "ucy8yUnJxS25UaW91M1F3Tm50RTN0Y0VHIiwic3ViIjoiaHR0cHM6Ly9hcGkuc3Rvcm1wYXRoLmNvbS92MS9hY2NvdW50cy81TXNRVEE0UVI5c0hZNnlRQlF"
                + "FSXlLIiwiZXhwIjoxNDU1NTI4Nzg1LCJydGkiOiI1MVcwc015Z0h3THQyVElsTFhKaGgwIn0.OUDaWC5xMyidkDX8FELAvvwDWk7-pWmUiUWQimPX3lA");
        verify(mockPlatform().preferenceStore()).setRefreshToken(
                "eyJraWQiOiI2WjA3NEJBQzhTM0tGWE5KOVhFTldEVUhGIiwiYWxnIjoiSFMyNTYifQ.eyJqdGkiOiI1MVcwc015Z0h3THQyVElsTFhKaGgwIiwiaWF0IjoxNDU"
                        + "1NTI1MTQ3LCJpc3MiOiJodHRwczovL2FwaS5zdG9ybXBhdGguY29tL3YxL2FwcGxpY2F0aW9ucy8yUnJxS25UaW91M1F3Tm50RTN0Y0VHIiwic3V"
                        + "iIjoiaHR0cHM6Ly9hcGkuc3Rvcm1wYXRoLmNvbS92MS9hY2NvdW50cy81TXNRVEE0UVI5c0hZNnlRQlFFSXlLIiwiZXhwIjoxNDYwNzA5MTQ3fQ."
                        + "g15poTJqau1nluVSCr0j-xnlqrQwySFOVX30mD_jw5A");
    }

    @Test
    public void successfulRefreshCallsSuccess() throws Exception {
        stub(mockPlatform().preferenceStore().getRefreshToken()).toReturn("abcdefghijklmnopqrstuwyxz0123456789");

        enqueueResponse("stormpath-refresh-token-response.json");
        StormpathCallback<Void> callback = mock(StormpathCallback.class);
        Stormpath.refreshAccessToken(callback);

        verify(callback).onSuccess(null);
    }

    @Test
    public void failedRefreshCallsFailure() throws Exception {
        stub(mockPlatform().preferenceStore().getRefreshToken()).toReturn("abcdefghijklmnopqrstuwyxz0123456789");

        enqueueResponse("stormpath-refresh-token-400.json", HttpURLConnection.HTTP_BAD_REQUEST);
        StormpathCallback<Void> callback = mock(StormpathCallback.class);
        Stormpath.refreshAccessToken(callback);

        verify(callback).onFailure(any(Throwable.class));
    }

    @Test
    public void failedDeserializationCallsFailure() throws Exception {
        stub(mockPlatform().preferenceStore().getRefreshToken()).toReturn("abcdefghijklmnopqrstuwyxz0123456789");

        enqueueEmptyResponse(HttpURLConnection.HTTP_OK);
        StormpathCallback<Void> callback = mock(StormpathCallback.class);
        Stormpath.refreshAccessToken(callback);

        verify(callback).onFailure(any(Throwable.class));
    }

    @Test
    public void missingAccessTokenInResponseCallsFailure() throws Exception {
        stub(mockPlatform().preferenceStore().getRefreshToken()).toReturn("abcdefghijklmnopqrstuwyxz0123456789");

        enqueueStringResponse("{}");
        StormpathCallback<Void> callback = mock(StormpathCallback.class);
        Stormpath.refreshAccessToken(callback);

        verify(callback).onFailure(any(Throwable.class));
    }

    @Test
    public void missingRefreshTokenCallsFailureWithoutCallingApi() throws Exception {
        StormpathCallback<Void> callback = mock(StormpathCallback.class);
        Stormpath.refreshAccessToken(callback);

        verify(callback).onFailure(any(Throwable.class));
        assertThat(requestCount()).isZero();
    }
}
