package com.stormpath.sdk;

import com.stormpath.sdk.models.RegisterParams;

import org.junit.Test;

import java.net.HttpURLConnection;

import okhttp3.mockwebserver.RecordedRequest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.stub;

public class CustomPathTest extends BaseTest {

    @Test
    public void customLoginPathUsed() throws Exception {
        StormpathConfiguration config = new StormpathConfiguration.Builder()
                .baseUrl(mockServerUrl())
                .loginPath("/my-login-path")
                .build();
        Stormpath.init(mockPlatform(), config);

        enqueueEmptyResponse(HttpURLConnection.HTTP_OK);
        Stormpath.login("user", "pass", mock(StormpathCallback.class));

        RecordedRequest request = takeLastRequest();
        assertThat(request.getPath()).isEqualTo("/my-login-path");
    }

    @Test
    public void customRegisterPathUsed() throws Exception {
        StormpathConfiguration config = new StormpathConfiguration.Builder()
                .baseUrl(mockServerUrl())
                .registerPath("/my-register-path")
                .build();
        Stormpath.init(mockPlatform(), config);

        enqueueEmptyResponse(HttpURLConnection.HTTP_OK);
        Stormpath.register(new RegisterParams("John", "Deere", "john.deere@example.com", "secret-safe-P45"), mock(StormpathCallback.class));

        RecordedRequest request = takeLastRequest();
        assertThat(request.getPath()).isEqualTo("/my-register-path");
    }

    @Test
    public void customLogoutPathUsed() throws Exception {
        StormpathConfiguration config = new StormpathConfiguration.Builder()
                .baseUrl(mockServerUrl())
                .logoutPath("/my-logout-path")
                .build();
        Stormpath.init(mockPlatform(), config);

        stub(mockPlatform().preferenceStore().getAccessToken()).toReturn("abcdefghijklmnopqrstuvwyxz0123456789");

        enqueueEmptyResponse(HttpURLConnection.HTTP_OK);
        Stormpath.logout();

        RecordedRequest request = takeLastRequest();
        assertThat(request.getPath()).isEqualTo("/my-logout-path");
    }

    @Test
    public void customResetPasswordPathUsed() throws Exception {
        StormpathConfiguration config = new StormpathConfiguration.Builder()
                .baseUrl(mockServerUrl())
                .passwordResetPath("/my-reset-path")
                .build();
        Stormpath.init(mockPlatform(), config);

        enqueueEmptyResponse(HttpURLConnection.HTTP_OK);
        Stormpath.resetPassword("john.deere@example.com", mock(StormpathCallback.class));

        RecordedRequest request = takeLastRequest();
        assertThat(request.getPath()).isEqualTo("/my-reset-path");
    }

    @Test
    public void customUserProfilePathUsed() throws Exception {
        StormpathConfiguration config = new StormpathConfiguration.Builder()
                .baseUrl(mockServerUrl())
                .userProfilePath("/my-user-path")
                .build();
        Stormpath.init(mockPlatform(), config);

        stub(mockPlatform().preferenceStore().getAccessToken()).toReturn("abcdefghijklmnopqrstuvwyxz0123456789");

        enqueueEmptyResponse(HttpURLConnection.HTTP_OK);
        Stormpath.getUserProfile(mock(StormpathCallback.class));

        RecordedRequest request = takeLastRequest();
        assertThat(request.getPath()).isEqualTo("/my-user-path");
    }

    @Test
    public void customRefreshTokenPathUsed() throws Exception {
        StormpathConfiguration config = new StormpathConfiguration.Builder()
                .baseUrl(mockServerUrl())
                .oauthPath("/my-oauth-path")
                .build();
        Stormpath.init(mockPlatform(), config);

        stub(mockPlatform().preferenceStore().getRefreshToken()).toReturn("abcdefghijklmnopqrstuvwyxz0123456789");

        enqueueEmptyResponse(HttpURLConnection.HTTP_OK);
        Stormpath.refreshAccessToken(mock(StormpathCallback.class));

        RecordedRequest request = takeLastRequest();
        assertThat(request.getPath()).isEqualTo("/my-oauth-path");
    }
}
