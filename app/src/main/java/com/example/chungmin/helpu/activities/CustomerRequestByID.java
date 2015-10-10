package com.example.chungmin.helpu.activities;

import android.app.AlertDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.example.chungmin.helpu.activities.sample.ProjectStatusFlow;
import com.example.chungmin.helpu.callback.Callback;
import com.example.chungmin.helpu.models.CustomerRequest;
import com.example.chungmin.helpu.serverrequest.CustomerRequestManager;
import com.example.chungmin.helpu.models.Globals;
import com.example.chungmin.helpu.R;


public class CustomerRequestByID extends HelpUBaseActivity {
    TextView tvCustomerRequestId, tvServiceName, tvDescription, tvUserId, tvProjectStatus;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_customer_request_by_id);

        tvCustomerRequestId = (TextView) findViewById(R.id.tvCustomerRequestId);
        tvServiceName = (TextView) findViewById(R.id.tvServiceName);
        tvDescription = (TextView) findViewById(R.id.tvDescription);
        tvUserId = (TextView) findViewById(R.id.tvUserId);
        tvProjectStatus = (TextView) findViewById(R.id.tvProjectStatus);

        displayCustomerRequestDetails();
    }

    private void displayCustomerRequestDetails() {
        //get from bundle
        Bundle b = getIntent().getExtras();
        int customerRequestId =b.getInt("customerRequestId",0);

        CustomerRequestManager.getByID(customerRequestId, new Callback.GetCustomerRequestCallback() {
            @Override
            public void complete(final CustomerRequest returnedCustomerRequest) {
                if (returnedCustomerRequest == null) {
                    showErrorMessage();
                } else {
                    tvCustomerRequestId.setText(returnedCustomerRequest.getCustomerRequestId()+"");
                    tvServiceName.setText(returnedCustomerRequest.getServiceId()+"");
                    tvDescription.setText(returnedCustomerRequest.getDescription());
                    tvUserId.setText(returnedCustomerRequest.getUserId()+"");
                    tvProjectStatus.setText(returnedCustomerRequest.getProjectStatus().toString());
                }

                tvProjectStatus.setOnClickListener(new View.OnClickListener() {
                    public void onClick(View v) {
                        Intent intent = new Intent(v.getContext(), ProjectStatusFlow.class);
                        Bundle b = new Bundle();
                        b.putInt("customerRequestId", returnedCustomerRequest.getCustomerRequestId());
                        b.putInt("projectStatusId", returnedCustomerRequest.getProjectStatus().getId());
                        intent.putExtras(b);
                        startActivity(intent);
                        finish();
                    }
                });
            }

            @Override
            public void failure(String msg) {
                msg = ((Globals) getApplication()).translateErrorType(msg);
                showAlert(msg);
            }
        });
    }

    private void showErrorMessage() {
        AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(CustomerRequestByID.this);
        dialogBuilder.setMessage("Get Customer Request Fail!!");
        dialogBuilder.setPositiveButton(android.R.string.ok, null);
        dialogBuilder.show();
    }
}
