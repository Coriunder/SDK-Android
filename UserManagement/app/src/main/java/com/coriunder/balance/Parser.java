package com.coriunder.balance;

import android.util.Log;

import com.coriunder.R;
import com.coriunder.balance.models.BalanceRow;
import com.coriunder.balance.models.BalanceTotal;
import com.coriunder.balance.models.Request;
import com.coriunder.base.Coriunder;
import com.coriunder.base.utils.BaseParser;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;

/**
 * Parser methods for Balance services
 */
public class Parser extends BaseParser {
    /**
     * Method to parse JSONObject to Request object
     * @param jsonResult JSONObject to parse
     * @return Request object
     */
    protected static Request parseRequest(JSONObject jsonResult) {
        Request item = new Request();
        item.setAmount(getDouble(jsonResult, "Amount"));
        item.setConfirmDate(getReadableDate(jsonResult, "ConfirmDate"));
        item.setCurrencyISOCode(getString(jsonResult, "CurrencyISOCode"));
        item.setRequestId(getLong(jsonResult, "ID"));
        item.setApproved(getBoolean(jsonResult, "IsApproved"));
        item.setPush(getBoolean(jsonResult, "IsPush"));
        item.setRequestDate(getReadableDate(jsonResult, "RequestDate"));
        item.setSourceAccountId(getLong(jsonResult, "SourceAccountID"));
        item.setSourceAccountName(getString(jsonResult, "SourceAccountName"));
        item.setSourceText(getString(jsonResult, "SourceText"));
        item.setTargetAccountId(getLong(jsonResult, "TargetAccountID"));
        item.setTargetAccountName(getString(jsonResult, "TargetAccountName"));
        item.setTargetText(getString(jsonResult, "TargetText"));
        return item;
    }

    /**
     * Method to parse JSONObject to ArrayList containing Request objects
     * @param response JSONObject to parse
     * @return ArrayList containing Request objects
     */
    protected static ArrayList<Request> parseRequests(JSONObject response) {
        JSONArray result = Parser.getJsonArray(response, "d");
        ArrayList<Request> arrayList = new ArrayList<>();
        if (result != null) {
            for (int i=0; i<result.length(); i++) {
                try {
                    JSONObject requestObj = result.getJSONObject(i);
                    Request item = Parser.parseRequest(requestObj);
                    arrayList.add(item);
                } catch (Exception e) {
                    Log.e(Coriunder.getContext().getString(R.string.balance_log),
                            Coriunder.getContext().getString(R.string.balance_error_request) + i);
                }
            }
        }
        return arrayList;
    }

    /**
     * Method to parse JSONObject to ArrayList containing BalanceRow objects
     * @param response JSONObject to parse
     * @return ArrayList containing BalanceRow objects
     */
    protected static ArrayList<BalanceRow> parseRows(JSONObject response) {
        JSONArray result = Parser.getJsonArray(response, "d");
        ArrayList<BalanceRow> arrayList = new ArrayList<>();
        if (result != null) {
            for (int i=0; i<result.length(); i++) {
                try {
                    JSONObject requestObj = result.getJSONObject(i);
                    BalanceRow item = new BalanceRow();
                    item.setAmount(Parser.getDouble(requestObj, "Amount"));
                    item.setCurrencyIso(Parser.getString(requestObj, "CurrencyIso"));
                    item.setBalanceRowId(Parser.getLong(requestObj, "ID"));
                    item.setInsertDate(Parser.getReadableDate(requestObj, "InsertDate"));
                    item.setPending(Parser.getBoolean(requestObj, "IsPending"));
                    item.setSourceId(Parser.getLong(requestObj, "SourceID"));
                    item.setSourceType(Parser.getString(requestObj, "SourceType"));
                    item.setText(Parser.getString(requestObj, "Text"));
                    item.setTotal(Parser.getDouble(requestObj, "Total"));
                    arrayList.add(item);
                } catch (Exception e) {
                    Log.e(Coriunder.getContext().getString(R.string.balance_log),
                            Coriunder.getContext().getString(R.string.balance_error_balance) + i);
                }
            }
        }
        return arrayList;
    }

    /**
     * Method to parse JSONObject to ArrayList containing BalanceTotal objects
     * @param response JSONObject to parse
     * @return ArrayList containing BalanceTotal objects
     */
    protected static ArrayList<BalanceTotal> parseTotal(JSONObject response) {
        JSONArray result = Parser.getJsonArray(response, "d");
        ArrayList<BalanceTotal> arrayList = new ArrayList<>();
        if (result != null) {
            for (int i=0; i<result.length(); i++) {
                try {
                    JSONObject requestObj = result.getJSONObject(i);
                    BalanceTotal item = new BalanceTotal();
                    item.setCurrencyIso(Parser.getString(requestObj, "CurrencyIso"));
                    item.setCurrent(Parser.getDouble(requestObj, "Current"));
                    item.setExpected(Parser.getDouble(requestObj, "Expected"));
                    item.setPending(Parser.getDouble(requestObj, "Pending"));
                    arrayList.add(item);
                } catch (Exception e) {
                    Log.e(Coriunder.getContext().getString(R.string.balance_log),
                            Coriunder.getContext().getString(R.string.balance_error_balance) + i);
                }
            }
        }
        return arrayList;
    }
}