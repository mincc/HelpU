package com.example.chungmin.helpu;

import java.io.Serializable;
import android.app.Activity;
import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import fragments.CreateQuotationFragment;
import fragments.JobDoneFragment;

import com.ipay.Ipay;
import com.ipay.IpayPayment;
import com.ipay.IpayR;
import com.ipay.IpayResultDelegate;

public class Test extends Activity {

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test);

//        if (savedInstanceState == null) {
//            FragmentManager fm = getFragmentManager();
//            FragmentTransaction ft = fm.beginTransaction();
//            Fragment frag = new JobDoneFragment().newInstance(9);
//            ft.add(R.id.llDisplaySection, frag);
//            ft.commit();
//        }

        Button button = (Button) findViewById(R.id.ipay88);

        button.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                makePayment();
            }
        });
    }

    @Override
    protected void onStart() {
        super.onStart();
    }

    private void makePayment() {

        IpayPayment payment = new IpayPayment();
        payment.setMerchantKey("HQgUUZLVzg");
        payment.setMerchantCode("M01235");
        payment.setRefNo(Long.toString(22));
        payment.setAmount("1.00");
        payment.setProdDesc("Print Cards,Stickers,Magnets");
        payment.setUserName("Mani");
        payment.setUserEmail("smani14@gmail.com");
        payment.setUserContact("3202008821");
        payment.setRemark("Remark");
        payment.setCountry("MY");
        payment.setCurrency("MYR");
        payment.setPaymentId("1");
        payment.setLang("ISO-8859-1");
        payment.setBackendPostURL("http://www.webapi.anyonecanprint.com");


        Intent checkoutIntent = Ipay.getInstance().checkout(payment, this, new IpayResultDelegate() {
            @Override
            public void onPaymentSucceeded(String TransId, String RefNo, String Amount, String Remark, String AuthCode) {

                System.out.println("######## onPayment Succeeded ###### " + TransId + " : " + RefNo + " :" + Amount + " : " + RefNo + " : " + AuthCode);
            }

            @Override
            public void onPaymentFailed(String TransId, String RefNo, String Amount, String Remark, String ErrDesc) {
                System.out.println("########  onPaymentFailed ###### " + TransId + " : " + RefNo + " :" + Amount + " : " + RefNo + " : " + ErrDesc);
            }

            @Override
            public void onPaymentCanceled(String TransId, String RefNo, String Amount, String Remark, String ErrDesc) {
                System.out.println("######## onPaymentCanceled  ###### " + TransId + " : " + RefNo + " :" + Amount + " : " + RefNo + " : " + ErrDesc);
            }

            @Override
            public void onRequeryResult(String MerchantCode, String RefNo, String
                    Amount, String Result) {

            }
        });
        startActivityForResult(checkoutIntent, 1001);
    }

}