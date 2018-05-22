package com.coriunder.unused.v1.mobilesdk.models;

import java.math.BigDecimal;
import java.util.Date;

/**
 * Created by 1 on 25.02.2016.
 */
public class Transaction {
    public enum TransactionType { AUTHORIZE_AND_CAPTURE, AUTHORIZE_ONLY, REFUND }

    private BigDecimal baseAmount;
    private int tipPercent;
    private String expirationMonth;
    private String expirationYear;
    private byte[] signatureImage;
    private byte[] manualCardImage;
    private Integer installments;
    private String email;
    private String personalNumber;
    private String cardholderName;
    private String phone;
    private String currency;
    private String paymentDetails;
    private String comment;
    private String transactionID;
    private Date transactionDate;
    private String replyCode;
    private String replyText;
    private boolean isManual;
    private String cvv;
    private String track2;
    private String creditcardDisplay;
    private String creditcardInternal;
    private String qrTransactionCode;
    private boolean isProccessed;
    private boolean isRefunded;
    private boolean isAccepted;
    private TransactionType transactionType;

    public Transaction() {
        baseAmount = new BigDecimal("0.00");
        tipPercent = 0;
        expirationMonth = "";
        expirationYear = "";
        signatureImage = null;
        manualCardImage = null;
        installments = 1;
        email = "";
        personalNumber = "";
        cardholderName = "";
        phone = "";
        currency = "";
        paymentDetails = "";
        comment = "";
        transactionID = "";
        transactionDate = new Date();
        replyCode = "";
        replyText = "";
        isManual = false;
        cvv = "";
        track2 = "";
        creditcardDisplay = "";
        creditcardInternal = "";
        qrTransactionCode = "";
        isProccessed = false;
        isRefunded = false;
        isAccepted = false;
        transactionType = TransactionType.AUTHORIZE_AND_CAPTURE;
    }

    public BigDecimal getBaseAmount() { return baseAmount; }
    public void setBaseAmount(BigDecimal baseAmount) { this.baseAmount = baseAmount; }

    public int getTipPercent() { return tipPercent; }
    public void setTipPercent(int tipPercent) { this.tipPercent = tipPercent; }

    public String getExpirationMonth() { return expirationMonth; }
    public void setExpirationMonth(String expirationMonth) { this.expirationMonth = expirationMonth; }

    public String getExpirationYear() { return expirationYear; }
    public void setExpirationYear(String expirationYear) { this.expirationYear = expirationYear; }

    public byte[] getSignatureImage() { return signatureImage; }
    public void setSignatureImage(byte[] signatureImage) { this.signatureImage = signatureImage; }

    public byte[] getManualCardImage() { return manualCardImage; }
    public void setManualCardImage(byte[] manualCardImage) { this.manualCardImage = manualCardImage; }

    public Integer getInstallments() { return installments; }
    public void setInstallments(Integer installments) { this.installments = installments; }

    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }

    public String getPersonalNumber() { return personalNumber; }
    public void setPersonalNumber(String personalNumber) { this.personalNumber = personalNumber; }
    
    public String getCardholderName() { return cardholderName; }
    public void setCardholderName(String cardholderName) { this.cardholderName = cardholderName; }

    public String getPhone() { return phone; }
    public void setPhone(String phone) { this.phone = phone; }
    
    public String getCurrency() { return currency; }
    public void setCurrency(String currency) { this.currency = currency; }

    public String getPaymentDetails() { return paymentDetails; }
    public void setPaymentDetails(String paymentDetails) { this.paymentDetails = paymentDetails; }

    public String getComment() { return comment; }
    public void setComment(String comment) { this.comment = comment; }

    public String getTransactionID() { return transactionID; }
    public void setTransactionID(String transactionID) { this.transactionID = transactionID; }

    public Date getTransactionDate() { return transactionDate; }
    public void setTransactionDate(Date transactionDate) { this.transactionDate = transactionDate; }

    public String getReplyCode() { return replyCode; }
    public void setReplyCode(String replyCode) { this.replyCode = replyCode; }

    public String getReplyText() { return replyText; }
    public void setReplyText(String replyText) { this.replyText = replyText; }

    public boolean isManual() { return isManual; }
    public void setIsManual(boolean isManual) { this.isManual = isManual; }

    public String getCvv() { return cvv; }
    public void setCvv(String cvv) { this.cvv = cvv; }

    public String getTrack2() { return track2; }
    public void setTrack2(String track2) { this.track2 = track2; }

    public String getCreditcardDisplay() { return creditcardDisplay; }
    public void setCreditcardDisplay(String creditcardDisplay) { this.creditcardDisplay = creditcardDisplay; }

    public String getCreditcardInternal() { return creditcardInternal; }
    public void setCreditcardInternal(String creditcardInternal) { this.creditcardInternal = creditcardInternal; }

    public String getQrTransactionCode() { return qrTransactionCode; }
    public void setQrTransactionCode(String qrTransactionCode) { this.qrTransactionCode = qrTransactionCode; }

    public boolean isProccessed() { return isProccessed; }
    public void setIsProccessed(boolean isProccessed) { this.isProccessed = isProccessed; }

    public boolean isRefunded() { return isRefunded; }
    public void setIsRefunded(boolean isRefunded) { this.isRefunded = isRefunded; }

    public boolean isAccepted() { return isAccepted; }
    public void setIsAccepted(boolean isAccepted) { this.isAccepted = isAccepted; }

    public TransactionType getTransactionType() { return transactionType; }
    public void setTransactionType(TransactionType transactionType) { this.transactionType = transactionType; }

    public void clearSensitiveData() {
        creditcardInternal = "";
        cvv = "";
        track2 = "";
    }

    /*
    public BigDecimal getAmountPlusVat() {
        BigDecimal vatPercent = App.getMerchant().getSettings().valueAddedTax;
        BigDecimal amount;
        switch (vatMode) {
            case NO_VAT:
                amount = baseAmount;
                break;
            case INCLUDING_VAT:
                amount = baseAmount.add(baseAmount.multiply(vatPercent.movePointLeft(2)));
                break;
            case EXCLUDING_VAT:
                BigDecimal divider = vatPercent.add(BigDecimal.TEN.multiply(BigDecimal.TEN)).movePointLeft(2);
                amount = baseAmount.divide(divider, 2, BigDecimal.ROUND_HALF_UP);
                break;
            default:
                amount = baseAmount;
                break;
        }

        return amount.setScale(2, BigDecimal.ROUND_HALF_UP);
    }

    public BigDecimal getTotalAmount() {
        BigDecimal amountPlusVat = null;
        if (vatMode == VatMode.INCLUDING_VAT)
            amountPlusVat = getAmountPlusVat();
        else
            amountPlusVat = baseAmount;

        return amountPlusVat.add(getTipAmount()).setScale(2, BigDecimal.ROUND_HALF_UP);
    }

    public BigDecimal getTipAmount() {
        BigDecimal amountPlusVat = null;
        if (vatMode == VatMode.INCLUDING_VAT)
            amountPlusVat = getAmountPlusVat();
        else
            amountPlusVat = baseAmount;

        return amountPlusVat.multiply(new BigDecimal(this.tipPercent).movePointLeft(2)).setScale(2, BigDecimal.ROUND_HALF_UP);
    }

    public void setCreditcard(String creditcardNumber) {
        try {
            creditcardInternal = creditcardNumber;
            creditcardDisplay = Utils.getSafeCardNumber(creditcardInternal);
        } catch (Exception e) {
            Log.exception("error setting credit card", e, false);
        }
    }*/
}