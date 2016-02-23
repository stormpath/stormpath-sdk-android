package com.stormpath.sdk.utils;

import java.io.InputStream;
import java.util.Scanner;

public class ResourceUtils {

    private static final String MOCK_DATA_DIRECTORY = "mockdata/%s";

    private ResourceUtils() {
    }

    /**
     * Converts InputStream to String.
     */
    public static String convertStreamToString(InputStream is) {
        Scanner s = new Scanner(is, "UTF-8").useDelimiter("\\A");
        return s.hasNext() ? s.next() : "";
    }

    /**
     * Reads a resource file to <code>String</code>.
     */
    public static String readFromFile(String filename) {
        InputStream is = ResourceUtils.class.getClassLoader().getResourceAsStream(String.format(MOCK_DATA_DIRECTORY, filename));
        return convertStreamToString(is);
    }
}