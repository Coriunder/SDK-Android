package com.coriunder.unused.v1.usersdk.models;

import java.util.ArrayList;

/**
 * Created by 1 on 18.02.2016.
 */
public class PaymentCardRootGroup extends BasicModel {
    protected ArrayList<PaymentCardSubrootGroup> subGroups;

    public PaymentCardRootGroup() {
        key = "";
        name = "";
        icon = "";
        subGroups = new ArrayList<>();
    }

    public ArrayList<PaymentCardSubrootGroup> getSubGroups() { return subGroups; }
    public void setSubGroups(ArrayList<PaymentCardSubrootGroup> subGroups) { this.subGroups = subGroups; }
}