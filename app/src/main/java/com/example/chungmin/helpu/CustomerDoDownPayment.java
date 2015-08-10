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

import fragments.CustomerRequestFragment;
import fragments.ServiceProviderFragment;


public class CustomerDoDownPayment extends ActionBarActivity {
    private CustomerRequest mCustomerRequest = null;
    Button btnDoPayment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_customer_do_down_payment);

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

        btnDoPayment = (Button)findViewById(R.id.btnDoPayment);
        btnDoPayment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(final View v) {
                CustomerRequest customerRequest = ((Globals)getApplication()).getCustomerRequest();
                customerRequest.setProjectStatusId(ProjectStatus.DoDownPayment.getId());
                String url = getString(R.string.server_uri) + ((Globals)getApplicationContext()).getCustomerRequestUpdate();
                CustomerRequestServerRequests serverRequest = new CustomerRequestServerRequests(getBaseContext());
                serverRequest.getCustomerRequestUpdate(customerRequest, url, new GetCustomerRequestCallback() {
                    @Override
                    public void done(CustomerRequest returnedCustomerRequest) {
                        Intent i = new Intent(v.getContext(),ProjectMessages.class);
                        startActivity(i);
                    }
                });
            }
        });
    }

}
