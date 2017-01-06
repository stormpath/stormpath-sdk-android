package com.stormpath.sdk;

import org.junit.Test;

import static org.assertj.core.api.Assertions.assertThat;

public class StormpathConfigurationTest extends BaseTest {
    @Test
    public void baseUrlWithoutTrailingSlash() throws Exception {
        StormpathConfiguration stormpath = new StormpathConfiguration.Builder().baseUrl("https://example.com/").build();

        assertThat(stormpath.getBaseUrl()).isEqualTo("https://example.com");
    }

    @Test(expected = IllegalStateException.class)
    public void exceptionThrownIfUsedWithoutInit() throws Exception {
        Stormpath.login("user", "password", null);
    }
}
