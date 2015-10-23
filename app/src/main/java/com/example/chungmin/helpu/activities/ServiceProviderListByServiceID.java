package com.example.chungmin.helpu.activities;

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

import com.example.chungmin.helpu.R;
import com.example.chungmin.helpu.callback.Callback;
import com.example.chungmin.helpu.serverrequest.ServiceProviderManager;
import com.example.chungmin.helpu.adapter.ServiceProviderAdapter;
import com.example.chungmin.helpu.enumeration.ProjectStatus;
import com.example.chungmin.helpu.models.CustomerRequest;
import com.example.chungmin.helpu.models.Globals;
import com.example.chungmin.helpu.models.ServiceProvider;
import com.example.chungmin.helpu.serverrequest.CustomerRequestManager;

import java.util.List;


public class ServiceProviderListByServiceID extends HelpUBaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        setContentView(R.layout.activity_service_provider_list);

        setTitle(R.string.strSlctSPdr);

        FragmentManager fm = getFragmentManager();
        if (fm.findFragmentById(android.R.id.content) == null) {
            ServiceProviderListFragment list = new ServiceProviderListFragment();
            fm.beginTransaction().add(android.R.id.content, list).commit();
        }

        isAllowMenuProgressBar = false;
    }

    public static class ServiceProviderListFragment extends ListFragment implements Callback.GetServiceProviderListCallback, View.OnClickListener {
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
                    CustomerRequestManager.update(customerRequest, new Callback.GetCustomerRequestCallback() {
                        @Override
                        public void complete(CustomerRequest returnedCustomerRequest) {
                            CustomerRequestManager.sendPushNotification(customerRequest.getCustomerRequestId(), new Callback.GetCustomerRequestCallback() {
                                @Override
                                public void complete(CustomerRequest data) {
                                    Intent redirect = new Intent(mContext, ProjectMessages.class);
                                    Bundle b = new Bundle();
                                    b.putInt("projectStatusId", customerRequest.getProjectStatusId());
                                    b.putInt("customerRequestId", customerRequest.getCustomerRequestId());
                                    redirect.putExtras(b);
                                    redirect.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                                    startActivity(redirect);
                                    getActivity().finish();
                                }

                                @Override
                                public void failure(String msg) {
                                    msg = ((Globals) mContext.getApplicationContext()).translateErrorType(msg);
                                    ((HelpUBaseActivity) mContext).showAlert(msg);
                                }
                            });
                        }

                        @Override
                        public void failure(String msg) {
                            msg = ((Globals) mContext.getApplicationContext()).translateErrorType(msg);
                            ((HelpUBaseActivity) mContext).showAlert(msg);
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

            ServiceProviderManager.getByServiceID(serviceId, userId, new Callback.GetServiceProviderListCallback() {
                @Override
                public void complete(List<ServiceProvider> returnedServiceProviderList) {
                    if (returnedServiceProviderList == null) {
                        showErrorMessage();
                    } else {
                        // create new adapter
                        ServiceProviderAdapter adapter = new ServiceProviderAdapter(context, returnedServiceProviderList, false);
                        // set the adapter to list
                        setListAdapter(adapter);
                    }
                }

                @Override
                public void failure(String msg) {
                    msg = ((Globals) mContext.getApplicationContext()).translateErrorType(msg);
                    ((HelpUBaseActivity) mContext).showAlert(msg);
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
        public void complete(List<ServiceProvider> data) {

            // create new adapter
            ServiceProviderAdapter adapter = new ServiceProviderAdapter(mContext, data, false);
            // set the adapter to list
            setListAdapter(adapter);
        }

        @Override
        public void failure(String msg) {
            msg = ((Globals) mContext.getApplicationContext()).translateErrorType(msg);
            ((HelpUBaseActivity) mContext).showAlert(msg);
        }

        @Override
        public void onClick(View v) {

        }
    }
}