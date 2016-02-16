package com.stormpath.sdk;

import org.junit.Test;

import static org.assertj.core.api.Assertions.assertThat;

public class StormpathConfigurationTest extends BaseTest {

    @Test(expected = IllegalStateException.class)
    public void exceptionThrownIfBaseUrlNotSet() throws Exception {
        new StormpathConfiguration.Builder().build();
    }

    @Test
    public void pathsStartWithSlash() throws Exception {
        StormpathConfiguration stormpath = new StormpathConfiguration.Builder()
                .baseUrl("https://example.com")
                .oauthPath("my-oauth")
                .registerPath("my-register")
                .passwordResetPath("my-password-reset")
                .userProfilePath("my-me-path")
                .logoutPath("my-logout-path")
                .build();

        assertThat(stormpath.oauthPath()).isEqualTo("/my-oauth");
        assertThat(stormpath.registerPath()).isEqualTo("/my-register");
        assertThat(stormpath.passwordResetPath()).isEqualTo("/my-password-reset");
        assertThat(stormpath.userProfilePath()).isEqualTo("/my-me-path");
        assertThat(stormpath.logoutPath()).isEqualTo("/my-logout-path");
    }

    @Test
    public void baseUrlWithoutTrailingSlash() throws Exception {
        StormpathConfiguration stormpath = new StormpathConfiguration.Builder().baseUrl("https://example.com/").build();

        assertThat(stormpath.baseUrl()).isEqualTo("https://example.com");
    }

    @Test(expected = IllegalStateException.class)
    public void exceptionThrownIfUsedWithoutInit() throws Exception {
        Stormpath.login("user", "password", null);
    }
}
