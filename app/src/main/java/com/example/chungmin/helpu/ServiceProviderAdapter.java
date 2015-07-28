package com.example.chungmin.helpu;

/**
 * Created by Chung Min on 7/23/2015.
 */

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.List;

public class ServiceProviderAdapter extends ArrayAdapter<ServiceProvider>{
    private List<ServiceProvider> items;

    public ServiceProviderAdapter(Context context, List<ServiceProvider> items) {
        super(context, R.layout.service_provider_custom_list, items);
        this.items = items;
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
            v = li.inflate(R.layout.service_provider_custom_list, null);
        }

        ServiceProvider serviceprovider = items.get(position);

        if(serviceprovider != null) {
            TextView tvServiceProviderId = (TextView)v.findViewById(R.id.tvServiceProviderId);
            TextView tvUserName = (TextView)v.findViewById(R.id.tvUserName);
            TextView tvService = (TextView)v.findViewById(R.id.tvService);
            TextView tvPhone = (TextView)v.findViewById(R.id.tvPhone);
            TextView tvEmail = (TextView)v.findViewById(R.id.tvEmail);

            if(tvServiceProviderId != null) {
                tvServiceProviderId.setText(serviceprovider.getServiceProviderId()+"");
            }

            if(tvUserName != null) {
                tvUserName.setText(serviceprovider.getUserName());
            }

            if(tvService != null) {
                tvService.setText(serviceprovider.getServiceName());
            }

            if(tvPhone != null) {
                tvPhone.setText(serviceprovider.getPhone());
            }

            if(tvEmail != null) {
                tvEmail.setText(serviceprovider.getEmail());
            }

        }

        return v;
    }
}
