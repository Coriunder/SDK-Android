package com.coriunder.balance.models;

/**
 * Info about total balances
 */
@SuppressWarnings("unused")
public class BalanceTotal {
    private String currencyIso;
    private double current;
    private double expected;
    private double pending;

    /**
     * Constructor.
     * Sets default non-null values for all variables.
     * To set custom values call setter methods.
     */
    public BalanceTotal() {
        currencyIso = "";
        current = 0;
        expected = 0;
        pending = 0;
    }

    public String getCurrencyIso() { return currencyIso; }
    public void setCurrencyIso(String currencyIso) { this.currencyIso = currencyIso; }

    public double getCurrent() { return current; }
    public void setCurrent(double current) { this.current = current; }

    public double getExpected() { return expected; }
    public void setExpected(double expected) { this.expected = expected; }

    public double getPending() { return pending; }
    public void setPending(double pending) { this.pending = pending; }
}