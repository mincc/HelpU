package com.example.chungmin.helpu;

/**
 * Created by Chung Min on 7/23/2015.
 */

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.provider.Settings;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.TextView;

import java.util.List;

public class CustomerRequestAdapter extends ArrayAdapter<CustomerRequest>{
    private List<CustomerRequest> items;
    private Context mContext;

    public CustomerRequestAdapter(Context context, List<CustomerRequest> items) {
        super(context, R.layout.customer_request_custom_list, items);
        this.items = items;
        mContext = context;
    }

    @Override
    public int getCount() {
        return items.size();
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View v = convertView;

        if(v == null) {
            LayoutInflater li = LayoutInflater.from(getContext());
            v = li.inflate(R.layout.customer_request_custom_list, null);
        }

        CustomerRequest customerRequest = items.get(position);

        if(customerRequest != null) {
            TextView tvCustomerRequestId = (TextView)v.findViewById(R.id.tvCustomerRequestId);
            TextView tvService = (TextView)v.findViewById(R.id.tvService);
            TextView tvDescription = (TextView)v.findViewById(R.id.tvDescription);
            TextView tvProjectStatus = (TextView)v.findViewById(R.id.tvProjectStatus);


            if (tvCustomerRequestId != null) {
                tvCustomerRequestId.setText(Integer.toString((customerRequest.getCustomerRequestId())));
            }


            if(tvService != null) {
                ServiceType serviceType = ServiceType.values()[customerRequest.getServiceId()];
                tvService.setText(serviceType.toString());
            }

            if(tvDescription != null) {
                tvDescription.setText(customerRequest.getDescription());
            }

            if(tvProjectStatus != null) {
                tvProjectStatus.setText(customerRequest.getProjectStatus().toString());
            }

            final int serviceId = customerRequest.getServiceId();

            Button btnChangeState= (Button) v.findViewById(R.id.btnChangeState);
            btnChangeState.setOnClickListener(new Button.OnClickListener() {
                public void onClick(View v) {
                    View superView = (View) v.getParent();
                    TextView tvCustomerRequestId = (TextView)superView.findViewById(R.id.tvCustomerRequestId);
                    TextView tvProjectStatus = (TextView)superView.findViewById(R.id.tvProjectStatus);

                    int customerRequestId = Integer.parseInt(tvCustomerRequestId.getText().toString());
                    final String projectStatus = tvProjectStatus.getText().toString().trim();

                    CustomerRequestServerRequests serverRequest = new CustomerRequestServerRequests(getContext());
                    String url =  mContext.getString(R.string.server_uri) + ((Globals)mContext.getApplicationContext()).getCustomerRequestGetByID();
                    serverRequest.getCustomerRequestByID(customerRequestId, url, new GetCustomerRequestCallback() {
                        @Override
                        public void done(final CustomerRequest returnedCustomerRequest) {
                            Intent redirect;
                            ((Globals) mContext.getApplicationContext()).setCustomerRequest(returnedCustomerRequest);

                            if(projectStatus.equals(ProjectStatus.New.toString()) || projectStatus.equals(ProjectStatus.Match.toString())) {
                                redirect = new Intent(mContext, ServiceProviderListByServiceID.class);
                                Bundle b = new Bundle();
                                b.putInt("serviceId", serviceId);
                                redirect.putExtras(b);
                                redirect.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                            }else if(projectStatus.equals(ProjectStatus.Pick.toString())) {
                                redirect = new Intent(mContext, CustomerRequestPickServiceProvider.class);
                            }else{
                                redirect = new Intent(mContext, MainActivity.class);
                            }
                            mContext.startActivity(redirect);
                        }
                    });

                    }
                });
        }

        return v;
    }
}
