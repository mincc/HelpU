package com.example.chungmin.helpu.activities;

import android.app.AlertDialog;
import android.os.Bundle;
import android.widget.TextView;

import com.example.chungmin.helpu.models.Globals;
import com.example.chungmin.helpu.R;
import com.example.chungmin.helpu.serverrequest.ServiceProviderManager;
import com.example.chungmin.helpu.callback.GetServiceProviderCallback;
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

        String url = getString(R.string.server_uri) + ((Globals) getApplication()).getServiceProviderGetByIDUrl();
        ServiceProviderManager serverRequest = new ServiceProviderManager();
        serverRequest.getByID(serviceProviderId, url, new GetServiceProviderCallback() {
            @Override
            public void done(ServiceProvider returnedServiceProvider) {
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
        });
    }

    private void showErrorMessage() {
        AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(ServiceProviderByID.this);
        dialogBuilder.setMessage("Get Service Provider By ID Fail!!");
        dialogBuilder.setPositiveButton(android.R.string.ok, null);
        dialogBuilder.show();
    }
}
