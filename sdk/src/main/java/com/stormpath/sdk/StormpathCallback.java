package com.stormpath.sdk;

import com.stormpath.sdk.models.StormpathError;

public interface StormpathCallback<T> {

    void onSuccess(T t);

    void onFailure(StormpathError error);
}
