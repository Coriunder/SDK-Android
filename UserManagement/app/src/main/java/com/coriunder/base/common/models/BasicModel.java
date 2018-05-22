package com.coriunder.base.common.models;

/**
 * Commonly used data structure.
 * Extended by Country, Language, State, PaymentCardRootGroup, PaymentCartSubRootGroup models
 */
@SuppressWarnings("unused")
public class BasicModel {
    protected String icon;
    protected String key;
    protected String name;

    public String getIcon() { return icon; }
    public void setIcon(String icon) { this.icon = icon; }

    public String getKey() { return key; }
    public void setKey(String key) { this.key = key; }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }
}