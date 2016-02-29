package com.stormpath.sdk;

import org.junit.Test;

import static org.assertj.core.api.Assertions.assertThat;

public class StormpathConfigurationTest extends BaseTest {

    @Test(expected = IllegalStateException.class)
    public void exceptionThrownIfBaseUrlNotSet() throws Exception {
        new StormpathConfiguration.Builder().build();
    }

    @Test(expected = IllegalStateException.class)
    public void exceptionThrownIfNullOauthPathSet() throws Exception {
        new StormpathConfiguration.Builder()
                .baseUrl("https://api.example.com")
                .oauthPath(null)
                .registerPath("/my-register")
                .passwordResetPath("/my-password-reset")
                .verifyEmailPath("/my-verify-email")
                .userProfilePath("/my-me-path")
                .logoutPath("/my-logout-path")
                .socialProvidersPath("/my-social-providers-path")
                .build();
    }

    @Test(expected = IllegalStateException.class)
    public void exceptionThrownIfNullRegisterPathSet() throws Exception {
        new StormpathConfiguration.Builder()
                .baseUrl("https://api.example.com")
                .oauthPath("/my-oauth")
                .registerPath(null)
                .passwordResetPath("/my-password-reset")
                .verifyEmailPath("/my-verify-email")
                .userProfilePath("/my-me-path")
                .logoutPath("/my-logout-path")
                .socialProvidersPath("my-social-providers-path")
                .build();
    }

    @Test(expected = IllegalStateException.class)
    public void exceptionThrownIfNullPasswordResetPathSet() throws Exception {
        new StormpathConfiguration.Builder()
                .baseUrl("https://api.example.com")
                .oauthPath("/my-oauth")
                .registerPath("/my-register")
                .passwordResetPath(null)
                .verifyEmailPath("/my-verify-email")
                .userProfilePath("/my-me-path")
                .logoutPath("/my-logout-path")
                .socialProvidersPath("/my-social-providers-path")
                .build();
    }

    @Test(expected = IllegalStateException.class)
    public void exceptionThrownIfNullVerifyEmailPathSet() throws Exception {
        new StormpathConfiguration.Builder()
                .baseUrl("https://api.example.com")
                .oauthPath("/my-oauth")
                .registerPath("/my-register")
                .passwordResetPath("/my-password-reset")
                .verifyEmailPath(null)
                .userProfilePath("/my-me-path")
                .logoutPath("/my-logout-path")
                .socialProvidersPath("/my-social-providers-path")
                .build();
    }

    @Test(expected = IllegalStateException.class)
    public void exceptionThrownIfNullUserProfilePathSet() throws Exception {
        new StormpathConfiguration.Builder()
                .baseUrl("https://api.example.com")
                .oauthPath("/my-oauth")
                .registerPath("/my-register")
                .passwordResetPath("/my-password-reset")
                .verifyEmailPath("/my-verify-email")
                .userProfilePath(null)
                .logoutPath("/my-logout-path")
                .socialProvidersPath("/my-social-providers-path")
                .build();
    }

    @Test(expected = IllegalStateException.class)
    public void exceptionThrownIfNullLogoutPathSet() throws Exception {
        new StormpathConfiguration.Builder()
                .baseUrl("https://api.example.com")
                .oauthPath("/my-oauth")
                .registerPath("/my-register")
                .passwordResetPath("/my-password-reset")
                .verifyEmailPath("/my-verify-email")
                .userProfilePath("/my-me-path")
                .logoutPath(null)
                .socialProvidersPath("/my-social-providers-path")
                .build();
    }

    @Test(expected = IllegalStateException.class)
    public void exceptionThrownIfNullSocialProvidersPathSet() throws Exception {
        new StormpathConfiguration.Builder()
                .baseUrl("https://api.example.com")
                .oauthPath("/my-oauth")
                .registerPath("/my-register")
                .passwordResetPath("/my-password-reset")
                .verifyEmailPath("/my-verify-email")
                .userProfilePath("/my-me-path")
                .logoutPath("/my-logout-path")
                .socialProvidersPath(null)
                .build();
    }

    @Test
    public void pathsStartWithSlash() throws Exception {
        StormpathConfiguration stormpath = new StormpathConfiguration.Builder()
                .baseUrl("https://api.example.com")
                .oauthPath("my-oauth")
                .registerPath("my-register")
                .passwordResetPath("my-password-reset")
                .verifyEmailPath("my-verify-email")
                .userProfilePath("my-me-path")
                .logoutPath("my-logout-path")
                .socialProvidersPath("my-social-providers-path")
                .build();

        assertThat(stormpath.oauthPath()).isEqualTo("/my-oauth");
        assertThat(stormpath.registerPath()).isEqualTo("/my-register");
        assertThat(stormpath.passwordResetPath()).isEqualTo("/my-password-reset");
        assertThat(stormpath.verifyEmailPath()).isEqualTo("/my-verify-email");
        assertThat(stormpath.userProfilePath()).isEqualTo("/my-me-path");
        assertThat(stormpath.logoutPath()).isEqualTo("/my-logout-path");
        assertThat(stormpath.socialProvidersPath()).isEqualTo("/my-social-providers-path");
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
