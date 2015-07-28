package com.example.chungmin.helpu;

import android.app.AlertDialog;
import android.content.Intent;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;


public class CustomerRequestByID extends ActionBarActivity {
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

        String url = getString(R.string.server_uri) + "CustomerRequestGetByID.php";
        CustomerRequestServerRequests serverRequest = new CustomerRequestServerRequests(this);
        serverRequest.getCustomerRequestByID(customerRequestId, url, new GetCustomerRequestCallback() {
            @Override
            public void done(final CustomerRequest returnedCustomerRequest) {
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
                    }
                });
            }
        });
    }

    private void showErrorMessage() {
        AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(CustomerRequestByID.this);
        dialogBuilder.setMessage("Get Customer Request Fail!!");
        dialogBuilder.setPositiveButton("Ok", null);
        dialogBuilder.show();
    }
}
