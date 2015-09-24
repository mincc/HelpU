package com.example.chungmin.helpu.models;

/**
 * Created by Chung Min on 8/13/2015.
 */
public class Transaction {
    private String transactionId, refNo, amount, remark, authCode, errDesc;
    private int id;

    public Transaction(String transactionId, String refNo, String amount, String remark, String authCode, String errDesc) {
        this.transactionId = transactionId;
        this.refNo = refNo;
        this.amount = amount;
        this.remark = remark;
        this.authCode = authCode;
        this.errDesc = errDesc;
    }

    public Transaction(int id, String transactionId, String refNo, String amount, String remark, String authCode, String errDesc) {
        this.id = id;
        this.transactionId = transactionId;
        this.refNo = refNo;
        this.amount = amount;
        this.remark = remark;
        this.authCode = authCode;
        this.errDesc = errDesc;
    }

    public Transaction() {

    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getTransactionId() {
        return transactionId;
    }

    public void setTransactionId(String transactionId) {
        this.transactionId = transactionId;
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

}
