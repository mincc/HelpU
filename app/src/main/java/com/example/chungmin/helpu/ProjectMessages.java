package com.example.chungmin.helpu;

import android.app.AlertDialog;
import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.Intent;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import fragments.CreateQuotationFragment;
import fragments.CustomerRequestFragment;
import fragments.RatingFragment;
import fragments.ServiceProviderFragment;


public class ProjectMessages extends ActionBarActivity {
    private CustomerRequest mCustomerRequest = null;
    private ServiceProvider mServiceProvider = null;
    private TextView tvMessageBox;
    private Button btnTask;
    private LinearLayout llRating;
    private LinearLayout llCreateQuotation;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_project_messages);

        //get from bundle
        Bundle b = getIntent().getExtras();
        final ProjectStatus projectStatus = ProjectStatus.values()[b.getInt("projectStatusId", 0)];
        final int userId = ((Globals)this.getApplicationContext()).getUserId();

        tvMessageBox = (TextView) findViewById(R.id.tvMessageBox);
        btnTask = (Button) findViewById(R.id.btnTask);
        llRating = (LinearLayout) findViewById(R.id.llRating);
        llCreateQuotation = (LinearLayout) findViewById(R.id.llCreateQuotation);

        if (savedInstanceState == null) {
            FragmentManager fm = getFragmentManager();
            final FragmentTransaction ft = fm.beginTransaction();
            mCustomerRequest = ((Globals) getApplication()).getCustomerRequest();
            if (mCustomerRequest == null) {
                String url = this.getString(R.string.server_uri) + ((Globals)getApplicationContext()).getServiceProviderJobOffer();
                ServiceProviderServerRequests serverRequest = new ServiceProviderServerRequests(this);
                serverRequest.getServiceProviderJobOffer(userId, url, new GetCustomerRequestCallback() {
                    @Override
                    public void done(CustomerRequest returnedCustomerRequest) {
                        if(returnedCustomerRequest != null) {
                            ((Globals) getApplication()).setCustomerRequest(returnedCustomerRequest);
                        }
                    }
                });;
            }
            mCustomerRequest = ((Globals) getApplication()).getCustomerRequest();

            String url = getString(R.string.server_uri) + ((Globals)getApplication()).getServiceProviderGetByID();
            ServiceProviderServerRequests serverRequest = new ServiceProviderServerRequests(this);
            serverRequest.getServiceProviderByID(mCustomerRequest.getServiceProviderId(), url, new GetServiceProviderCallback() {
                @Override
                public void done(ServiceProvider returnedServiceProvider) {
                    if (returnedServiceProvider == null) {
                        showErrorMessage();
                    } else {
                        mServiceProvider = returnedServiceProvider;
                        Fragment frag = new CustomerRequestFragment().newInstance(mCustomerRequest.getCustomerRequestId());
                        ft.add(R.id.llCustomerRequest, frag);
                        frag = new ServiceProviderFragment().newInstance(mCustomerRequest.getServiceProviderId());
                        ft.add(R.id.llServiceProvider, frag);

                        if (projectStatus.getId() == ProjectStatus.Quotation.getId()) {
                            frag = new CreateQuotationFragment().newInstance(14);
                            ft.add(R.id.llCreateQuotation, frag);
                        }

                        if (userId == mCustomerRequest.getUserId() &&
                                (projectStatus.getId() == ProjectStatus.ServiceDone.getId() ||
                                        projectStatus.getId() == ProjectStatus.ServiceProvRating.getId() ||
                                        projectStatus.getId() == ProjectStatus.CustomerRating.getId())
                                ) {
                            frag = new RatingFragment().newInstance("ServiceProvider", userId, mServiceProvider.getUserId(), mCustomerRequest.getCustomerRequestId());
                            ft.add(R.id.llRating, frag);
                        } else if (userId != mCustomerRequest.getUserId() &&
                                (projectStatus.getId() == ProjectStatus.ServiceDone.getId() ||
                                        projectStatus.getId() == ProjectStatus.ServiceProvRating.getId() ||
                                        projectStatus.getId() == ProjectStatus.CustomerRating.getId())
                                ) {
                            frag = new RatingFragment().newInstance("Customer", userId, mCustomerRequest.getUserId(), mCustomerRequest.getCustomerRequestId());
                            ft.add(R.id.llRating, frag);
                        }

                        ft.commit();
                    }
                }
            });
        }

        switch(projectStatus)
        {
            case Pick:
                setTitle("Choose Service Provider");
                if(userId == mCustomerRequest.getUserId()) {
                    tvMessageBox.setText("Please wait for selected Service Provider Reply.");
                }
                else {
                    tvMessageBox.setText("No define yet");
                }
                break;
            case CandidateNotification:
                setTitle("Candidate Notification");
                if(userId == mCustomerRequest.getUserId()) {
                    tvMessageBox.setText("Please wait for selected Service Provider Reply.");
                }
                else {
                    tvMessageBox.setVisibility(View.GONE);
                    btnTask.setText("Confirm And Accept the Job");
                    btnTask.setVisibility(View.VISIBLE);
                }
                break;
            case ComfirmRequest:
                setTitle("Confirm The Job");
                if(userId == mCustomerRequest.getUserId()) {
                    tvMessageBox.setText("Please wait for service provider create quotation.");
                }
                else {
                    llCreateQuotation.setVisibility(View.VISIBLE);
                    btnTask.setVisibility(View.GONE);
                    tvMessageBox.setVisibility(View.GONE);
                }
                break;
            case Quotation:
                setTitle("Create Quotation");
                if(userId == mCustomerRequest.getUserId()) {
                    tvMessageBox.setText("Please wait for service provider create quotation.");
                }
                else {
                    llCreateQuotation.setVisibility(View.VISIBLE);
                    btnTask.setVisibility(View.GONE);
                    tvMessageBox.setVisibility(View.GONE);
                }
                break;
            case ConfirmQuotation:
                setTitle("Confirm Quotation");
                if(userId == mCustomerRequest.getUserId()) {
                    llCreateQuotation.setVisibility(View.VISIBLE);
                    btnTask.setText("Do Payment");
                    btnTask.setVisibility(View.VISIBLE);
                    tvMessageBox.setVisibility(View.GONE);
                }
                else {
                    tvMessageBox.setText("Please wait for customer to confirm quotation and do the down payment.");
                }
                break;
            case DoDownPayment:
                setTitle("Do Down Payment");
                if(userId == mCustomerRequest.getUserId()) {
                    tvMessageBox.setText("Please wait while down payment being processing...");
                }else {
                    tvMessageBox.setText("No define yet");
                }
                break;
            case WinAwardNotification:
                setTitle("Win Award Notification");
                if(userId == mCustomerRequest.getUserId()) {
                    tvMessageBox.setText("Down payment received and notification already send to service provider");
                }else {
                    tvMessageBox.setVisibility(View.GONE);
                    btnTask.setText("Received Payment");
                    btnTask.setVisibility(View.VISIBLE);
                }
                break;
            case ReceiveDownPayment:
                setTitle("Received Down Payment");
                if(userId == mCustomerRequest.getUserId()) {
                    tvMessageBox.setText("Received Down payment and wait service provider to start the job");
                }else {
                    tvMessageBox.setVisibility(View.GONE);
                    btnTask.setText("Service Start");
                    btnTask.setVisibility(View.VISIBLE);
                }
                break;
            case ServiceStart:
                setTitle("Service Start");
                if(userId == mCustomerRequest.getUserId()) {
                    tvMessageBox.setVisibility(View.GONE);
                    btnTask.setText("Service End");
                    btnTask.setVisibility(View.VISIBLE);
                }else {
                    tvMessageBox.setText("Customer will determine end of the job after satisfaction");
                    tvMessageBox.setVisibility(View.VISIBLE);
                    btnTask.setVisibility(View.GONE);
                }
                break;
            case ServiceDone:
                setTitle("Rating");
                if(userId == mCustomerRequest.getUserId()) {
                }else {
                }
                tvMessageBox.setVisibility(View.GONE);
                btnTask.setVisibility(View.GONE);
                llRating.setVisibility(View.VISIBLE);
                break;
            case CustomerRating:
                setTitle("Customer Rating");
                if(userId == mCustomerRequest.getUserId()) {
                    tvMessageBox.setText("Project already finish and will close after service provider rating");
                    tvMessageBox.setVisibility(View.VISIBLE);
                }else {
                    tvMessageBox.setText("Please rate for customer");
                    tvMessageBox.setVisibility(View.VISIBLE);
                    llRating.setVisibility(View.VISIBLE);
                }
                btnTask.setVisibility(View.GONE);
                break;
            case ServiceProvRating:
                setTitle("Service Provider Rating");
                if(userId == mCustomerRequest.getUserId()) {
                    tvMessageBox.setText("Please rate for service provider");
                    tvMessageBox.setVisibility(View.VISIBLE);
                    llRating.setVisibility(View.VISIBLE);
                }else {
                    tvMessageBox.setText("Project already finish and will close after customer rating");
                    tvMessageBox.setVisibility(View.VISIBLE);
                }
                btnTask.setVisibility(View.GONE);
                break;
            case Done:
                setTitle("Project Done");
                tvMessageBox.setText("Project Done successfully");
                tvMessageBox.setVisibility(View.VISIBLE);
                llRating.setVisibility(View.GONE);
                btnTask.setVisibility(View.GONE);
                break;
            default:
                tvMessageBox.setText("Project Status still not define...");
                break;
        }

        btnTask.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                CustomerRequest customerRequest = ((Globals) getApplication()).getCustomerRequest();
                switch (projectStatus) {
                    case CandidateNotification:
                        if (userId == mCustomerRequest.getUserId()) {
                            //customer
                        } else {
                            //service provider
                            customerRequest.setProjectStatusId(ProjectStatus.Quotation.getId());
                        }
                        break;
                    case ComfirmRequest:
                        if (userId == mCustomerRequest.getUserId()) {
                            //customer
                        } else {
                            //service provider
                        }
                        break;
                    case ConfirmQuotation:
                        if (userId == mCustomerRequest.getUserId()) {
                            //customer
                            customerRequest.setProjectStatusId(ProjectStatus.DoDownPayment.getId());
                        } else {
                            //service provider
                        }
                        break;
                    case WinAwardNotification:
                        if (userId == mCustomerRequest.getUserId()) {
                            //customer
                        } else {
                            //service provider
                            customerRequest.setProjectStatusId(ProjectStatus.ReceiveDownPayment.getId());
                        }
                        break;
                    case ReceiveDownPayment:
                        if (userId == mCustomerRequest.getUserId()) {
                            //customer
                        } else {
                            //service provider
                            customerRequest.setProjectStatusId(ProjectStatus.ServiceStart.getId());
                        }
                        break;
                    case ServiceStart:
                        if (userId == mCustomerRequest.getUserId()) {
                            //customer
                            customerRequest.setProjectStatusId(ProjectStatus.ServiceDone.getId());
                        } else {
                            //service provider
                        }
                        break;
                    case ServiceDone:
                        if (userId == mCustomerRequest.getUserId()) {
                            //customer
                            customerRequest.setProjectStatusId(ProjectStatus.CustomerRating.getId());
                        } else {
                            //service provider
                            customerRequest.setProjectStatusId(ProjectStatus.ServiceProvRating.getId());
                        }
                        break;
                    case CustomerRating:
                        if (userId == mCustomerRequest.getUserId()) {
                            //customer

                        } else {
                            //service provider
                        }
                        break;
                    default:
                        break;

                }

                String url = getString(R.string.server_uri) + ((Globals) getApplicationContext()).getCustomerRequestUpdate();
                CustomerRequestServerRequests serverRequest = new CustomerRequestServerRequests(getBaseContext());
                serverRequest.getCustomerRequestUpdate(customerRequest, url, new GetCustomerRequestCallback() {
                    @Override
                    public void done(CustomerRequest returnedCustomerRequest) {
                    }
                });

                Intent redirect = new Intent(ProjectMessages.this, ProjectMessages.class);
                Bundle b = new Bundle();
                b.putInt("projectStatusId", customerRequest.getProjectStatusId());
                redirect.putExtras(b);
                redirect.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(redirect);
            }
        });
    }

    private void showErrorMessage() {
        AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(ProjectMessages.this);
        dialogBuilder.setMessage("Get Service Provider By ID Fail!!");
        dialogBuilder.setPositiveButton("Ok", null);
        dialogBuilder.show();
    }
}
