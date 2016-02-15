package com.stormpath.sdk;

import com.stormpath.sdk.models.RegisterParams;
import com.stormpath.sdk.utils.ResourceUtils;

import org.junit.Test;

import java.net.HttpURLConnection;

import okhttp3.mockwebserver.MockResponse;
import okhttp3.mockwebserver.RecordedRequest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

public class RegisterTest extends BaseTest {

    @Override
    public void setUp() throws Exception {
        super.setUp();
        initWithDefaults();
    }

    @Test
    public void correctRequest() throws Exception {
        enqueueResponse("stormpath-register-response.json");
        Stormpath.register(new RegisterParams("John", "Deere", "john.deere@example.com", "Test1234&"), mock(StormpathCallback.class));

        RecordedRequest request = takeLastRequest();

        assertThat(request.getMethod()).isEqualTo("POST");
        assertThat(request.getPath()).isEqualTo("/register");
        assertThat(request.getBody().readUtf8())
                .isEqualTo("{\"email\":\"john.deere@example.com\",\"givenName\":\"John\",\"password\":\"Test1234&\",\"surname\":\"Deere\"}");
    }

    @Test
    public void successfulRegistrationCallsSuccess() throws Exception {
        enqueueResponse("stormpath-register-response.json");
        StormpathCallback<Void> callback = mock(StormpathCallback.class);
        Stormpath.register(new RegisterParams("John", "Deere", "john.deere@example.com", "Test1234"), callback);

        verify(callback).onSuccess(null);
    }

    @Test
    public void successfulRegistrationSavesTokens() throws Exception {
        MockResponse mockResponse = new MockResponse()
                .setBody(ResourceUtils.readFromFile("stormpath-register-response.json"))
                .addHeader("Set-Cookie: access_token=eyJraWQiOiI2WjA3NEJBQzhTM0tGWE5KOVhFTldEVUhGIiwiYWxnIjoiSFMyNTYifQ.eyJqdGkiOiI0aEtvNG5"
                        + "PWDBKNTJ0dnZ1cGJjNHBiIiwiaWF0IjoxNDU1Mjk2NjM2LCJpc3MiOiJodHRwczovL2FwaS5zdG9ybXBhdGguY29tL3YxL2FwcGxpY2F0aW9ucy8"
                        + "yUnJxS25UaW91M1F3Tm50RTN0Y0VHIiwic3ViIjoiaHR0cHM6Ly9hcGkuc3Rvcm1wYXRoLmNvbS92MS9hY2NvdW50cy80Z1F4MEM5bTE5UE9IQ2Z"
                        + "yeTdEUzZ4IiwiZXhwIjoxNDU1MzAwMjM2LCJydGkiOiI0aEtvNGs0UzVpbTRINnEzZExrb0RYIn0.mqoBTzyPcIrcx224T4hSJuc0aPrmuiuiNME"
                        + "e_a0diI8; path=/; expires=Fri, 12 Feb 2016 18:03:56 GMT; httponly")
                .addHeader("Set-Cookie: refresh_token=eyJraWQiOiI2WjA3NEJBQzhTM0tGWE5KOVhFTldEVUhGIiwiYWxnIjoiSFMyNTYifQ.eyJqdGkiOiI0aEtvNG"
                        + "s0UzVpbTRINnEzZExrb0RYIiwiaWF0IjoxNDU1Mjk2NjM2LCJpc3MiOiJodHRwczovL2FwaS5zdG9ybXBhdGguY29tL3YxL2FwcGxpY2F0aW9ucy"
                        + "8yUnJxS25UaW91M1F3Tm50RTN0Y0VHIiwic3ViIjoiaHR0cHM6Ly9hcGkuc3Rvcm1wYXRoLmNvbS92MS9hY2NvdW50cy80Z1F4MEM5bTE5UE9IQ2"
                        + "ZyeTdEUzZ4IiwiZXhwIjoxNDYwNDgwNjM2fQ.zKjsFRsI2hoBu9vjeKvDFzAZy-0fq_C98w05TSXQ0Ns; path=/; expires=Tue, 12 Apr 20"
                        + "16 17:03:56 GMT; httponly");
        enqueueResponse(mockResponse);
        Stormpath.register(new RegisterParams("John", "Deere", "john.deere@example.com", "Test1234"), mock(StormpathCallback.class));

        verify(mockPlatform().preferenceStore()).setAccessToken("eyJraWQiOiI2WjA3NEJBQzhTM0tGWE5KOVhFTldEVUhGIiwiYWxnIjoiSFMyNTYifQ.eyJqdGk"
                + "iOiI0aEtvNG5PWDBKNTJ0dnZ1cGJjNHBiIiwiaWF0IjoxNDU1Mjk2NjM2LCJpc3MiOiJodHRwczovL2FwaS5zdG9ybXBhdGguY29tL3YxL2FwcGxpY2F0aW9"
                + "ucy8yUnJxS25UaW91M1F3Tm50RTN0Y0VHIiwic3ViIjoiaHR0cHM6Ly9hcGkuc3Rvcm1wYXRoLmNvbS92MS9hY2NvdW50cy80Z1F4MEM5bTE5UE9IQ2ZyeTd"
                + "EUzZ4IiwiZXhwIjoxNDU1MzAwMjM2LCJydGkiOiI0aEtvNGs0UzVpbTRINnEzZExrb0RYIn0.mqoBTzyPcIrcx224T4hSJuc0aPrmuiuiNMEe_a0diI8");
        verify(mockPlatform().preferenceStore())
                .setRefreshToken("eyJraWQiOiI2WjA3NEJBQzhTM0tGWE5KOVhFTldEVUhGIiwiYWxnIjoiSFMyNTYifQ.eyJqdGk"
                        + "iOiI0aEtvNGs0UzVpbTRINnEzZExrb0RYIiwiaWF0IjoxNDU1Mjk2NjM2LCJpc3MiOiJodHRwczovL2FwaS5zdG9ybXBhdGguY29tL3YxL2FwcGxpY2F0aW9"
                        + "ucy8yUnJxS25UaW91M1F3Tm50RTN0Y0VHIiwic3ViIjoiaHR0cHM6Ly9hcGkuc3Rvcm1wYXRoLmNvbS92MS9hY2NvdW50cy80Z1F4MEM5bTE5UE9IQ2ZyeTd"
                        + "EUzZ4IiwiZXhwIjoxNDYwNDgwNjM2fQ.zKjsFRsI2hoBu9vjeKvDFzAZy-0fq_C98w05TSXQ0Ns");
    }

    @Test
    public void failedRegisterCallsFailure() throws Exception {
        enqueueResponse("stormpath-register-400.json", HttpURLConnection.HTTP_BAD_REQUEST);
        StormpathCallback<Void> callback = mock(StormpathCallback.class);
        Stormpath.register(new RegisterParams("John", "Deere", "john.deere@example.com", "Test1234"), callback);

        verify(callback).onFailure(any(Throwable.class));
    }
}
