package com.example.chungmin.helpu;

import android.app.AlertDialog;
import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Handler;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.ipay.Ipay;
import com.ipay.IpayPayment;

import fragments.CreateQuotationFragment;
import fragments.CustomerRequestFragment;
import fragments.RatingFragment;
import fragments.ServiceProviderFragment;


public class ProjectMessages extends ActionBarActivity implements View.OnClickListener {
    private CustomerRequest mCustomerRequest = null;
    private ServiceProvider mServiceProvider = null;
    private User mUser = null;
    private TextView tvMessageBox;
    private Button btnTask, btnPaymentGateway, btnCancel;
    private LinearLayout llRating;
    private LinearLayout llCreateQuotation;
    private PaymentInfo mPaymentInfo;
    private PaymentResultDelegate mPaymentResultDelegate;
    private ProjectStatus mProjectStatus;
    private int mUserId;

    public static String resultTitle;
    public static String resultInfo;
    public static String resultExtra;

    TextView txtResultTitle;
    TextView txtResultDescription;
    TextView txtResultDetails;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_project_messages);

        //get from bundle
        Bundle b = getIntent().getExtras();
        mProjectStatus = ProjectStatus.values()[b.getInt("projectStatusId", 0)];

        UserLocalStore userLocalStore = new UserLocalStore(this);
        mUser = userLocalStore.getLoggedInUser();
        mUserId = mUser.getUserId();

        tvMessageBox = (TextView) findViewById(R.id.tvMessageBox);
        btnTask = (Button) findViewById(R.id.btnTask);
        btnCancel = (Button) findViewById(R.id.btnCancel);
        btnPaymentGateway = (Button) findViewById(R.id.btnPaymentGateway);
        llRating = (LinearLayout) findViewById(R.id.llRating);
        llCreateQuotation = (LinearLayout) findViewById(R.id.llCreateQuotation);

        txtResultTitle = (TextView) findViewById(R.id.txtResultTitle);
        txtResultDescription = (TextView) findViewById(R.id.txtResultDescription);
        txtResultDetails = (TextView) findViewById(R.id.txtResultDetails);

        if (savedInstanceState == null) {
            FragmentManager fm = getFragmentManager();
            final FragmentTransaction ft = fm.beginTransaction();
            mCustomerRequest = ((Globals) getApplication()).getCustomerRequest();
            if (mCustomerRequest == null) {
                String url = this.getString(R.string.server_uri) + ((Globals)getApplicationContext()).getServiceProviderJobOffer();
                ServiceProviderServerRequests serverRequest = new ServiceProviderServerRequests(this);
                serverRequest.getServiceProviderJobOffer(mUserId, url, new GetCustomerRequestCallback() {
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

                        if (mProjectStatus.getId() == ProjectStatus.Quotation.getId()) {
                            frag = new CreateQuotationFragment().newInstance(14);
                            ft.add(R.id.llCreateQuotation, frag);
                        }

                        if (mUserId == mCustomerRequest.getUserId() &&
                                (mProjectStatus.getId() == ProjectStatus.ServiceDone.getId() ||
                                        mProjectStatus.getId() == ProjectStatus.ServiceProvRating.getId() ||
                                        mProjectStatus.getId() == ProjectStatus.CustomerRating.getId())
                                ) {
                            frag = new RatingFragment().newInstance("ServiceProvider", mUserId, mServiceProvider.getUserId(), mCustomerRequest.getCustomerRequestId());
                            ft.add(R.id.llRating, frag);
                        } else if (mUserId != mCustomerRequest.getUserId() &&
                                (mProjectStatus.getId() == ProjectStatus.ServiceDone.getId() ||
                                        mProjectStatus.getId() == ProjectStatus.ServiceProvRating.getId() ||
                                        mProjectStatus.getId() == ProjectStatus.CustomerRating.getId())
                                ) {
                            frag = new RatingFragment().newInstance("Customer", mUserId, mCustomerRequest.getUserId(), mCustomerRequest.getCustomerRequestId());
                            ft.add(R.id.llRating, frag);
                        }

                        ft.commit();
                    }
                }
            });
        }

        //region Project Status Checking
        switch (mProjectStatus)
        {
            case Pick:
                setTitle("Choose Service Provider");
                if (mUserId == mCustomerRequest.getUserId()) {
                    tvMessageBox.setText("Please wait for selected Service Provider Reply.");
                }
                else {
                    tvMessageBox.setText("No define yet");
                }
                break;
            case CandidateNotification:
                setTitle("Candidate Notification");
                if (mUserId == mCustomerRequest.getUserId()) {
                    tvMessageBox.setText("Please wait for selected Service Provider Reply.");
                }
                else {
                    tvMessageBox.setVisibility(View.GONE);
                    btnTask.setText("Confirm And Accept the Job");
                    btnTask.setVisibility(View.VISIBLE);
                }
                break;
            case ConfirmRequest:
                setTitle("Confirm The Job");
                if (mUserId == mCustomerRequest.getUserId()) {
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
                if (mUserId == mCustomerRequest.getUserId()) {
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
                if (mUserId == mCustomerRequest.getUserId()) {
                    //only uncomment when not involve real payment and just want to change state like testing
                    llCreateQuotation.setVisibility(View.VISIBLE);
//                    btnTask.setText("Do Payment");
                    btnTask.setText("OK, Deal");
                    btnTask.setVisibility(View.VISIBLE);
                    btnCancel.setText("Cancel, No Deal");
                    btnCancel.setVisibility(View.VISIBLE);
                    //btnPaymentGateway.setVisibility(View.VISIBLE);
                    tvMessageBox.setVisibility(View.GONE);
                }
                else {
//                    tvMessageBox.setText("Please wait for customer to confirm quotation and do the down payment.");
                    tvMessageBox.setText("Please wait for customer to confirmation.");
                }
                break;
            case DoDownPayment:
                setTitle("Deal?");
                if (mUserId == mCustomerRequest.getUserId()) {
//                    tvMessageBox.setText("Please wait while payment being processing by system and notify service provider...");
                    tvMessageBox.setText("Service provider will be notify by our system...");
                }else {
                    tvMessageBox.setText("No define yet");
                }
                break;
            case WinAwardNotification:
                setTitle("Win Award Notification");
                if (mUserId == mCustomerRequest.getUserId()) {
//                    tvMessageBox.setText("Down payment received and notification already send to service provider");
                    tvMessageBox.setText("Notification already send to service provider");
                }else {
                    tvMessageBox.setVisibility(View.GONE);
//                    btnTask.setText("Received Payment");
                    btnTask.setText("OK, Noted and will arrange start date");
                    btnTask.setVisibility(View.VISIBLE);
                }
                break;
            case ReceiveDownPayment:
                setTitle("Received Down Payment");
                if (mUserId == mCustomerRequest.getUserId()) {
                    tvMessageBox.setText("Received Down payment and wait service provider to start the job");
                }else {
                    tvMessageBox.setVisibility(View.GONE);
                    btnTask.setText("Service Start");
                    btnTask.setVisibility(View.VISIBLE);
                }
                break;
            case ServiceStart:
                setTitle("Service Start");
                if (mUserId == mCustomerRequest.getUserId()) {
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
                if (mUserId == mCustomerRequest.getUserId()) {
                }else {
                }
                tvMessageBox.setVisibility(View.GONE);
                btnTask.setVisibility(View.GONE);
                llRating.setVisibility(View.VISIBLE);
                break;
            case CustomerRating:
                setTitle("Customer Rating");
                if (mUserId == mCustomerRequest.getUserId()) {
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
                if (mUserId == mCustomerRequest.getUserId()) {
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
        //endregion

        btnTask.setOnClickListener(this);
        btnCancel.setOnClickListener(this);
        btnPaymentGateway.setOnClickListener(this);

    }

    private void showErrorMessage() {
        AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(ProjectMessages.this);
        dialogBuilder.setMessage("Get Service Provider By ID Fail!!");
        dialogBuilder.setPositiveButton("Ok", null);
        dialogBuilder.show();
    }

    @Override
    public void onClick(View v) {
        if (v == btnPaymentGateway) {
            //region Payment Gateway
            if (mUser != null && mCustomerRequest != null && mServiceProvider != null) {
                mPaymentInfo = new PaymentInfo(mUser, mCustomerRequest, mServiceProvider, "www.google.com");

                IpayPayment payment = new IpayPayment();
                payment.setMerchantCode(mPaymentInfo.getMerchantCode());
                payment.setMerchantKey(mPaymentInfo.getMerchantKey());
                payment.setPaymentId(mPaymentInfo.getPaymentId());
                payment.setCurrency(mPaymentInfo.getCurrency());
                payment.setLang(mPaymentInfo.getLang());
                payment.setRemark(mPaymentInfo.getRemark());
                payment.setRefNo(mPaymentInfo.getRefID());
                payment.setAmount(mPaymentInfo.getAmount());
                payment.setProdDesc(mPaymentInfo.getProdDesc());
                payment.setUserName(mPaymentInfo.getUserName());
                payment.setUserEmail(mPaymentInfo.getUserEmail());
                payment.setUserContact(mPaymentInfo.getUserContact());
                payment.setCountry(mPaymentInfo.getCountry());
                payment.setBackendPostURL(mPaymentInfo.getBackendPostURL());

                mPaymentResultDelegate = new PaymentResultDelegate();
                Intent checkoutIntent = Ipay.getInstance().checkout(payment, this, mPaymentResultDelegate);
                startActivityForResult(checkoutIntent, 1);
            } else {
                Toast.makeText(this, "Some info is missing and can't retrieve", Toast.LENGTH_LONG).show();
                return;
            }
            //endregion
        } else if (v == btnTask) {
            //region Button Task
            CustomerRequest customerRequest = ((Globals) getApplication()).getCustomerRequest();
            switch (mProjectStatus) {
                case CandidateNotification:
                    if (mUserId == mCustomerRequest.getUserId()) {
                        //customer
                    } else {
                        //service provider
                        customerRequest.setProjectStatusId(ProjectStatus.Quotation.getId());
                    }
                    break;
                case ConfirmRequest:
                    if (mUserId == mCustomerRequest.getUserId()) {
                        //customer
                    } else {
                        //service provider
                    }
                    break;
                case ConfirmQuotation:
                    if (mUserId == mCustomerRequest.getUserId()) {
                        //customer
                        customerRequest.setProjectStatusId(ProjectStatus.DoDownPayment.getId());
                    } else {
                        //service provider
                    }
                    break;
                case WinAwardNotification:
                    if (mUserId == mCustomerRequest.getUserId()) {
                        //customer
                    } else {
                        //service provider
                        customerRequest.setProjectStatusId(ProjectStatus.ReceiveDownPayment.getId());
                    }
                    break;
                case ReceiveDownPayment:
                    if (mUserId == mCustomerRequest.getUserId()) {
                        //customer
                    } else {
                        //service provider
                        customerRequest.setProjectStatusId(ProjectStatus.ServiceStart.getId());
                    }
                    break;
                case ServiceStart:
                    if (mUserId == mCustomerRequest.getUserId()) {
                        //customer
                        customerRequest.setProjectStatusId(ProjectStatus.ServiceDone.getId());
                    } else {
                        //service provider
                    }
                    break;
                case ServiceDone:
                    if (mUserId == mCustomerRequest.getUserId()) {
                        //customer
                        customerRequest.setProjectStatusId(ProjectStatus.CustomerRating.getId());
                    } else {
                        //service provider
                        customerRequest.setProjectStatusId(ProjectStatus.ServiceProvRating.getId());
                    }
                    break;
                case CustomerRating:
                    if (mUserId == mCustomerRequest.getUserId()) {
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
            finish();
            //endregion
        } else if (v == btnCancel) {
            //region Button Cancel
            AlertDialog.Builder adb = new AlertDialog.Builder(this);
            final AlertDialog alertDialog = adb.create();
            adb.setMessage("Are you sure you want reselect the service provider?");
            adb.setTitle("Reselect Service Provider?");
            adb.setIcon(android.R.drawable.ic_dialog_alert);
            adb.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int which) {
//                    Toast.makeText(getBaseContext(), "OK Button is press", Toast.LENGTH_SHORT).show();

                    CustomerRequest customerRequest = ((Globals) getApplication()).getCustomerRequest();
                    customerRequest.setProjectStatusId(ProjectStatus.New.getId());
                    String url = getString(R.string.server_uri) + ((Globals) getApplicationContext()).getCustomerRequestUpdate();
                    CustomerRequestServerRequests serverRequest = new CustomerRequestServerRequests(getBaseContext());
                    serverRequest.getCustomerRequestUpdate(customerRequest, url, new GetCustomerRequestCallback() {
                        @Override
                        public void done(CustomerRequest returnedCustomerRequest) {
                        }
                    });

                    Intent redirect = new Intent(getBaseContext(), ServiceProviderListByServiceID.class);
                    Bundle b = new Bundle();
                    b.putInt("serviceId", customerRequest.getServiceId());
                    redirect.putExtras(b);
                    redirect.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    startActivity(redirect);
                    finish();
                }
            });

            adb.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int which) {
                    alertDialog.dismiss();
                }
            });
            adb.show();
            //endregion
        }
    }

    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        String paymentStatus = "";

        if (requestCode != 1)
            return;

        //only keep got return result and ignore the cancel or terminate transaction
        if (resultTitle != null || resultInfo != null || resultExtra != null) {
            String transId = mPaymentResultDelegate.getTransId();
            String refNo = mPaymentResultDelegate.getRefNo();
            String amount = mPaymentResultDelegate.getAmount();
            String remark = mPaymentInfo.getRemark(); //Result return by iPay88 not complete
            String authCode = mPaymentResultDelegate.getAuthCode();
            String errDesc = mPaymentResultDelegate.getErrDesc();

            Transaction transaction = new Transaction(transId, refNo, amount, remark, authCode, errDesc);
            storeTransactionInfo(transaction);
        }

        txtResultTitle.setVisibility(View.GONE);
        txtResultDescription.setVisibility(View.GONE);
        txtResultDetails.setVisibility(View.GONE);

        if (resultTitle != null) {

            txtResultTitle.setText(resultTitle);
            txtResultTitle.setVisibility(View.VISIBLE);
            paymentStatus = resultTitle;
            resultTitle = null;
        }

        if (resultInfo != null) {

            txtResultDescription.setText(resultInfo);
            txtResultDescription.setVisibility(View.VISIBLE);
            resultInfo = null;
        }

        if (resultExtra != null) {

            txtResultDetails.setText(resultExtra);
            txtResultDetails.setVisibility(View.VISIBLE);
            resultExtra = null;
        }

        //redirect to other page after 10 sec
        if (paymentStatus == "SUCCESS") {    //SUCCESS, FAILURE
            btnTask.setVisibility(View.GONE);
            btnPaymentGateway.setVisibility(View.GONE);
            tvMessageBox.setText("page will redirect after 10 seconds!!");
            tvMessageBox.setVisibility(View.VISIBLE);

            txtResultTitle.setVisibility(View.GONE);
            txtResultDescription.setVisibility(View.GONE);
            txtResultDetails.setVisibility(View.GONE);

            final CustomerRequest customerRequest = ((Globals) getApplication()).getCustomerRequest();
            customerRequest.setProjectStatusId(ProjectStatus.DoDownPayment.getId());
            String url = getString(R.string.server_uri) + ((Globals) getApplicationContext()).getCustomerRequestUpdate();
            CustomerRequestServerRequests serverRequest = new CustomerRequestServerRequests(getBaseContext());
            serverRequest.getCustomerRequestUpdate(customerRequest, url, new GetCustomerRequestCallback() {
                @Override
                public void done(CustomerRequest returnedCustomerRequest) {
                }
            });

            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
            /* Create an Intent that will start the Menu-Activity. */
                    Intent redirect = new Intent(ProjectMessages.this, ProjectMessages.class);
                    Bundle b = new Bundle();
                    b.putInt("projectStatusId", customerRequest.getProjectStatusId());
                    redirect.putExtras(b);
                    redirect.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    startActivity(redirect);
                    finish();
                }
            }, 10000);
        }
    }

    private void storeTransactionInfo(Transaction transaction) {
        String requestUrl = getString(R.string.server_uri) + ((Globals) getApplicationContext()).getTransactionInsert();
        TransactionServerRequests serverRequest = new TransactionServerRequests(getBaseContext());
        serverRequest.transactionInsert(transaction, requestUrl, new GetTransactionCallback() {
            @Override
            public void done(Transaction returnedTransaction) {
            }
        });
    }
}
