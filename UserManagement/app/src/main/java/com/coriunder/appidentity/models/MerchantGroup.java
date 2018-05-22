package com.coriunder.appidentity.models;

/**
 * Information about a merchant group
 */
@SuppressWarnings("unused")
public class MerchantGroup {
    private int key;
    private String value;

    /**
     * Constructor.
     * Sets default non-null values for all variables.
     * To set custom values call setter methods.
     */
    public MerchantGroup() {
        key = 0;
        value = "";
    }

    public int getKey() { return key; }
    public void setKey(int key) { this.key = key; }

    public String getValue() { return value; }
    public void setValue(String value) { this.value = value; }
}