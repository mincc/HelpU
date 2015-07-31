package com.example.chungmin.helpu;

import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;

import fragments.CustomerRequestFragment;
import fragments.ServiceProviderFragment;


public class Test extends ActionBarActivity {
    private static final String TAG = "CustomerRequestFragment";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Log.d(TAG, "Test onCreate");
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test2);

        if (savedInstanceState == null) {
            FragmentManager fm = getFragmentManager();
            FragmentTransaction ft = fm.beginTransaction();
            CustomerRequest cr =((Globals)getApplication()).getCustomerRequest();
            Fragment crfrag = new CustomerRequestFragment().newInstance(cr.getCustomerRequestId());
            ft.add(R.id.llCustomerRequest, crfrag);
            Fragment spfrag = new ServiceProviderFragment().newInstance(4);
            ft.add(R.id.llServiceProvider, spfrag);
            ft.commit();
        }


    }


}
