package com.stormpath.sdk.ui;

import android.content.Context;
import android.content.pm.PackageManager;
import android.content.res.Resources;
import android.os.Bundle;
import android.util.Log;

import java.io.Serializable;

public class StormpathLoginConfig implements Serializable {

    public static final String AUTO_LOGIN = "StormpathLoginActivity.AUTO_LOGIN_AFTER_REGISTER";
    public static final String BACKGROUND_COLOR = "backgroundColor";
    public static final String ICON = "icon";

    private boolean autoLoginAfterRegister;
    private int iconResource;
    private int backgroundColor;

    private int primaryActionButtonColor;
    private int primaryActionButtonColorSelected;
    private int primaryActionTextColor;

    private int secondaryActionButtonColor;
    private int secondaryActionButtonColorSelected;
    private int secondaryActionTextColor;

    //private int tertiaryAction

    public static StormpathLoginConfig fromBundle(Bundle bundle) {

        StormpathLoginConfig.Builder configBuilder = new Builder();

        if (bundle.containsKey(AUTO_LOGIN)) {
            configBuilder.autoLoginAfterRegister(bundle.getBoolean(AUTO_LOGIN));
        }

        if (bundle.containsKey(ICON)){
            configBuilder.setIcon(bundle.getInt(ICON));
        }

        if (bundle.containsKey(BACKGROUND_COLOR)){
            configBuilder.setBackgroundColor(bundle.getInt(BACKGROUND_COLOR));
        }

        return configBuilder.build();
    }

    public boolean isAutoLoginAfterRegister() {
        return autoLoginAfterRegister;
    }

    public int getIconResource() {
        return iconResource;
    }

    public int getBackgroundColor() {
        return backgroundColor;
    }

    private StormpathLoginConfig(Builder builder) {
        autoLoginAfterRegister = builder.autoLoginAfterRegister;
        iconResource = builder.iconResource;
        backgroundColor = builder.backgroundResource;
    }

    public static class Builder {

        private boolean autoLoginAfterRegister = false;
        private int iconResource = 0; //0 is the fallback illegal identifier that doesn't throw a FATAL EXCEPTION
        private int backgroundResource = 0;

        public Builder autoLoginAfterRegister(boolean autoLogin) {
            this.autoLoginAfterRegister = autoLogin;
            return this;
        }

        public Builder setIcon(int icon){
            this.iconResource = icon;
            return this;
        }

        public Builder setBackgroundColor(int background){
            this.backgroundResource = background;
            return this;
        }

        public StormpathLoginConfig build() {
            return new StormpathLoginConfig(this);
        }

        public Bundle create(){
            Bundle mBundle = new Bundle();
            mBundle.putInt("icon", this.iconResource);
            mBundle.putInt("backgroundColor", this.backgroundResource);

            return mBundle;
        }


    } //end Builder


}
