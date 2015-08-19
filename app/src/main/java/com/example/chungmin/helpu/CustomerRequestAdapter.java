package com.example.chungmin.helpu;

/**
 * Created by Chung Min on 7/23/2015.
 */

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.TextView;

import java.util.List;

public class CustomerRequestAdapter extends ArrayAdapter<CustomerRequest>{
    TextView tvCustomerRequestId, tvService, tvDescription, tvProjectStatus, tvServiceProviderId, tvQuotation;
    TextView lblServiceProviderId, lblQuotation;
    Button btnChangeState, btnRemoveFromView;
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
            tvCustomerRequestId = (TextView)v.findViewById(R.id.tvCustomerRequestId);
            tvService = (TextView)v.findViewById(R.id.tvService);
            tvDescription = (TextView)v.findViewById(R.id.tvDescription);
            tvProjectStatus = (TextView)v.findViewById(R.id.tvProjectStatus);
            tvServiceProviderId = (TextView)v.findViewById(R.id.tvServiceProviderId);
            tvQuotation = (TextView)v.findViewById(R.id.tvQuotation);
            lblServiceProviderId = (TextView)v.findViewById(R.id.lblServiceProviderId);
            lblQuotation = (TextView)v.findViewById(R.id.lblQuotation);
            btnChangeState = (Button) v.findViewById(R.id.btnChangeState);
            btnRemoveFromView = (Button) v.findViewById(R.id.btnRemoveFromView);

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

            String projectStatus = customerRequest.getProjectStatus().toString();
            if (projectStatus == ProjectStatus.ConfirmRequest.toString() ||
                    projectStatus == ProjectStatus.Quotation.toString() ||
                    projectStatus == ProjectStatus.ConfirmQuotation.toString() ||
                    projectStatus == ProjectStatus.DoDownPayment.toString() ||
                    projectStatus == ProjectStatus.WinAwardNotification.toString() ||
                    projectStatus == ProjectStatus.ReceiveDownPayment.toString() ||
                    projectStatus == ProjectStatus.ServiceStart.toString() ||
                    projectStatus == ProjectStatus.ServiceDone.toString() ||
                    projectStatus == ProjectStatus.CustomerRating.toString() ||
                    projectStatus == ProjectStatus.ServiceProvRating.toString() ||
                    projectStatus == ProjectStatus.Done.toString()
                    ){
                tvServiceProviderId.setText(customerRequest.getServiceProviderId()+"");
                tvQuotation.setText(Double.toString(customerRequest.getQuotation()));

                tvServiceProviderId.setVisibility(View.VISIBLE);
                tvQuotation.setVisibility(View.VISIBLE);
                lblServiceProviderId.setVisibility(View.VISIBLE);
                lblQuotation.setVisibility(View.VISIBLE);
            }else
            {
                tvServiceProviderId.setText("No");
                tvQuotation.setText("0.00");

                tvServiceProviderId.setVisibility(View.GONE);
                tvQuotation.setVisibility(View.GONE);
                lblServiceProviderId.setVisibility(View.GONE);
                lblQuotation.setVisibility(View.GONE);
            }

            if (projectStatus == ProjectStatus.Done.toString()) {
                btnRemoveFromView.setVisibility(View.VISIBLE);
            } else {
                btnRemoveFromView.setVisibility(View.GONE);
            }

            btnRemoveFromView.setOnClickListener(new Button.OnClickListener() {
                public void onClick(final View v) {
                    final View superView = (View) v.getParent();
                    TextView tvCustomerRequestId = (TextView) superView.findViewById(R.id.tvCustomerRequestId);

                    int customerRequestId = Integer.parseInt(tvCustomerRequestId.getText().toString());
                    CustomerRequestServerRequests serverRequest = new CustomerRequestServerRequests(getContext());
                    String url = mContext.getString(R.string.server_uri) + ((Globals) mContext.getApplicationContext()).getCustomerRequestGetByID();
                    serverRequest.getCustomerRequestByID(customerRequestId, url, new GetCustomerRequestCallback() {
                        @Override
                        public void done(final CustomerRequest returnedCustomerRequest) {
                            final CustomerRequest customerRequest = returnedCustomerRequest;
                            customerRequest.setProjectStatusId(ProjectStatus.RemoveFromView.getId());
                            String url = mContext.getString(R.string.server_uri) + ((Globals) mContext.getApplicationContext()).getCustomerRequestUpdate();
                            CustomerRequestServerRequests serverRequest = new CustomerRequestServerRequests(getContext());
                            serverRequest.getCustomerRequestUpdate(customerRequest, url, new GetCustomerRequestCallback() {
                                @Override
                                public void done(CustomerRequest returnedCustomerRequest) {
                                    Intent redirect = new Intent(mContext, CustomerRequestList.class);
                                    redirect.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                                    mContext.startActivity(redirect);
                                    ((Activity) mContext).finish();
                                }
                            });

                        }
                    });
                }
            });
        }

        final int serviceId = customerRequest.getServiceId();
            btnChangeState.setOnClickListener(new Button.OnClickListener() {
                public void onClick(final View v) {
                    final View superView = (View) v.getParent();
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

                            int userId = ((Globals)getContext().getApplicationContext()).getUserId();
                            Bundle b = new Bundle();
                            switch(returnedCustomerRequest.getProjectStatus()){
                                case New:
                                case Match:
                                    redirect = new Intent(mContext, ServiceProviderListByServiceID.class);
                                    b.putInt("serviceId", serviceId);

                                    break;
                                case Pick:
                                    redirect = new Intent(mContext, ProjectMessages.class);
                                    b.putInt("projectStatusId", returnedCustomerRequest.getProjectStatusId());
                                    break;
                                case DoDownPayment:
                                    if(userId != returnedCustomerRequest.getUserId()){

                                        redirect = new Intent(mContext, MainActivity.class);
                                    }
                                    else{
                                        redirect = new Intent(mContext, ProjectMessages.class);
                                        b.putInt("projectStatusId", returnedCustomerRequest.getProjectStatusId());
                                    }
                                    break;
                                case ConfirmRequest:
                                case Quotation:
                                case ConfirmQuotation:
                                case CandidateNotification:
                                case WinAwardNotification:
                                case ReceiveDownPayment:
                                case ServiceStart:
                                case ServiceDone:
                                case CustomerRating:
                                case ServiceProvRating:
                                case Done:
                                    redirect = new Intent(mContext, ProjectMessages.class);
                                    b.putInt("projectStatusId", returnedCustomerRequest.getProjectStatusId());
                                    break;
                                default:
                                    redirect = new Intent(mContext, MainActivity.class);
                                    break;
                            }
                            redirect.putExtras(b);
                            redirect.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                            mContext.startActivity(redirect);
                            ((Activity) mContext).finish();
                        }
                    });

                    }
                });

        return v;
    }

}
