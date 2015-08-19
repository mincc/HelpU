package com.example.chungmin.helpu;

/**
 * Created by Chung Min on 8/13/2015.
 */

import android.os.Bundle;

import com.ipay.IpayResultDelegate;

import java.io.Serializable;

public class PaymentResultDelegate implements IpayResultDelegate, Serializable {

    private static String transId = "";
    private static String refNo = "";
    private static String amount = "";
    private static String remark = "";
    private static String authCode = "";
    private static String errDesc = "";

    public String getTransId() {
        return transId;
    }

    public void setTransId(String transId) {
        this.transId = transId;
    }

    public String getRefNo() {
        return refNo;
    }

    public void setRefNo(String refNo) {
        this.refNo = refNo;
    }

    public String getAmount() {
        return amount;
    }

    public void setAmount(String amount) {
        this.amount = amount;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    public String getAuthCode() {
        return authCode;
    }

    public void setAuthCode(String authCode) {
        this.authCode = authCode;
    }

    public String getErrDesc() {
        return errDesc;
    }

    public void setErrDesc(String errDesc) {
        this.errDesc = errDesc;
    }

    @Override
    public void onPaymentSucceeded(String TransId, String RefNo,
                                   String Amount, String Remark, String AuthCode) {

        ProjectMessages.resultTitle = "SUCCESS";
        ProjectMessages.resultInfo = "You have successfully completed your transaction.";

        String extra = "";
        extra = extra + "TransId\t= " + TransId + "\n";
        extra = extra + "RefNo\t\t= " + RefNo + "\n";
        extra = extra + "Amount\t= " + Amount + "\n";
        extra = extra + "Remark\t= " + Remark + "\n";
        extra = extra + "AuthCode\t= " + AuthCode;
        ProjectMessages.resultExtra = extra;

        setTransId(TransId);
        setRefNo(RefNo);
        setAmount(Amount);
        setRemark(Remark);
        setAuthCode(AuthCode);
        setErrDesc("");

    }

    @Override
    public void onPaymentFailed(String TransId, String RefNo, String Amount,
                                String Remark, String ErrDesc) {

        ProjectMessages.resultTitle = "FAILURE";
        ProjectMessages.resultInfo = ErrDesc;

        String extra = "";
        extra = extra + "TransId\t= " + TransId + "\n";
        extra = extra + "RefNo\t\t= " + RefNo + "\n";
        extra = extra + "Amount\t= " + Amount + "\n";
        extra = extra + "Remark\t= " + Remark + "\n";
        extra = extra + "ErrDesc\t= " + ErrDesc;
        ProjectMessages.resultExtra = extra;

        setTransId(TransId);
        setRefNo(RefNo);
        setAmount(Amount);
        setRemark(Remark);
        setAuthCode("");
        setErrDesc(ErrDesc);
    }

    @Override
    public void onPaymentCanceled(String TransId, String RefNo, String Amount,
                                  String Remark, String ErrDesc) {

        ProjectMessages.resultTitle = "CANCELED";
        ProjectMessages.resultInfo = "The transaction has been cancelled.";

        String extra = "";
        extra = extra + "TransId\t= " + TransId + "\n";
        extra = extra + "RefNo\t\t= " + RefNo + "\n";
        extra = extra + "Amount\t= " + Amount + "\n";
        extra = extra + "Remark\t= " + Remark + "\n";
        extra = extra + "ErrDesc\t= " + ErrDesc;
        ProjectMessages.resultExtra = extra;

        setTransId(TransId);
        setRefNo(RefNo);
        setAmount(Amount);
        setRemark(Remark);
        setAuthCode("");
        setErrDesc(ErrDesc);
    }

    @Override
    public void onRequeryResult(String MerchantCode, String RefNo,
                                String Amount, String Result) {
        ProjectMessages.resultTitle = "Requery Result";
        ProjectMessages.resultInfo = "";

        String extra = "";
        extra = extra + "MerchantCode\t= " + MerchantCode + "\n";
        extra = extra + "RefNo\t\t= " + RefNo + "\n";
        extra = extra + "Amount\t= " + Amount + "\n";
        extra = extra + "Result\t= " + Result;
        ProjectMessages.resultExtra = extra;

    }
}