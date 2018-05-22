package com.coriunder.unused.v1.balancesdk.models;

import java.util.Date;

/**
 * Created by 1 on 22.02.2016.
 */
public class BalanceItem {

    private String currencyIso;
    private String amount;
    private String total;
    private String text;
    private String sourceId;
    private Date date;
    private String sourceType;
    private String balanceRowId;
    private boolean isPending;
    
    public BalanceItem() {
        currencyIso = "";
        amount = "";
        total = "";
        text = "";
        sourceId = "";
        date = new Date();
        sourceType = "";
        balanceRowId = "";
        isPending = false;
    }

    public String getCurrencyIso() { return currencyIso; }
    public void setCurrencyIso(String currencyIso) { this.currencyIso = currencyIso; }

    public String getAmount() { return amount; }
    public void setAmount(String amount) { this.amount = amount; }

    public String getTotal() { return total; }
    public void setTotal(String total) { this.total = total; }

    public String getText() { return text; }
    public void setText(String text) { this.text = text; }

    public String getSourceId() { return sourceId; }
    public void setSourceId(String sourceId) { this.sourceId = sourceId; }

    public Date getDate() { return date; }
    public void setDate(Date date) { this.date = date; }

    public String getSourceType() { return sourceType; }
    public void setSourceType(String sourceType) { this.sourceType = sourceType; }

    public String getBalanceRowId() { return balanceRowId; }
    public void setBalanceRowId(String balanceRowId) { this.balanceRowId = balanceRowId; }

    public boolean isPending() { return isPending; }
    public void setIsPending(boolean isPending) { this.isPending = isPending; }
}