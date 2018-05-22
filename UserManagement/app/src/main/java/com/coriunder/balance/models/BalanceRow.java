package com.coriunder.balance.models;

import java.util.Date;

/**
 * Transactions' history item
 */
@SuppressWarnings("unused")
public class BalanceRow {
    private double amount;
    private String currencyIso;
    private long balanceRowId;
    private Date insertDate;
    private boolean isPending;
    private long sourceId;
    private String sourceType;
    private String text;
    private double total;

    /**
     * Constructor.
     * Sets default non-null values for all variables.
     * To set custom values call setter methods.
     */
    public BalanceRow() {
        amount = 0;
        currencyIso = "";
        balanceRowId = 0;
        insertDate = new Date();
        isPending = false;
        sourceId = 0;
        sourceType = "";
        text = "";
        total = 0;
    }

    public String getCurrencyIso() { return currencyIso; }
    public void setCurrencyIso(String currencyIso) { this.currencyIso = currencyIso; }

    public double getAmount() { return amount; }
    public void setAmount(double amount) { this.amount = amount; }

    public double getTotal() { return total; }
    public void setTotal(double total) { this.total = total; }

    public String getText() { return text; }
    public void setText(String text) { this.text = text; }

    public long getSourceId() { return sourceId; }
    public void setSourceId(long sourceId) { this.sourceId = sourceId; }

    public Date getInsertDate() { return insertDate; }
    public void setInsertDate(Date insertDate) { this.insertDate = insertDate; }

    public String getSourceType() { return sourceType; }
    public void setSourceType(String sourceType) { this.sourceType = sourceType; }

    public long getBalanceRowId() { return balanceRowId; }
    public void setBalanceRowId(long balanceRowId) { this.balanceRowId = balanceRowId; }

    public boolean isPending() { return isPending; }
    public void setPending(boolean isPending) { this.isPending = isPending; }
}