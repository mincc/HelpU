package com.example.chungmin.helpu;

import android.graphics.Color;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;


public class ProjectStatusFlow extends HelpUBaseActivity {
    TextView tvCustomerRequestId, tvProjectStatus, tvNew, tvMatch, tvPick, tvSelectedNotification, tvConfirmRequest, tvQuotation;
    TextView tvConfirmQuotation, tvDeal, tvDealNotification, tvReceiveDownPayment, tvServiceStart, tvServiceDone;
    TextView tvCustomerRating, tvServiceProvRating, tvDone;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_project_status_flow);

        tvCustomerRequestId = (TextView) findViewById(R.id.tvCustomerRequestId);
        tvProjectStatus = (TextView) findViewById(R.id.tvProjectStatus);

        tvNew = (TextView) findViewById(R.id.tvNew);
        tvMatch = (TextView) findViewById(R.id.tvMatch);
        tvPick = (TextView) findViewById(R.id.tvPick);
        tvSelectedNotification = (TextView) findViewById(R.id.tvSelectedNotification);
        tvConfirmRequest = (TextView) findViewById(R.id.tvConfirmRequest);
        tvQuotation = (TextView) findViewById(R.id.tvQuotation);
        tvConfirmQuotation = (TextView) findViewById(R.id.tvConfirmQuotation);
        tvDeal = (TextView) findViewById(R.id.tvDeal);
        tvDealNotification = (TextView) findViewById(R.id.tvDealNotification);
        tvReceiveDownPayment = (TextView) findViewById(R.id.tvReceiveDownPayment);
        tvServiceStart = (TextView) findViewById(R.id.tvServiceStart);
        tvServiceDone = (TextView) findViewById(R.id.tvServiceDone);
        tvCustomerRating = (TextView) findViewById(R.id.tvCustomerRating);
        tvServiceProvRating = (TextView) findViewById(R.id.tvServiceProvRating);
        tvDone = (TextView) findViewById(R.id.tvDone);

        //get from bundle
        Bundle b = getIntent().getExtras();
//        int customerRequestId =b.getInt("customerRequestId", 0);
//        int projectStatusId =b.getInt("projectStatusId",0);

//        tvCustomerRequestId.setText(customerRequestId+"");
//            String currentProjectStatus = ProjectStatus.values()[projectStatusId]+"";
//        tvProjectStatus.setText(currentProjectStatus);
        String currentProjectStatus = ProjectStatus.New.toString();

        //Set the color
        boolean isCorrect = true;
        if(isCorrect && tvNew.getText().equals(currentProjectStatus))
        {
            tvNew.setTextColor(Color.parseColor("#FF0B2EFF"));
            isCorrect = true;
        }

        if(isCorrect && tvMatch.getText().equals(currentProjectStatus))
        {
            tvMatch.setTextColor(Color.parseColor("#FF0B2EFF"));
            isCorrect = true;
        }else {
            tvMatch.setTextColor(Color.parseColor("#FFFF030E"));
            isCorrect = false;
        }

        if(isCorrect && tvPick.getText().equals(currentProjectStatus))
        {
            tvPick.setTextColor(Color.parseColor("#FF0B2EFF"));
            isCorrect = true;
        }else {
            tvPick.setTextColor(Color.parseColor("#FFFF030E"));
            isCorrect = false;
        }

        if (isCorrect && tvSelectedNotification.getText().equals(currentProjectStatus))
        {
            tvSelectedNotification.setTextColor(Color.parseColor("#FF0B2EFF"));
            isCorrect = true;
        }else {
            tvSelectedNotification.setTextColor(Color.parseColor("#FFFF030E"));
            isCorrect = false;
        }

        if(isCorrect && tvConfirmRequest.getText().equals(currentProjectStatus))
        {
            tvConfirmRequest.setTextColor(Color.parseColor("#FF0B2EFF"));
            isCorrect = true;
        }else {
            tvConfirmRequest.setTextColor(Color.parseColor("#FFFF030E"));
            isCorrect = false;
        }

        if(isCorrect && tvQuotation.getText().equals(currentProjectStatus))
        {
            tvQuotation.setTextColor(Color.parseColor("#FF0B2EFF"));
            isCorrect = true;
        }else {
            tvQuotation.setTextColor(Color.parseColor("#FFFF030E"));
            isCorrect = false;
        }

        if(isCorrect && tvConfirmQuotation.getText().equals(currentProjectStatus))
        {
            tvConfirmQuotation.setTextColor(Color.parseColor("#FF0B2EFF"));
            isCorrect = true;
        }else {
            tvConfirmQuotation.setTextColor(Color.parseColor("#FFFF030E"));
            isCorrect = false;
        }

        if (isCorrect && tvDeal.getText().equals(currentProjectStatus))
        {
            tvDeal.setTextColor(Color.parseColor("#FF0B2EFF"));
            isCorrect = true;
        }else {
            tvDeal.setTextColor(Color.parseColor("#FFFF030E"));
            isCorrect = false;
        }

        if (isCorrect && tvDealNotification.getText().equals(currentProjectStatus))
        {
            tvDealNotification.setTextColor(Color.parseColor("#FF0B2EFF"));
            isCorrect = true;
        }else {
            tvDealNotification.setTextColor(Color.parseColor("#FFFF030E"));
            isCorrect = false;
        }

        if(isCorrect && tvReceiveDownPayment.getText().equals(currentProjectStatus))
        {
            tvReceiveDownPayment.setTextColor(Color.parseColor("#FF0B2EFF"));
            isCorrect = true;
        }else {
            tvReceiveDownPayment.setTextColor(Color.parseColor("#FFFF030E"));
            isCorrect = false;
        }

        if(isCorrect && tvServiceStart.getText().equals(currentProjectStatus))
        {
            tvServiceStart.setTextColor(Color.parseColor("#FF0B2EFF"));
            isCorrect = true;
        }else {
            tvServiceStart.setTextColor(Color.parseColor("#FFFF030E"));
            isCorrect = false;
        }

        if(isCorrect && tvServiceDone.getText().equals(currentProjectStatus))
        {
            tvServiceDone.setTextColor(Color.parseColor("#FF0B2EFF"));
            isCorrect = true;
        }else {
            tvServiceDone.setTextColor(Color.parseColor("#FFFF030E"));
            isCorrect = false;
        }

        if(isCorrect && tvCustomerRating.getText().equals(currentProjectStatus))
        {
            tvCustomerRating.setTextColor(Color.parseColor("#FF0B2EFF"));
            isCorrect = true;
        }else {
            tvCustomerRating.setTextColor(Color.parseColor("#FFFF030E"));
            isCorrect = false;
        }

        if(isCorrect && tvServiceProvRating.getText().equals(currentProjectStatus))
        {
            tvServiceProvRating.setTextColor(Color.parseColor("#FF0B2EFF"));
            isCorrect = true;
        }else {
            tvServiceProvRating.setTextColor(Color.parseColor("#FFFF030E"));
            isCorrect = false;
        }

        if(isCorrect && tvDone.getText().equals(currentProjectStatus))
        {
            tvDone.setTextColor(Color.parseColor("#FF0B2EFF"));
            isCorrect = true;
        }else {
            tvDone.setTextColor(Color.parseColor("#FFFF030E"));
            isCorrect = false;
        }
    }

}
