package com.stormpath.sdk;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.RobolectricGradleTestRunner;
import org.robolectric.RuntimeEnvironment;

import static org.assertj.core.api.Assertions.assertThat;

@RunWith(RobolectricGradleTestRunner.class)
public class StormpathTest extends BaseTest {

    public void configurationSaved() throws Exception {
        Stormpath.init(RuntimeEnvironment.application, new StormpathConfiguration.Builder().baseUrl("https://example.com").build());
        assertThat(Stormpath.config.baseUrl()).isEqualTo("https://example.com");
    }

    @Test(expected = IllegalStateException.class)
    public void exceptionThrownIfConfiguredMoreThanOnce() throws Exception {
        Stormpath.init(RuntimeEnvironment.application, new StormpathConfiguration.Builder().baseUrl("https://example.com").build());
        Stormpath.init(RuntimeEnvironment.application, new StormpathConfiguration.Builder().baseUrl("https://example.com").build());
    }
}
