package com.example.chungmin.helpu.activities;

import android.app.AlertDialog;
import android.os.Bundle;
import android.widget.TextView;

import com.example.chungmin.helpu.callback.Callback;
import com.example.chungmin.helpu.models.Globals;
import com.example.chungmin.helpu.R;
import com.example.chungmin.helpu.serverrequest.ServiceProviderManager;
import com.example.chungmin.helpu.models.ServiceProvider;


public class ServiceProviderByID extends HelpUBaseActivity {
    TextView tvUserId, tvServiceProviderId, tvServiceId, tvPhone, tvEmail;
    
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_service_provider_by_id);

        tvUserId = (TextView) findViewById(R.id.tvUserId);
        tvServiceProviderId = (TextView) findViewById(R.id.tvServiceProviderId);
        tvServiceId = (TextView) findViewById(R.id.tvServiceId);
        tvPhone = (TextView) findViewById(R.id.tvPhone);
        tvEmail = (TextView) findViewById(R.id.tvEmail);

        displayServiceProviderDetails();
    }


    private void displayServiceProviderDetails() {
        //get from bundle
        Bundle b = getIntent().getExtras();
        int serviceProviderId =b.getInt("serviceProviderId",0);

        ServiceProviderManager.getByID(serviceProviderId, new Callback.GetServiceProviderCallback() {
            @Override
            public void complete(ServiceProvider returnedServiceProvider) {
                if (returnedServiceProvider == null) {
                    showErrorMessage();
                } else {
                    tvUserId.setText(returnedServiceProvider.getUserId() + "");
                    tvServiceProviderId.setText(returnedServiceProvider.getServiceProviderId() + "");
                    tvServiceId.setText(returnedServiceProvider.getServiceId() + "");
                    tvPhone.setText(returnedServiceProvider.getPhone());
                    tvEmail.setText(returnedServiceProvider.getEmail() + "");
                }
            }

            @Override
            public void failure(String msg) {
                msg = ((Globals) getApplication()).translateErrorType(msg);
                showAlert(msg);
            }
        });
    }

    private void showErrorMessage() {
        AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(ServiceProviderByID.this);
        dialogBuilder.setMessage("Get Service Provider By ID Fail!!");
        dialogBuilder.setPositiveButton(android.R.string.ok, null);
        dialogBuilder.show();
    }
}
