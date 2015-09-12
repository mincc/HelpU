package com.example.chungmin.helpu;

import android.content.DialogInterface;
import android.content.Intent;
import android.provider.UserDictionary;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;


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
