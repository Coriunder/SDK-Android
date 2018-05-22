package com.coriunder.paymentmethods.models;

import com.coriunder.base.common.models.BasicModel;

import java.util.ArrayList;

/**
 * Root group for payment method types
 */
@SuppressWarnings("unused")
public class PaymentCardRootGroup extends BasicModel {
    private ArrayList<PaymentCardSubRootGroup> subGroups;

    /**
     * Constructor.
     * Sets default non-null values for all variables.
     * To set custom values call setter methods.
     */
    public PaymentCardRootGroup() {
        icon = "";
        key = "";
        name = "";
        subGroups = new ArrayList<>();
    }

    public ArrayList<PaymentCardSubRootGroup> getSubGroups() { return subGroups; }
    public void setSubGroups(ArrayList<PaymentCardSubRootGroup> subGroups) { this.subGroups = subGroups; }
}