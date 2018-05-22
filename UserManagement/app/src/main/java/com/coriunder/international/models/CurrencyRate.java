package com.coriunder.international.models;

/**
 * Information about currency rate
 */
@SuppressWarnings("unused")
public class CurrencyRate {
    private String key;
    private double value;

    /**
     * Constructor.
     * Sets default non-null values for all variables.
     * To set custom values call setter methods.
     */
    public CurrencyRate() {
        key = "";
        value = 0;
    }

    public String getKey() { return key; }
    public void setKey(String key) { this.key = key; }

    public double getValue() { return value; }
    public void setValue(double value) { this.value = value; }
}