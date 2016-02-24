package com.stormpath.sdk.models;

import com.squareup.moshi.Json;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import java.io.Serializable;

/**
 * Represents an error from the API (deserialized from response) or a local exception.
 */
public class StormpathError implements Serializable {

    @Json(name = "status")
    private int status;

    @Json(name = "message")
    private String message;

    @Json(name = "code")
    private int code;

    @Json(name = "developerMessage")
    private String developerMessage;

    @Json(name = "moreInfo")
    private String moreInfo;

    @Nullable
    private transient Throwable throwable;

    /**
     * Constructor for manually building an error when a local exception happens.
     *
     * @param message   a user-friendly message
     * @param throwable the exception we caught
     */
    public StormpathError(String message, @NonNull Throwable throwable) {
        this.throwable = throwable;
        this.message = message;
        this.developerMessage = throwable.getMessage();
        this.status = -1;
        this.code = -1;
    }

    public int status() {
        return status;
    }

    /**
     * @return an end-user error message
     */
    public String message() {
        return message;
    }

    public int code() {
        return code;
    }

    public String developerMessage() {
        return developerMessage;
    }

    public String moreInfo() {
        return moreInfo;
    }

    /**
     * @return the throwable which caused the error, or null if the error was returned by the API
     */
    @Nullable
    public Throwable throwable() {
        return throwable;
    }
}
