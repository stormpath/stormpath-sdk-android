package com.stormpath.sdk;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.RobolectricGradleTestRunner;
import org.robolectric.RuntimeEnvironment;

import static org.assertj.core.api.Assertions.assertThat;

@RunWith(RobolectricGradleTestRunner.class)
public class StormpathTest extends BaseTest {

    @Test
    public void configurationSaved() throws Exception {
        Stormpath.init(RuntimeEnvironment.application, new StormpathConfiguration.Builder().baseUrl("https://example.com").build());
        assertThat(Stormpath.config.baseUrl()).isEqualTo("https://example.com");
    }

    @Test
    public void savedAccessTokenIsReturned() throws Exception {
        Stormpath.init(RuntimeEnvironment.application, new StormpathConfiguration.Builder().baseUrl("https://example.com").build());
        String accessToken = "asdf1234";
        Stormpath.platform.preferenceStore().setAccessToken(accessToken);
        assertThat(Stormpath.accessToken()).isEqualTo(accessToken);
    }

    @Test(expected = IllegalStateException.class)
    public void exceptionThrownIfConfiguredMoreThanOnce() throws Exception {
        Stormpath.init(RuntimeEnvironment.application, new StormpathConfiguration.Builder().baseUrl("https://example.com").build());
        Stormpath.init(RuntimeEnvironment.application, new StormpathConfiguration.Builder().baseUrl("https://example.com").build());
    }
}
