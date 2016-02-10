package com.stormpath.sdk;

import android.content.Context;
import android.content.SharedPreferences;

public class SharedPrefsStore implements PreferenceStore {

    private final SharedPreferences sharedPreferences;

    public SharedPrefsStore(Context appContext) {
        sharedPreferences = appContext.getSharedPreferences("stormpath-shared-prefs", Context.MODE_PRIVATE);
    }
}
