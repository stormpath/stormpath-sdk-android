package com.stormpath.sdk;

import android.util.Log;

public class AndroidLogger extends StormpathLogger {

    public static final String TAG = "Stormpath";

    @Override
    protected void log(@LogLevel int logLevel, String message) {
        switch (logLevel) {
            case StormpathLogger.VERBOSE:
                Log.v(TAG, message);
                break;
            case StormpathLogger.DEBUG:
                Log.d(TAG, message);
                break;
            case StormpathLogger.INFO:
                Log.i(TAG, message);
                break;
            case StormpathLogger.WARN:
                Log.w(TAG, message);
                break;
            case StormpathLogger.ERROR:
                Log.e(TAG, message);
                break;
            case StormpathLogger.ASSERT:
                Log.wtf(TAG, message);
                break;
            case StormpathLogger.SILENT:
                // nothing to do here
                break;
        }
    }
}
