package com.stormpath.sdk.utils;

public class StringUtils {

    private StringUtils() {
        // no instantiations
    }

    public static boolean isNotBlank(String s) {
        return !isBlank(s);
    }

    public static boolean isBlank(String s) {
        return s == null || s.length() == 0;
    }
}
