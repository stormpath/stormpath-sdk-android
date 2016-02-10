package com.stormpath.sdk;

import org.junit.Test;

import static org.assertj.core.api.Assertions.assertThat;

public class StormpathTest {

    @Test(expected = IllegalStateException.class)
    public void exceptionThrownIfBaseUrlNotSet() throws Exception {
        new Stormpath.Builder().build();
    }

    @Test
    public void pathsStartWithSlash() throws Exception {
        Stormpath stormpath = new Stormpath.Builder()
                .baseUrl("https://example.com")
                .oauthPath("my-oauth")
                .registerPath("my-register")
                .passwordResetPath("my-password-reset")
                .verifyEmailPath("my-verify-email")
                .userProfilePath("my-me-path")
                .logoutPath("my-logout-path")
                .build();

        assertThat(stormpath.getOauthPath()).isEqualTo("/my-oauth");
        assertThat(stormpath.getRegisterPath()).isEqualTo("/my-register");
        assertThat(stormpath.getPasswordResetPath()).isEqualTo("/my-password-reset");
        assertThat(stormpath.getVerifyEmailPath()).isEqualTo("/my-verify-email");
        assertThat(stormpath.getUserProfilePath()).isEqualTo("/my-me-path");
        assertThat(stormpath.getLogoutPath()).isEqualTo("/my-logout-path");
    }

    @Test
    public void baseUrlWithoutTrailingSlash() throws Exception {
        Stormpath stormpath = new Stormpath.Builder().baseUrl("https://example.com/").build();

        assertThat(stormpath.getBaseUrl()).isEqualTo("https://example.com");
    }
}
