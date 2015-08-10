package com.example.chungmin.helpu;

import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.Intent;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import fragments.CustomerRequestFragment;
import fragments.ServiceProviderFragment;


public class ServiceProviderCreateQuotation extends ActionBarActivity {
    private CustomerRequest mCustomerRequest = null;
    private EditText etQuotation;
    private Button btnDone;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_service_provider_create_quotation);

        if (savedInstanceState == null) {
            FragmentManager fm = getFragmentManager();
            final FragmentTransaction ft = fm.beginTransaction();
            mCustomerRequest = ((Globals) getApplication()).getCustomerRequest();
            if (mCustomerRequest == null) {
                String url = this.getString(R.string.server_uri) + ((Globals)getApplicationContext()).getServiceProviderJobOffer();
                ServiceProviderServerRequests serverRequest = new ServiceProviderServerRequests(this);
                int userId = ((Globals)this.getApplicationContext()).getUserId();
                serverRequest.getServiceProviderJobOffer(userId, url, new GetCustomerRequestCallback() {
                    @Override
                    public void done(CustomerRequest returnedCustomerRequest) {
                        if(returnedCustomerRequest != null) {
                            ((Globals) getApplication()).setCustomerRequest(returnedCustomerRequest);
                        }
                    }
                });;
            }
            mCustomerRequest = ((Globals) getApplication()).getCustomerRequest();
            Fragment frag = new CustomerRequestFragment().newInstance(mCustomerRequest.getCustomerRequestId());
            ft.add(R.id.llCustomerRequest, frag);
            frag = new ServiceProviderFragment().newInstance(mCustomerRequest.getServiceProviderId());
            ft.add(R.id.llServiceProvider, frag);
            ft.commit();

        }

        etQuotation = (EditText)this.findViewById(R.id.etQuotation);
        btnDone = (Button)this.findViewById(R.id.btnDone);

        btnDone.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                CustomerRequest customerRequest = ((Globals)getApplication()).getCustomerRequest();
                customerRequest.setProjectStatusId(ProjectStatus.ConfirmQuotation.getId());
                customerRequest.setQuotation(Double.parseDouble(etQuotation.getText().toString()));
                String url = getString(R.string.server_uri) + ((Globals)getApplicationContext()).getCustomerRequestUpdate();
                CustomerRequestServerRequests serverRequest = new CustomerRequestServerRequests(getBaseContext());
                serverRequest.getCustomerRequestUpdate(customerRequest, url, new GetCustomerRequestCallback() {
                    @Override
                    public void done(CustomerRequest returnedCustomerRequest) {
                        Intent i = new Intent(ServiceProviderCreateQuotation.this, ServiceProviderConfirmQuotation.class);
                        startActivity(i);
                    }
                });
            }
        });
    }
}
