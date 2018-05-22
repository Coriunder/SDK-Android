package com.coriunder.transactions;

import android.util.Log;

import com.coriunder.R;
import com.coriunder.base.Coriunder;
import com.coriunder.base.utils.BaseParser;
import com.coriunder.transactions.models.Transaction;
import com.coriunder.transactions.models.TransactionLookup;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;

/**
 * Parser methods for Transactions services
 */
public class Parser extends BaseParser {
    /**
     * Method to parse JSONObject to Transaction object
     * @param transObj JSONObject to parse
     * @return Transaction object
     */
    protected static Transaction parseTransaction(JSONObject transObj) {
        Transaction transaction = new Transaction();
        transaction.setAmount(getDouble(transObj, "Amount"));
        transaction.setAuthCode(getString(transObj, "AuthCode"));
        transaction.setComment(getString(transObj, "Comment"));
        transaction.setCurrencyIso(getString(transObj, "CurrencyIso"));
        transaction.setTransactionId(getLong(transObj, "ID"));
        transaction.setInsertDate(getReadableDate(transObj, "InsertDate"));
        transaction.setInstallments(getInt(transObj, "Installments"));
        transaction.setManual(getBoolean(transObj, "IsManual"));
        transaction.setRefunded(getBoolean(transObj, "IsRefunded"));
        transaction.setMerchant(parseMerchant(getJsonObject(transObj, "Merchant"), R.string.transactions_log));

        // Parse payer data
        JSONObject payerData = getJsonObject(transObj, "PayerData");
        if (payerData != null) {
            transaction.setPayerEmail(getString(payerData, "Email"));
            transaction.setPayerFullName(getString(payerData, "FullName"));
            transaction.setPayerPhone(getString(payerData, "Phone"));
            transaction.setPayerShippingAddress(parseAddress(getJsonObject(payerData, "ShippingAddress")));
        }

        // Parse payment data
        JSONObject paymentData = getJsonObject(transObj, "PaymentData");
        if (paymentData != null) {
            transaction.setPaymentDataBillingAddress(parseAddress(getJsonObject(paymentData, "BillingAddress")));
            transaction.setPaymentDataBin(getString(paymentData, "Bin"));
            transaction.setPaymentDataBinCountry(getString(paymentData, "BinCountry"));
            transaction.setPaymentDataExpirationMonth(getInt(paymentData, "ExpirationMonth"));
            transaction.setPaymentDataExpirationYear(getInt(paymentData, "ExpirationYear"));
            transaction.setPaymentDataLast4(getString(paymentData, "Last4"));
            transaction.setPaymentDataType(getString(paymentData, "Type"));
        }

        transaction.setPaymentDisplay(getString(transObj, "PaymentDisplay"));
        transaction.setPaymentMethodGroupKey(getString(transObj, "PaymentMethodGroupKey"));
        transaction.setPaymentMethodKey(getString(transObj, "PaymentMethodKey"));
        transaction.setReceiptLink(getString(transObj, "ReceiptLink"));
        transaction.setReceiptText(getString(transObj, "ReceiptText"));
        transaction.setText(getString(transObj, "Text"));

        return transaction;
    }

    /**
     * Method to parse JSONObject to ArrayList containing TransactionLookup objects
     * @param response JSONObject to parse
     * @return ArrayList containing TransactionLookup objects
     */
    protected static ArrayList<TransactionLookup> parseTransactionLookup(JSONObject response) {
        JSONArray result = Parser.getJsonArray(response, "d");
        ArrayList<TransactionLookup> arrayList = new ArrayList<>();
        if (result != null) {
            for (int i=0; i<result.length(); i++) {
                try {
                    JSONObject transObj = result.getJSONObject(i);
                    TransactionLookup item = new TransactionLookup();
                    item.setMerchantName(Parser.getString(transObj, "MerchantName"));
                    item.setMerchantSupportEmail(Parser.getString(transObj, "MerchantSupportEmail"));
                    item.setMerchantSupportPhone(Parser.getString(transObj, "MerchantSupportPhone"));
                    item.setMerchantWebSite(Parser.getString(transObj, "MerchantWebSite"));
                    item.setMethodString(Parser.getString(transObj, "MethodString"));
                    item.setTransactionDate(Parser.getReadableDate(transObj, "TransDate"));
                    item.setTransactionID(Parser.getLong(transObj, "TransID"));
                    arrayList.add(item);
                } catch (Exception e) {
                    Log.e(Coriunder.getContext().getString(R.string.transactions_log),
                            Coriunder.getContext().getString(R.string.transactions_error_lookup) + i);
                }
            }
        }
        return arrayList;
    }

    /**
     * Method to parse JSONObject to ArrayList containing Transaction objects
     * @param response JSONObject to parse
     * @return ArrayList containing Transaction objects
     */
    protected static ArrayList<Transaction> parseSearch(JSONObject response) {
        JSONArray result = Parser.getJsonArray(response, "d");
        ArrayList<Transaction> arrayList = new ArrayList<>();
        if (result != null) {
            for (int i=0; i<result.length(); i++) {
                try {
                    JSONObject transObj = result.getJSONObject(i);
                    Transaction transaction = parseTransaction(transObj);
                    arrayList.add(transaction);
                } catch (Exception e) {
                    Log.e(Coriunder.getContext().getString(R.string.transactions_log),
                            Coriunder.getContext().getString(R.string.transactions_error_transaction)+i);
                }
            }
        }
        return arrayList;
    }
}