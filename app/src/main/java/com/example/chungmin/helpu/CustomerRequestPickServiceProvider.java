package com.example.chungmin.helpu;

import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import org.w3c.dom.Text;

import fragments.CustomerRequestFragment;
import fragments.ServiceProviderFragment;


public class CustomerRequestPickServiceProvider extends HelpUBaseActivity {

    private TextView tvMessageBox;
    private CustomerRequest mCustomerRequest = null;
    UserLocalStore userLocalStore;
    private User mUser = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_customer_request_pick_service_provider);

        mUser = userLocalStore.getLoggedInUser();
        tvMessageBox = (TextView) this.findViewById(R.id.tvMessageBox);

        if (savedInstanceState == null) {
            FragmentManager fm = getFragmentManager();
            final FragmentTransaction ft = fm.beginTransaction();
            mCustomerRequest = ((Globals) getApplication()).getCustomerRequest();
            if (mCustomerRequest == null) {
                String url = getString(R.string.server_uri) + ((Globals) getApplicationContext()).getCustomerRequestJobOffer();
                CustomerRequestServerRequests serverRequest = new CustomerRequestServerRequests();
                int userId = ((Globals)this.getApplicationContext()).getUserId();
                serverRequest.getCustomerRequestJobOffer(userId, url, new GetCustomerRequestCallback() {
                    @Override
                    public void done(CustomerRequest returnedCustomerRequest) {
                        if (returnedCustomerRequest != null) {
                            ((Globals) getApplication()).setCustomerRequest(returnedCustomerRequest);
                            mCustomerRequest = ((Globals) getApplication()).getCustomerRequest();
                            Fragment frag = new CustomerRequestFragment().newInstance(mCustomerRequest, mUser);
                            ft.add(R.id.llCustomerRequest, frag);
                            frag = new ServiceProviderFragment().newInstance(mCustomerRequest);
                            ft.add(R.id.llServiceProvider, frag);
                            ft.commit();

                            if (mCustomerRequest.getProjectStatus() == ProjectStatus.Pick) {
                                tvMessageBox.setVisibility(View.VISIBLE);
                            } else if (mCustomerRequest.getProjectStatus() == ProjectStatus.SelectedNotification) {
                                tvMessageBox.setVisibility(View.GONE);
                            }
                        }
                    }
                });;
            }

        }


    }

}
