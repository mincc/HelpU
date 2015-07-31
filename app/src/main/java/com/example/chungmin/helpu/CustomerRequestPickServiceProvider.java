package com.example.chungmin.helpu;

import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;

import org.w3c.dom.Text;

import fragments.CustomerRequestFragment;
import fragments.ServiceProviderFragment;


public class CustomerRequestPickServiceProvider extends ActionBarActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_customer_request_pick_service_provider);

        if (savedInstanceState == null) {
            FragmentManager fm = getFragmentManager();
            FragmentTransaction ft = fm.beginTransaction();
            CustomerRequest cr =((Globals)getApplication()).getCustomerRequest();
            Fragment frag = new CustomerRequestFragment().newInstance(cr.getCustomerRequestId());
            ft.add(R.id.llCustomerRequest, frag);
            frag = new ServiceProviderFragment().newInstance(cr.getServiceProviderId());
            ft.add(R.id.llServiceProvider, frag);
            ft.commit();
        }
    }

}
