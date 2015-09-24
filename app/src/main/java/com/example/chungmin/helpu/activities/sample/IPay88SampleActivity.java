package com.example.chungmin.helpu.activities.sample;

import android.os.Bundle;

import com.example.chungmin.helpu.activities.HelpUBaseActivity;
import com.example.chungmin.helpu.models.IPay88ResultDelegate;
import com.example.chungmin.helpu.R;
import com.ipay.Ipay;
import com.ipay.IpayPayment;
import com.ipay.IpayR;

import android.content.Intent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

public class IPay88SampleActivity extends HelpUBaseActivity implements OnClickListener {

    String MerchantKey = "HQgUUZLVzg";
    String MerchantCode = "M01235";

    String RefID = "Z001";
    String ProdDesc = "Galaxy Tab";
    String Amount = "1.0";
    String UserName = "Angela";
    String UserContact = "60123456789";
    String UserEmail = "angela@ipay.com";
    String Remark = "cheap cheap";

    String Country = "MY";


    private String PaymentId = "";
    private String Currency = "MYR";
    private String Lang = "ISO-8859-1";

    String backendPostURL = "www.google.com";


    public static String resultTitle;
    public static String resultInfo;
    public static String resultExtra;

    TextView txtResultTitle;
    TextView txtResultDescription;
    TextView txtResultDetails;

    EditText txtRefID = null;
    EditText txtProdDesc = null;
    EditText txtAmount = null;
    EditText txtUserName = null;
    EditText txtUserContact = null;
    EditText txtUserEmail = null;

    EditText txtMerchantCode = null;
    EditText txtMerchantKey = null;
    EditText txtPaymentID = null;
    EditText txtCurrency = null;
    EditText txtLang = null;
    EditText txtRemark = null;

    EditText txtCountry = null;

    EditText txtBackendPost = null;


    Button payButton = null;

    Button requeryButton = null;

    @Override
    public void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ipay88_sample);

        txtResultTitle = (TextView) findViewById(R.id.txtResultTitle);
        txtResultDescription = (TextView) findViewById(R.id.txtResultDescription);
        txtResultDetails = (TextView) findViewById(R.id.txtResultDetails);


        txtMerchantCode = (EditText) findViewById(R.id.txtMerchantCode);
        txtMerchantKey = (EditText) findViewById(R.id.txtMerchantKey);
        txtPaymentID = (EditText) findViewById(R.id.txtPaymentID);
        txtCurrency = (EditText) findViewById(R.id.txtCurrency);
        txtLang = (EditText) findViewById(R.id.txtLang);
        txtRemark = (EditText) findViewById(R.id.txtRemark);

        txtCountry = (EditText) findViewById(R.id.txtCountry);

        txtMerchantCode.setText(MerchantCode);
        txtMerchantKey.setText(MerchantKey);
        txtPaymentID.setText(PaymentId);
        txtCurrency.setText(Currency);
        txtLang.setText(Lang);
        txtRemark.setText(Remark);

        txtCountry.setText(Country);

        txtBackendPost = (EditText) findViewById(R.id.txtBackendPost);
        txtBackendPost.setText(backendPostURL);

        txtRefID = (EditText) findViewById(R.id.txtRefID);
        txtProdDesc = (EditText) findViewById(R.id.txtProdDesc);
        txtAmount = (EditText) findViewById(R.id.txtAmount);
        txtUserName = (EditText) findViewById(R.id.txtUserName);
        txtUserContact = (EditText) findViewById(R.id.txtUserContact);
        txtUserEmail = (EditText) findViewById(R.id.txtUserEmail);

        txtRefID.setText(RefID);
        txtProdDesc.setText(ProdDesc);
        txtAmount.setText(Amount);
        txtUserName.setText(UserName);
        txtUserContact.setText(UserContact);
        txtUserEmail.setText(UserEmail);

        try {
            payButton = (Button) findViewById(R.id.btnPay);
            payButton.setOnClickListener(this);

            requeryButton = (Button) findViewById(R.id.btnRequery);
            requeryButton.setOnClickListener(this);
        } catch (Exception ex) {
            ex.printStackTrace();
        }


    }

    @Override
    public void onClick(View v) {
        if (v == payButton) {

            IpayPayment payment = new IpayPayment();


            payment.setMerchantCode(txtMerchantCode.getText().toString());
            payment.setMerchantKey(txtMerchantKey.getText().toString());
            payment.setPaymentId(txtPaymentID.getText().toString());
            payment.setCurrency(txtCurrency.getText().toString());
            payment.setLang(txtLang.getText().toString());

            payment.setRemark(txtRemark.getText().toString());

            payment.setRefNo(txtRefID.getText().toString());
            payment.setAmount(txtAmount.getText().toString());
            payment.setProdDesc(txtProdDesc.getText().toString());
            payment.setUserName(txtUserName.getText().toString());
            payment.setUserEmail(txtUserEmail.getText().toString());
            payment.setUserContact(txtUserContact.getText().toString());

            payment.setCountry(txtCountry.getText().toString());
            payment.setBackendPostURL(txtBackendPost.getText().toString());


            Intent checkoutIntent = Ipay.getInstance().checkout(payment, this, new IPay88ResultDelegate());

            startActivityForResult(checkoutIntent, 1);
        } else if (v == requeryButton) {

            try {
                String local_MerchantCode = txtMerchantCode.getText().toString();
                String local_RefID = txtRefID.getText().toString();
                String local_Amount = txtAmount.getText().toString();

                IpayR r = new IpayR();
                r.setMerchantCode(local_MerchantCode);
                r.setRefNo(local_RefID);
                r.setAmount(local_Amount);

                Intent checkoutIntent = Ipay.getInstance().requery
                        (
                                r,
                                this, new IPay88ResultDelegate());

                startActivityForResult(checkoutIntent, 1);
            } catch (Exception e) {
                e.printStackTrace();
            }

        }
    }

    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode != 1)
            return;

        txtResultTitle.setVisibility(View.GONE);
        txtResultDescription.setVisibility(View.GONE);
        txtResultDetails.setVisibility(View.GONE);

        if (resultTitle != null) {

            txtResultTitle.setText(resultTitle);
            txtResultTitle.setVisibility(View.VISIBLE);
            resultTitle = null;
        }

        if (resultInfo != null) {

            txtResultDescription.setText(resultInfo);
            txtResultDescription.setVisibility(View.VISIBLE);
            resultInfo = null;
        }

        if (resultExtra != null) {

            txtResultDetails.setText(resultExtra);
            txtResultDetails.setVisibility(View.VISIBLE);
            resultExtra = null;
        }

    }

}
