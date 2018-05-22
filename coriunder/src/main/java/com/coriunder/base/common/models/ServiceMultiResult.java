package com.coriunder.base.common.models;

import java.util.ArrayList;

/**
 * Common service response data
 */
@SuppressWarnings("unused")
public class ServiceMultiResult extends ServiceResult {
    private long recordNumber;
    private ArrayList<String> refNumbers;

    /**
     * Constructor.
     * Sets default non-null values for all variables.
     * To set custom values call setter methods.
     */
    public ServiceMultiResult() {
        recordNumber = 0;
        refNumbers = new ArrayList<>();
        code = 0;
        isSuccess = false;
        key = "";
        message = "";
        number = "";
    }

    public long getRecordNumber() { return recordNumber; }
    public void setRecordNumber(long recordNumber) { this.recordNumber = recordNumber; }

    public ArrayList<String> getRefNumbers() { return refNumbers; }
    public void setRefNumbers(ArrayList<String> refNumbers) { this.refNumbers = refNumbers; }
}