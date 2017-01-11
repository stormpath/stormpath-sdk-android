package com.stormpath.sdk.models;

import java.util.List;

/**
 * Created by edjiang on 1/5/17.
 */
public class LoginModel {
    private List<FormField> formFields;
    private List<AccountStore> accountStores;

    public List<FormField> getFormFields() {
        return formFields;
    }

    public List<AccountStore> getAccountStores() {
        return accountStores;
    }
}
