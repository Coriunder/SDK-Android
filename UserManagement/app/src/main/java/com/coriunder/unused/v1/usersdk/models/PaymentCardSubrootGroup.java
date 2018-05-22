package com.coriunder.unused.v1.usersdk.models;

/**
 * Created by 1 on 18.02.2016.
 */
public class PaymentCardSubrootGroup {

    private String key;
    private String name;
    private String icon;
    private String groupKey;
    private String value1Caption;
    private String value1ValidationRegex;
    private String value2Caption;
    private String value2ValidationRegex;
    private boolean hasExpirationDate;

    public PaymentCardSubrootGroup() {
        key = "";
        name = "";
        icon = "";
        groupKey = "";
        value1Caption = "";
        value1ValidationRegex = "";
        value2Caption = "";
        value2ValidationRegex = "";
        hasExpirationDate = false;
    }

    public String getKey() { return key; }
    public void setKey(String key) { this.key = key; }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public String getIcon() { return icon; }
    public void setIcon(String icon) { this.icon = icon; }

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

    public boolean isHasExpirationDate() { return hasExpirationDate; }
    public void setHasExpirationDate(boolean hasExpirationDate) { this.hasExpirationDate = hasExpirationDate; }
}