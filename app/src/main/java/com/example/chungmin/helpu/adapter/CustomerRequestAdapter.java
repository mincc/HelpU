package com.example.chungmin.helpu.adapter;

/**
 * Created by Chung Min on 7/23/2015.
 */

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.BounceInterpolator;
import android.view.animation.TranslateAnimation;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.example.chungmin.helpu.R;
import com.example.chungmin.helpu.activities.HelpUBaseActivity;
import com.example.chungmin.helpu.activities.ServiceProviderListByServiceID;
import com.example.chungmin.helpu.activities.CustomerRequestList;
import com.example.chungmin.helpu.activities.MainActivity;
import com.example.chungmin.helpu.activities.ProjectMessages;
import com.example.chungmin.helpu.callback.Callback;
import com.example.chungmin.helpu.enumeration.ProjectStatus;
import com.example.chungmin.helpu.enumeration.ServiceType;
import com.example.chungmin.helpu.models.CustomerRequest;
import com.example.chungmin.helpu.models.Globals;
import com.example.chungmin.helpu.serverrequest.CustomerRequestManager;
import com.readystatesoftware.viewbadger.BadgeView;

import java.util.List;

public class CustomerRequestAdapter extends ArrayAdapter<CustomerRequest> {
    TextView tvCustomerRequestId, tvService, tvDescription, tvProjectStatus, tvServiceProviderId, tvQuotation, tvLocBadge;
    TextView lblServiceProviderId, lblQuotation;
    Button btnEnter, btnDelete;
    private List<CustomerRequest> items;
    private Context mContext;
    private int mUserId = 0;
    private BadgeView badge;
    private CustomerRequest mCustomerRequest;


    public CustomerRequestAdapter(Context context, List<CustomerRequest> items) {
        super(context, R.layout.customer_request_custom_list, items);
        this.items = items;
        mContext = context;
        mUserId = ((Globals) context.getApplicationContext()).getUserId();
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

        mCustomerRequest = items.get(position);

        if (mCustomerRequest != null) {
            tvCustomerRequestId = (TextView)v.findViewById(R.id.tvCustomerRequestId);
            tvService = (TextView)v.findViewById(R.id.tvService);
            tvDescription = (TextView)v.findViewById(R.id.tvDescription);
            tvProjectStatus = (TextView)v.findViewById(R.id.tvProjectStatus);
            tvServiceProviderId = (TextView)v.findViewById(R.id.tvServiceProviderId);
            tvQuotation = (TextView)v.findViewById(R.id.tvQuotation);
            tvLocBadge = (TextView) v.findViewById(R.id.tvLocBadge);
            lblServiceProviderId = (TextView)v.findViewById(R.id.lblServiceProviderId);
            lblQuotation = (TextView)v.findViewById(R.id.lblQuotation);
            btnEnter = (Button) v.findViewById(R.id.btnEnter);
            btnDelete = (Button) v.findViewById(R.id.btnDelete);

            if (tvCustomerRequestId != null) {
                tvCustomerRequestId.setText(Integer.toString((mCustomerRequest.getCustomerRequestId())));
            }


            if(tvService != null) {
                ServiceType serviceType = ServiceType.values()[mCustomerRequest.getServiceId()];
                tvService.setText(serviceType.toString());
            }

            if(tvDescription != null) {
                tvDescription.setText(mCustomerRequest.getDescription());
            }

            if(tvProjectStatus != null) {
                tvProjectStatus.setText(mCustomerRequest.getProjectStatus().toString());
            }

            ProjectStatus projectStatus = mCustomerRequest.getProjectStatus();
            switch (projectStatus) {
                case New:
                case Pick:
                    tvServiceProviderId.setVisibility(View.GONE);
                    tvQuotation.setVisibility(View.GONE);
                    lblServiceProviderId.setVisibility(View.GONE);
                    lblQuotation.setVisibility(View.GONE);
                    break;
                case ConfirmRequest:
                case Quotation:
                case ConfirmQuotation:
                case Deal:
                case PlanStartDate:
                case ReceiveDownPayment:
                case ServiceStart:
                case ServiceDone:
                case CustomerRating:
                case ServiceProvRating:
                    tvServiceProviderId.setText(mCustomerRequest.getServiceProviderId() + "");
                    tvQuotation.setText(Double.toString(mCustomerRequest.getQuotation()));

                    tvServiceProviderId.setVisibility(View.VISIBLE);
                    tvQuotation.setVisibility(View.VISIBLE);
                    lblServiceProviderId.setVisibility(View.VISIBLE);
                    lblQuotation.setVisibility(View.VISIBLE);
                    break;
                case SelectedNotification:
                    tvServiceProviderId.setText("No");
                    tvQuotation.setText("0.00");

                    tvServiceProviderId.setVisibility(View.GONE);
                    tvQuotation.setVisibility(View.GONE);
                    lblServiceProviderId.setVisibility(View.GONE);
                    lblQuotation.setVisibility(View.GONE);

                    if (mCustomerRequest.getUserId() != mUserId && mCustomerRequest.isAlreadyReadNotification() == 0) {
                        badge = new BadgeView(getContext(), tvLocBadge);
                        badge.setBadgeBackgroundColor(Color.YELLOW);
                        badge.setTextColor(Color.BLUE);
                        badge.setText("New!");
                        badge.setBadgeMargin(10, 10);
                        badge.setTextSize(10);
                        badge.show();
                    }
                    break;
                case DealNotification:
                    tvServiceProviderId.setText(mCustomerRequest.getServiceProviderId() + "");
                    tvQuotation.setText(Double.toString(mCustomerRequest.getQuotation()));

                    tvServiceProviderId.setVisibility(View.VISIBLE);
                    tvQuotation.setVisibility(View.VISIBLE);
                    lblServiceProviderId.setVisibility(View.VISIBLE);
                    lblQuotation.setVisibility(View.VISIBLE);

                    if (mCustomerRequest.getUserId() != mUserId && mCustomerRequest.isAlreadyReadNotification() == 0) {
                        badge = new BadgeView(getContext(), tvLocBadge);
                        badge.setBadgeBackgroundColor(getContext().getResources().getColor(R.color.White));
                        badge.setTextColor(getContext().getResources().getColor(R.color.DroidGreen));
                        badge.setText("You Are\n Hired!");
                        badge.setTextSize(10);
                        badge.show();
                    }
                    break;
                case ConfirmRequestNotification:
                case QuotationNotification:
                case ConfirmQuotationNotification:
                case PlanStartDateNotification:
                case ServiceStartNotification:
                case ServiceProviderRatingNotification:
                    tvServiceProviderId.setText(mCustomerRequest.getServiceProviderId() + "");
                    tvQuotation.setText(Double.toString(mCustomerRequest.getQuotation()));

                    tvServiceProviderId.setVisibility(View.VISIBLE);
                    tvQuotation.setVisibility(View.VISIBLE);
                    lblServiceProviderId.setVisibility(View.VISIBLE);
                    lblQuotation.setVisibility(View.VISIBLE);

                    if (mCustomerRequest.getUserId() == mUserId && mCustomerRequest.isAlreadyReadNotification() == 0) {
                        badge = new BadgeView(getContext(), tvLocBadge);
                        badge.setBadgeBackgroundColor(getContext().getResources().getColor(R.color.DroidGreen));
                        badge.setTextColor(Color.BLACK);
                        badge.setText("1");
                        badge.setTextSize(10);
                        TranslateAnimation anim = new TranslateAnimation(-100, 0, 0, 0);
                        anim.setInterpolator(new BounceInterpolator());
                        anim.setDuration(1000);
                        badge.toggle(anim, null);
                        badge.show();
                    }
                    break;
                case ServiceDoneNotification:
                case CustomerRatingNotification:
                    tvServiceProviderId.setText(mCustomerRequest.getServiceProviderId() + "");
                    tvQuotation.setText(Double.toString(mCustomerRequest.getQuotation()));

                    tvServiceProviderId.setVisibility(View.VISIBLE);
                    tvQuotation.setVisibility(View.VISIBLE);
                    lblServiceProviderId.setVisibility(View.VISIBLE);
                    lblQuotation.setVisibility(View.VISIBLE);

                    if (mCustomerRequest.getUserId() != mUserId && mCustomerRequest.isAlreadyReadNotification() == 0) {
                        badge = new BadgeView(getContext(), tvLocBadge);
                        badge.setBadgeBackgroundColor(getContext().getResources().getColor(R.color.DroidGreen));
                        badge.setTextColor(Color.BLACK);
                        badge.setText("1");
                        badge.setTextSize(10);
                        TranslateAnimation anim = new TranslateAnimation(-100, 0, 0, 0);
                        anim.setInterpolator(new BounceInterpolator());
                        anim.setDuration(1000);
                        badge.toggle(anim, null);
                        badge.show();
                    }
                    break;
                case ProjectClose:
                    tvServiceProviderId.setText(mCustomerRequest.getServiceProviderId() + "");
                    tvQuotation.setText(Double.toString(mCustomerRequest.getQuotation()));

                    tvServiceProviderId.setVisibility(View.VISIBLE);
                    tvQuotation.setVisibility(View.VISIBLE);
                    lblServiceProviderId.setVisibility(View.VISIBLE);
                    lblQuotation.setVisibility(View.VISIBLE);

                    if (mCustomerRequest.getUserId() == mUserId) {
                        btnDelete.setVisibility(View.VISIBLE);
                    } else {
                        btnDelete.setVisibility(View.GONE);
                    }
                    break;
                default:
                    Toast.makeText(mContext, R.string.strUnknownAction, Toast.LENGTH_LONG).show();
                    break;
            }

            btnDelete.setOnClickListener(new Button.OnClickListener() {
                public void onClick(final View v) {
                    final View superView = (View) v.getParent();
                    TextView tvCustomerRequestId = (TextView) superView.findViewById(R.id.tvCustomerRequestId);

                    int customerRequestId = Integer.parseInt(tvCustomerRequestId.getText().toString());
                    CustomerRequestManager.getByID(customerRequestId, new Callback.GetCustomerRequestCallback() {
                        @Override
                        public void complete(final CustomerRequest returnedCustomerRequest) {
                            if (returnedCustomerRequest != null) {
                                final CustomerRequest customerRequest = returnedCustomerRequest;
                                CustomerRequestManager.delete(customerRequest.getCustomerRequestId(), 1, new Callback.GetCustomerRequestCallback() {
                                    @Override
                                    public void complete(CustomerRequest returnedCustomerRequest) {
                                        Intent redirect = new Intent(mContext, CustomerRequestList.class);
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

        final int serviceId = mCustomerRequest.getServiceId();
        btnEnter.setOnClickListener(new Button.OnClickListener() {
            public void onClick(final View v) {
                final View superView = (View) v.getParent();
                TextView tvCustomerRequestId = (TextView) superView.findViewById(R.id.tvCustomerRequestId);
                int mCustomerRequestId = Integer.parseInt(tvCustomerRequestId.getText().toString());

                CustomerRequestManager.getByID(mCustomerRequestId, new Callback.GetCustomerRequestCallback() {
                    @Override
                    public void complete(final CustomerRequest returnedCustomerRequest) {
                        if (returnedCustomerRequest != null) {
                            Intent redirect;
                            ((Globals) mContext.getApplicationContext()).setCustomerRequest(returnedCustomerRequest);

                            int userId = ((Globals) getContext().getApplicationContext()).getUserId();
                            Bundle b = new Bundle();
                            switch (returnedCustomerRequest.getProjectStatus()) {
                                case New:
                                case Match:
                                    redirect = new Intent(mContext, ServiceProviderListByServiceID.class);
                                    b.putInt("serviceId", serviceId);

                                    break;
                                case Pick:
                                case SelectedNotification:
                                case ConfirmRequest:
                                case ConfirmRequestNotification:
                                case Quotation:
                                case QuotationNotification:
                                case ConfirmQuotation:
                                case ConfirmQuotationNotification:
                                case Deal:
                                case DealNotification:
                                case PlanStartDate:
                                case PlanStartDateNotification:
                                case ReceiveDownPayment:
                                case ServiceStart:
                                case ServiceStartNotification:
                                case ServiceDone:
                                case ServiceDoneNotification:
                                case CustomerRating:
                                case CustomerRatingNotification:
                                case ServiceProvRating:
                                case ServiceProviderRatingNotification:
                                case ProjectClose:
                                    redirect = new Intent(mContext, ProjectMessages.class);
                                    b.putInt("projectStatusId", returnedCustomerRequest.getProjectStatusId());
                                    b.putInt("customerRequestId", returnedCustomerRequest.getCustomerRequestId());
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
                    }

                    @Override
                    public void failure(String msg) {
                        msg = ((Globals) mContext.getApplicationContext()).translateErrorType(msg);
                        ((HelpUBaseActivity) mContext).showAlert(msg);
                    }
                });
            }
        });

        return v;
    }


}
