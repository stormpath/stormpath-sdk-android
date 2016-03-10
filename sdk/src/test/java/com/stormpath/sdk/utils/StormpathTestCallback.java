package com.stormpath.sdk.utils;

import com.stormpath.sdk.StormpathCallback;
import com.stormpath.sdk.models.StormpathError;

public class StormpathTestCallback<T> implements StormpathCallback<T> {

    public T response;

    public StormpathError error;

    @Override
    public void onSuccess(T t) {
        this.response = t;
    }

    @Override
    public void onFailure(StormpathError error) {
        this.error = error;
    }
}
