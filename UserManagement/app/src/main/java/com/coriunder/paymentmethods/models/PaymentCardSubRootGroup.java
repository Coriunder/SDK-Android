package com.coriunder.paymentmethods.models;

import com.coriunder.base.common.models.BasicModel;

/**
 * Sub root group for payment method types
 */
@SuppressWarnings("unused")
public class PaymentCardSubRootGroup extends BasicModel {
    private String groupKey;
    private boolean hasExpirationDate;
    private String value1Caption;
    private String value1ValidationRegex;
    private String value2Caption;
    private String value2ValidationRegex;

    /**
     * Constructor.
     * Sets default non-null values for all variables.
     * To set custom values call setter methods.
     */
    public PaymentCardSubRootGroup() {
        icon = "";
        key = "";
        name = "";
        groupKey = "";
        hasExpirationDate = false;
        value1Caption = "";
        value1ValidationRegex = "";
        value2Caption = "";
        value2ValidationRegex = "";
    }

    public String getGroupKey() { return groupKey; }
    public void setGroupKey(String groupKey) { this.groupKey = groupKey; }

    public String getValue1Caption() { return value1Caption; }
    public void setValue1Caption(String value1Caption) { this.value1Caption = value1Caption; }

    public String getValue1ValidationRegex() { return value1ValidationRegex; }
    public void setValue1ValidationRegex(String value1ValidationRegex) { this.value1ValidationRegex = value1ValidationRegex; }

    public String getValue2Caption() { return value2Caption; }
    public void setValue2Caption(String value2Caption) { this.value2Caption = value2Caption; }

    public String getValue2ValidationRegex() { return value2ValidationRegex; }
    public void setValue2ValidationRegex(String value2ValidationRegex) { this.value2ValidationRegex = value2ValidationRegex; }

    public boolean hasExpirationDate() { return hasExpirationDate; }
    public void setHasExpirationDate(boolean hasExpirationDate) { this.hasExpirationDate = hasExpirationDate; }
}