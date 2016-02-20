package com.stormpath.sdk.utils;

import android.app.Application;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.stub;

public class Mocks {

    public static Application appContext() {
        Application application = mock(Application.class);
        stub(application.getApplicationContext()).toReturn(application);
        return application;
    }
}
