package com.stormpath.sdk;

import com.stormpath.sdk.android.SharedPrefsStore;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.RobolectricGradleTestRunner;
import org.robolectric.RuntimeEnvironment;

import static org.assertj.core.api.Assertions.assertThat;

@RunWith(RobolectricGradleTestRunner.class)
public class PreferenceStoreTest extends BaseTest {

    private PreferenceStore preferenceStore;

    @Before
    public void setUp() {
        preferenceStore = new SharedPrefsStore(RuntimeEnvironment.application);
    }

    @Test
    public void accessTokenStorageWorks() throws Exception {
        String accessToken = "asdf1234567890";
        assertThat(preferenceStore.getAccessToken()).isNull();
        preferenceStore.setAccessToken(accessToken);
        assertThat(preferenceStore.getAccessToken()).isEqualTo(accessToken);
        preferenceStore.clearAccessToken();
        assertThat(preferenceStore.getAccessToken()).isNull();
    }

    @Test
    public void refreshTokenStorageWorks() throws Exception {
        String refreshToken = "1234567890asdf";
        assertThat(preferenceStore.getRefreshToken()).isNull();
        preferenceStore.setRefreshToken(refreshToken);
        assertThat(preferenceStore.getRefreshToken()).isEqualTo(refreshToken);
        preferenceStore.clearRefreshToken();
        assertThat(preferenceStore.getRefreshToken()).isNull();
    }
}
