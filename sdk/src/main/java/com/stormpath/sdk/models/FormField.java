package com.stormpath.sdk.models;

/**
 * Created by edjiang on 1/5/17.
 */
public class FormField {
    private String name;
    private String label;
    private String placeholder;
    private Boolean required;
    private String type;

    public String getName() {
        return name;
    }

    public String getLabel() {
        return label;
    }

    public String getPlaceholder() {
        return placeholder;
    }

    public Boolean getRequired() {
        return required;
    }

    public String getType() {
        return type;
    }
}