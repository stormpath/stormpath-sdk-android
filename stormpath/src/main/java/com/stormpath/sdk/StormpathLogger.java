package com.stormpath.sdk;

import android.support.annotation.IntDef;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

public abstract class StormpathLogger {

    public static final int SILENT = 100;

    /**
     * Logging levels -- values match up with android.util.Log
     */
    public static final int VERBOSE = 2;

    public static final int DEBUG = 3;

    public static final int INFO = 4;

    public static final int WARN = 5;

    public static final int ERROR = 6;

    public static final int ASSERT = 7;

    @Retention(RetentionPolicy.SOURCE)
    @IntDef(value = {SILENT, VERBOSE, DEBUG, INFO, WARN, ERROR, ASSERT})
    @interface LogLevel {

    }

    /**
     * By default we don't log anything.
     */
    private int logLevel = SILENT;

    public void setLogLevel(@LogLevel int logLevel) {
        this.logLevel = logLevel;
    }

    protected abstract void log(@LogLevel int logLevel, String message);

    protected void log(@LogLevel int logLevel, Throwable throwable, String message, Object... args) {
        if (!isLoggable(logLevel)) {
            return;
        }

        if (message == null) {
            if (throwable == null) {
                return; // Swallow message if it's null and there's no throwable.
            }

            message = getStackTraceString(throwable);
        } else {
            if (args.length > 0) {
                message = String.format(message, args);
            }
            if (throwable != null) {
                message += "\n" + getStackTraceString(throwable);
            }
        }

        log(logLevel, message);
    }

    private boolean isLoggable(@LogLevel int level) {
        return level <= this.logLevel;
    }

    private String getStackTraceString(Throwable t) {
        // Don't replace this with Log.getStackTraceString() - it hides
        // UnknownHostException, which is not what we want.
        StringWriter sw = new StringWriter(256);
        PrintWriter pw = new PrintWriter(sw, false);
        t.printStackTrace(pw);
        pw.flush();
        return sw.toString();
    }

    public void v(String message, Object... args) {
        log(VERBOSE, null, message, args);
    }

    public void d(String message, Object... args) {
        log(DEBUG, null, message, args);
    }

    public void i(String message, Object... args) {
        log(INFO, null, message, args);
    }

    public void w(String message, Object... args) {
        log(WARN, null, message, args);
    }

    public void e(String message, Object... args) {
        log(ERROR, null, message, args);
    }

    public void wtf(String message, Object... args) {
        log(ASSERT, null, message, args);
    }

    public void v(Throwable t, String message, Object... args) {
        log(VERBOSE, t, message, args);
    }

    public void d(Throwable t, String message, Object... args) {
        log(DEBUG, t, message, args);
    }

    public void i(Throwable t, String message, Object... args) {
        log(INFO, t, message, args);
    }

    public void w(Throwable t, String message, Object... args) {
        log(WARN, t, message, args);
    }

    public void e(Throwable t, String message, Object... args) {
        log(ERROR, t, message, args);
    }

    public void wtf(Throwable t, String message, Object... args) {
        log(ASSERT, t, message, args);
    }
}
