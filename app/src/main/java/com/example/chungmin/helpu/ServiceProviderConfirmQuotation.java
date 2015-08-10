package com.example.chungmin.helpu;

import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;

import fragments.CustomerRequestFragment;
import fragments.ServiceProviderFragment;


public class ServiceProviderConfirmQuotation extends ActionBarActivity {
    private CustomerRequest mCustomerRequest = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_service_provider_confirm_quotation);

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
    }

}
