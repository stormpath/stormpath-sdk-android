package com.stormpath.sdk;

public interface StormpathCallback<T> {

    void onSuccess(T t);

    void onFailure(StormpathError error);
}
