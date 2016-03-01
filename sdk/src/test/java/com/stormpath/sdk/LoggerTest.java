package com.stormpath.sdk;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.RobolectricGradleTestRunner;
import org.robolectric.RuntimeEnvironment;
import org.robolectric.shadows.ShadowLog;

import static org.assertj.core.api.Assertions.assertThat;

@RunWith(RobolectricGradleTestRunner.class)
public class LoggerTest extends BaseTest {

    private StormpathLogger logger;

    @Override
    public void setUp() throws Exception {
        super.setUp();
        ShadowLog.getLogs().clear();

        StormpathConfiguration configuration = new StormpathConfiguration.Builder()
                .baseUrl(mockServerUrl())
                .build();
        Stormpath.init(RuntimeEnvironment.application, configuration);

        logger = Stormpath.logger();
    }

    private void writeTestLogs() {
        logger.v("verbose");
        logger.d("debug");
        logger.i("info");
        logger.w("warn");
        logger.e("error");
        logger.wtf("assert");

        logger.v("verbose: %s", "v");
        logger.d("debug: %s", "d");
        logger.i("info: %s", "i");
        logger.w("warn: %s", "w");
        logger.e("error: %s", "e");
        logger.wtf("assert: %s", "wtf");

        logger.v(new RuntimeException("v"), "verbose");
        logger.d(new RuntimeException("d"), "debug");
        logger.i(new RuntimeException("i"), "info");
        logger.w(new RuntimeException("w"), "warn");
        logger.e(new RuntimeException("e"), "error");
        logger.wtf(new RuntimeException("wtf"), "assert");

        logger.v(new RuntimeException("v"), null);
        logger.d(new RuntimeException("d"), null);
        logger.i(new RuntimeException("i"), null);
        logger.w(new RuntimeException("w"), null);
        logger.e(new RuntimeException("e"), null);
        logger.wtf(new RuntimeException("wtf"), null);
    }

    @Test
    public void logsSilentByDefault() throws Exception {
        writeTestLogs();

        assertThat(ShadowLog.getLogs()).isEmpty();
    }

    @Test
    public void verboseLogs() throws Exception {
        Stormpath.setLogLevel(StormpathLogger.VERBOSE);
        writeTestLogs();

        assertThat(ShadowLog.getLogs()).hasSize(24);
    }

    @Test
    public void nullLogStatementsNotLogged() throws Exception {
        Stormpath.setLogLevel(StormpathLogger.VERBOSE);

        logger.v(null);
        logger.v(null, null);
        logger.v(null, null, null);

        assertThat(ShadowLog.getLogs()).isEmpty();
    }
}
