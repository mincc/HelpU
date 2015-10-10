package com.example.chungmin.helpu.adapter;

/**
 * Created by Chung Min on 7/23/2015.
 */

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.text.format.DateUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import com.example.chungmin.helpu.R;
import com.example.chungmin.helpu.activities.HelpUBaseActivity;
import com.example.chungmin.helpu.callback.Callback;
import com.example.chungmin.helpu.serverrequest.ServiceProviderManager;
import com.example.chungmin.helpu.activities.ServiceProviderList;
import com.example.chungmin.helpu.enumeration.ServiceType;
import com.example.chungmin.helpu.models.Globals;
import com.example.chungmin.helpu.models.ServiceProvider;

import java.util.Date;
import java.util.List;

import HelpUGenericUtilities.DateTimeUtils;
import HelpUGenericUtilities.Enumerations;

import static android.text.format.DateUtils.getRelativeTimeSpanString;

public class ServiceProviderAdapter extends ArrayAdapter<ServiceProvider>{
    private List<ServiceProvider> items;
    private Context mContext;
    private boolean mDisplayDeleteButton;

    public ServiceProviderAdapter(Context context, List<ServiceProvider> items, boolean displayDeleteButton) {
        super(context, R.layout.service_provider_custom_list, items);
        this.items = items;
        mContext = context;
        mDisplayDeleteButton = displayDeleteButton;
    }

    @Override
    public int getCount() {
        return items.size();
    }

    static class ViewHolder {
        TextView tvServiceProviderId;
        TextView tvUserName;
        TextView tvService;
        TextView tvOnline;
        TextView tvPhone;
        TextView tvEmail;
        Button btnDelete;
        RatingBar rbAvgRatedValue;
        TextView tvAvgRatedValue;
    }

    @Override
    public View getView(int position, final View convertView, ViewGroup parent) {
        View v = convertView;
        ViewHolder viewHolder;

        if(v == null) {
            LayoutInflater li = LayoutInflater.from(getContext());
            v = li.inflate(R.layout.service_provider_custom_list, null);

            viewHolder = new ViewHolder();
            viewHolder.tvServiceProviderId = (TextView) v.findViewById(R.id.tvServiceProviderId);
            viewHolder.tvUserName = (TextView) v.findViewById(R.id.tvUserName);
            viewHolder.tvService = (TextView) v.findViewById(R.id.tvService);
            viewHolder.tvOnline = (TextView) v.findViewById(R.id.tvOnline);
            viewHolder.tvPhone = (TextView) v.findViewById(R.id.tvPhone);
            viewHolder.tvEmail = (TextView) v.findViewById(R.id.tvEmail);
            viewHolder.btnDelete = (Button) v.findViewById(R.id.btnDelete);
            viewHolder.rbAvgRatedValue = (RatingBar) v.findViewById(R.id.rbAvgRatedValue);
            viewHolder.tvAvgRatedValue = (TextView) v.findViewById(R.id.tvAvgRatedValue);

            // store the holder with the view.
            v.setTag(viewHolder);
        } else {
            // we've just avoided calling findViewById() on resource everytime
            // just use the viewHolder
            viewHolder = (ViewHolder) v.getTag();
        }

        ServiceProvider serviceprovider = items.get(position);

        if(serviceprovider != null) {

            if (viewHolder.tvServiceProviderId != null) {
                viewHolder.tvServiceProviderId.setText(serviceprovider.getServiceProviderId() + "");
            }

            if (viewHolder.tvUserName != null) {
                viewHolder.tvUserName.setText(serviceprovider.getUserName());
            }

            if (viewHolder.tvService != null) {
                viewHolder.tvService.setText(ServiceType.values()[serviceprovider.getServiceId()].toString());
            }

            if (viewHolder.tvOnline != null) {
                String result = DateTimeUtils.ToHuman(serviceprovider.getLastOnline(), Enumerations.DateStyle.HowLong, mContext);
                viewHolder.tvOnline.setText(result);
            }

            if (viewHolder.tvPhone != null) {
                viewHolder.tvPhone.setText(serviceprovider.getPhone());
            }

            if (viewHolder.tvEmail != null) {
                viewHolder.tvEmail.setText(serviceprovider.getEmail());
            }

            if (viewHolder.rbAvgRatedValue != null) {
                viewHolder.rbAvgRatedValue.setRating((float) serviceprovider.getAvgRatedValue());
                viewHolder.tvAvgRatedValue.setText(Double.toString(serviceprovider.getAvgRatedValue()));
            }

            if (mDisplayDeleteButton) {
                viewHolder.btnDelete.setVisibility(View.VISIBLE);
            } else {
                viewHolder.btnDelete.setVisibility(View.GONE);
            }

            viewHolder.btnDelete.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(final View v) {
                    final View superView = (View) v.getParent().getParent();
                    TextView tvServiceProviderId = (TextView) superView.findViewById(R.id.tvServiceProviderId);
                    int serviceProviderId = Integer.parseInt(tvServiceProviderId.getText().toString());
                    int isLogicalDelete = 1;
                    ServiceProviderManager.delete(serviceProviderId, isLogicalDelete, new Callback.GetServiceProviderCallback() {
                        @Override
                        public void complete(final ServiceProvider returnedServiceProvider) {
                            Intent redirect = new Intent(v.getContext(), ServiceProviderList.class);
                            redirect.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                            mContext.startActivity(redirect);
                            ((Activity) mContext).finish();
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

        return v;
    }
}
