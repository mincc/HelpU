package com.example.chungmin.helpu.activities;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.example.chungmin.helpu.R;


public class AboutUs extends HelpUBaseActivity implements Button.OnClickListener {
    Button btnHire, btnWork;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_about_us);

        btnHire = (Button) findViewById(R.id.btnHire);
        btnWork = (Button) findViewById(R.id.btnWork);

        btnHire.setOnClickListener(this);
        btnWork.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        Intent redirect = null;

        if (v == btnHire) {
            redirect = new Intent(this, Hire.class);
        } else if (v == btnWork) {
            redirect = new Intent(this, Work.class);
        }

        startActivity(redirect);
        finish();
    }
}
