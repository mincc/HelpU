package com.example.chungmin.helpu;

/**
 * Created by Chung Min on 8/13/2015.
 */

import java.io.Serializable;

import com.ipay.IpayResultDelegate;

public class IPay88ResultDelegate implements IpayResultDelegate, Serializable {

    private static final long serialVersionUID = 10001L;

    @Override
    public void onPaymentSucceeded(String TransId, String RefNo,
                                   String Amount, String Remark, String AuthCode) {

        IPay88SampleActivity.resultTitle = "SUCCESS";
        IPay88SampleActivity.resultInfo = "You have successfully completed your transaction.";

        String extra = "";
        extra = extra + "TransId\t= " + TransId + "\n";
        extra = extra + "RefNo\t\t= " + RefNo + "\n";
        extra = extra + "Amount\t= " + Amount + "\n";
        extra = extra + "Remark\t= " + Remark + "\n";
        extra = extra + "AuthCode\t= " + AuthCode;
        IPay88SampleActivity.resultExtra = extra;

    }

    @Override
    public void onPaymentFailed(String TransId, String RefNo, String Amount,
                                String Remark, String ErrDesc) {

        IPay88SampleActivity.resultTitle = "FAILURE";
        IPay88SampleActivity.resultInfo = ErrDesc;

        String extra = "";
        extra = extra + "TransId\t= " + TransId + "\n";
        extra = extra + "RefNo\t\t= " + RefNo + "\n";
        extra = extra + "Amount\t= " + Amount + "\n";
        extra = extra + "Remark\t= " + Remark + "\n";
        extra = extra + "ErrDesc\t= " + ErrDesc;
        IPay88SampleActivity.resultExtra = extra;

    }

    @Override
    public void onPaymentCanceled(String TransId, String RefNo, String Amount,
                                  String Remark, String ErrDesc) {

        IPay88SampleActivity.resultTitle = "CANCELED";
        IPay88SampleActivity.resultInfo = "The transaction has been cancelled.";

        String extra = "";
        extra = extra + "TransId\t= " + TransId + "\n";
        extra = extra + "RefNo\t\t= " + RefNo + "\n";
        extra = extra + "Amount\t= " + Amount + "\n";
        extra = extra + "Remark\t= " + Remark + "\n";
        extra = extra + "ErrDesc\t= " + ErrDesc;
        IPay88SampleActivity.resultExtra = extra;

    }

    @Override
    public void onRequeryResult(String MerchantCode, String RefNo,
                                String Amount, String Result) {
        IPay88SampleActivity.resultTitle = "Requery Result";
        IPay88SampleActivity.resultInfo = "";

        String extra = "";
        extra = extra + "MerchantCode\t= " + MerchantCode + "\n";
        extra = extra + "RefNo\t\t= " + RefNo + "\n";
        extra = extra + "Amount\t= " + Amount + "\n";
        extra = extra + "Result\t= " + Result;
        IPay88SampleActivity.resultExtra = extra;

    }

}