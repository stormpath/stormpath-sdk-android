package com.stormpath.sdk;

import com.stormpath.sdk.models.Account;
import com.stormpath.sdk.models.StormpathError;

import org.junit.Test;

import java.net.HttpURLConnection;

import okhttp3.mockwebserver.RecordedRequest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.stub;
import static org.mockito.Mockito.verify;

public class UserProfileTest extends BaseTest {

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

        enqueueResponse("stormpath-user-profile-response.json");
        Stormpath.getAccount(mock(StormpathCallback.class));

        RecordedRequest request = takeLastRequest();

        assertThat(request.getMethod()).isEqualTo("GET");
        assertThat(request.getPath()).isEqualTo("/me");
        assertThat(request.getHeader("Authorization")).isEqualTo("Bearer " + accessToken);
        assertThat(request.getHeader("Accept")).isEqualTo("application/json");
    }

    @Test
    public void successfulFetchCallsSuccess() throws Exception {
        stub(mockPlatform().preferenceStore().getAccessToken()).toReturn("abcdefghijklmnopqrstuvwyxz0123456789");

        enqueueResponse("stormpath-user-profile-response.json");
        StormpathCallback<Account> callback = mock(StormpathCallback.class);
        Stormpath.getAccount(callback);

        verify(callback).onSuccess(any(Account.class));
    }

    @Test
    public void successfulFetchReturnsCorrectData() throws Exception {
        stub(mockPlatform().preferenceStore().getAccessToken()).toReturn("abcdefghijklmnopqrstuvwyxz0123456789");

        UserProfileCallback callback = new UserProfileCallback();
        enqueueResponse("stormpath-user-profile-response.json");

        Stormpath.getAccount(callback);
        assertThat(callback.account.getEmail()).isEqualTo("john.deere@example.com");
        assertThat(callback.account.getUsername()).isEqualTo("john.deere@example.com");
        assertThat(callback.account.getGivenName()).isEqualTo("John");
        assertThat(callback.account.getMiddleName()).isNull();
        assertThat(callback.account.getSurname()).isEqualTo("Deere");
        assertThat(callback.account.getFullName()).isEqualTo("John Deere");
        assertThat(callback.account.getStatus()).isEqualTo("ENABLED");
    }

    @Test
    public void failedFetchCallsFailure() throws Exception {
        stub(mockPlatform().preferenceStore().getAccessToken()).toReturn("abcdefghijklmnopqrstuvwyxz0123456789");

        enqueueEmptyResponse(HttpURLConnection.HTTP_BAD_REQUEST);
        StormpathCallback<Account> callback = mock(StormpathCallback.class);
        Stormpath.getAccount(callback);

        verify(callback).onFailure(any(StormpathError.class));
    }

    @Test
    public void failedDeserializationCallsFailure() throws Exception {
        enqueueEmptyResponse(HttpURLConnection.HTTP_OK);
        StormpathCallback<Account> callback = mock(StormpathCallback.class);
        Stormpath.getAccount(callback);

        verify(callback).onFailure(any(StormpathError.class));
    }

    @Test
    public void missingAccessTokenCallsFailureWithoutCallingApi() throws Exception {
        StormpathCallback<Account> callback = mock(StormpathCallback.class);
        Stormpath.getAccount(callback);

        verify(callback).onFailure(any(StormpathError.class));
        assertThat(requestCount()).isZero();
    }

    private static class UserProfileCallback implements StormpathCallback<Account> {
        public Account account;

        @Override
        public void onSuccess(Account account) {
            this.account = account;
        }

        @Override
        public void onFailure(StormpathError error) {
            throw new RuntimeException("onFailure was called but it shouldn't have been!");
        }
    }
}
