package com.stormpath.sdk.ui;

import android.os.Bundle;

import java.io.Serializable;

public class StormpathLoginConfig implements Serializable {

    public static final String AUTO_LOGIN = "StormpathLoginActivity.AUTO_LOGIN_AFTER_REGISTER";

    private boolean autoLoginAfterRegister;

    public static StormpathLoginConfig fromBundle(Bundle bundle) {

        StormpathLoginConfig.Builder configBuilder = new Builder();

        if (bundle.containsKey(AUTO_LOGIN)) {
            configBuilder.autoLoginAfterRegister(bundle.getBoolean(AUTO_LOGIN));
        }

        return configBuilder.build();
    }

    private StormpathLoginConfig(Builder builder) {
        autoLoginAfterRegister = builder.autoLoginAfterRegister;
    }

    public static class Builder {

        private boolean autoLoginAfterRegister = false;

        public Builder autoLoginAfterRegister(boolean autoLogin) {
            this.autoLoginAfterRegister = autoLogin;
            return this;
        }

        public StormpathLoginConfig build() {
            return new StormpathLoginConfig(this);
        }
    }
}
