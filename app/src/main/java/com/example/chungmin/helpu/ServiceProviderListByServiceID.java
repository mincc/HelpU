package com.example.chungmin.helpu;

import android.app.AlertDialog;
import android.app.FragmentManager;
import android.app.ListFragment;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.List;


public class ServiceProviderListByServiceID extends HelpUBaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_service_provider_list);

        setTitle("Select Service Provider...");

        FragmentManager fm = getFragmentManager();
        if (fm.findFragmentById(android.R.id.content) == null) {
            ServiceProviderListFragment list = new ServiceProviderListFragment();
            fm.beginTransaction().add(android.R.id.content, list).commit();
        }

        isAllowMenuProgressBar = false;
    }

    public static class ServiceProviderListFragment extends ListFragment implements FetchServiceProviderDataListener, View.OnClickListener {
        private Context mContext;

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container,
                                 Bundle savedInstanceState) {

            mContext = getActivity();
            initView(mContext);

            return super.onCreateView(inflater, container, savedInstanceState);
        }

        public void onActivityCreated(Bundle savedInstanceState) {
            super.onActivityCreated(savedInstanceState);

            // Getting listview from xml
            ListView lv = getListView();

            // Adding button to listview at footer
            lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    //user choose the provider and change the project status to Pick(3)
                    TextView tvServiceProviderId = (TextView) view.findViewById(R.id.tvServiceProviderId);
                    int serviceProviderId = Integer.parseInt((String) tvServiceProviderId.getText());
                    int projectStatusId = ProjectStatus.Pick.getId();
                    final CustomerRequest customerRequest = ((Globals) mContext.getApplicationContext()).getCustomerRequest();
                    customerRequest.setServiceProviderId(serviceProviderId);
                    customerRequest.setProjectStatusId(projectStatusId);
                    String url = getString(R.string.server_uri) + ((Globals) mContext.getApplicationContext()).getCustomerRequestUpdate();
                    CustomerRequestServerRequests serverRequest = new CustomerRequestServerRequests();
                    serverRequest.getCustomerRequestUpdate(customerRequest, url, new GetCustomerRequestCallback() {
                        @Override
                        public void done(CustomerRequest returnedCustomerRequest) {
                            Intent redirect = new Intent(mContext, ProjectMessages.class);
                            Bundle b = new Bundle();
                            b.putInt("projectStatusId", customerRequest.getProjectStatusId());
                            b.putInt("customerRequestId", customerRequest.getCustomerRequestId());
                            redirect.putExtras(b);
                            redirect.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                            startActivity(redirect);
                            getActivity().finish();
                        }
                    });
                }
            });

        }

        private void initView(final Context context) {
            //get from bundle
            Bundle b = getActivity().getIntent().getExtras();
            int serviceId = b.getInt("serviceId", 0);
            int userId = ((Globals) mContext.getApplicationContext()).getUserId();

            String url = getString(R.string.server_uri) + ((Globals) mContext.getApplicationContext()).getServiceProviderGetByServiceID();
            ServiceProviderServerRequests serverRequest = new ServiceProviderServerRequests();
            serverRequest.getServiceProviderByServiceID(serviceId, userId, url, new GetServiceProviderListCallback() {
                @Override
                public void done(List<ServiceProvider> returnedServiceProviderList) {
                    if (returnedServiceProviderList == null) {
                        showErrorMessage();
                    } else {
                        // create new adapter
                        ServiceProviderAdapter adapter = new ServiceProviderAdapter(context, returnedServiceProviderList, false);
                        // set the adapter to list
                        setListAdapter(adapter);
                    }
                }
            });
        }


        private void showErrorMessage() {
            AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(mContext);
            dialogBuilder.setMessage("Get Service Provider By Service ID Fail!!");
            dialogBuilder.setPositiveButton(android.R.string.ok, null);
            dialogBuilder.show();
        }

        @Override
        public void Complete(List<ServiceProvider> data) {

            // create new adapter
            ServiceProviderAdapter adapter = new ServiceProviderAdapter(mContext, data, false);
            // set the adapter to list
            setListAdapter(adapter);
        }

        @Override
        public void Failure(String msg) {

            // show failure message
            Toast.makeText(mContext, msg, Toast.LENGTH_LONG).show();
        }

        @Override
        public void onClick(View v) {

        }
    }
}