package com.example.chungmin.helpu;

import android.app.AlertDialog;
import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.app.NotificationManager;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Handler;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.ipay.Ipay;
import com.ipay.IpayPayment;

import fragments.CreateQuotationFragment;
import fragments.CustomerRequestFragment;
import fragments.RatingFragment;
import fragments.ServiceProviderFragment;
import fragments.UserInfoFragment;


public class ProjectMessages extends HelpUBaseActivity implements View.OnClickListener {
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
    private int mCustomerRequestId;
    private int mUserId;
    private boolean mIsStillStayHere = false;

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
        mCustomerRequestId = b.getInt("customerRequestId", 0);

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
            if (mCustomerRequest == null) {
                //notification redirect
                getCustomerRequestInfo();
            } else {
                //normal retrieve from list in application
                getServiceProviderInfo();
            }
        }

        btnTask.setOnClickListener(this);
        btnCancel.setOnClickListener(this);
        btnPaymentGateway.setOnClickListener(this);

        isAllowMenuProgressBar = true;

        if (!BuildConfig.DEBUG) {
            mIsStillStayHere = true;
            int timer = 20 * 1000; //20 second
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    if (mIsStillStayHere) {
                        Intent redirect = new Intent(ProjectMessages.this, MainActivity.class);
                        redirect.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        startActivity(redirect);
                        finish();
                    }
                }
            }, timer);
        }
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
                showToast("Some info is missing and can't retrieve");
                return;
            }
            //endregion
        } else if (v == btnTask) {
            //region Button Task
            switch (mProjectStatus) {
                case Pick:
                case SelectedNotification:
                    if (mUserId == mCustomerRequest.getUserId()) {
                        //customer
                    } else {
                        //service provider
                        mCustomerRequest.setProjectStatusId(ProjectStatus.ConfirmRequest.getId());
                    }
                    break;
                case ConfirmRequest:
                case ConfirmRequestNotification:
                    if (mUserId == mCustomerRequest.getUserId()) {
                        //customer
                    } else {
                        //service provider
                    }
                    break;
                case Quotation:
                case QuotationNotification:
                    if (mUserId == mCustomerRequest.getUserId()) {
                        //customer
                    } else {
                        //service provider
                    }
                    break;
                case ConfirmQuotation:
                case ConfirmQuotationNotification:
                    if (mUserId == mCustomerRequest.getUserId()) {
                        //customer
                        mCustomerRequest.setProjectStatusId(ProjectStatus.Deal.getId());
                    } else {
                        //service provider
                    }
                    break;
                case Deal:
                case DealNotification:
                    if (mUserId == mCustomerRequest.getUserId()) {
                        //customer
                    } else {
                        //service provider
                        mCustomerRequest.setProjectStatusId(ProjectStatus.PlanStartDate.getId());
                    }
                    break;
                case PlanStartDate:
                case PlanStartDateNotification:
                    if (mUserId == mCustomerRequest.getUserId()) {
                        //customer
                    } else {
                        //service provider
                        mCustomerRequest.setProjectStatusId(ProjectStatus.ServiceStart.getId());
                    }
                    break;
                case ReceiveDownPayment:
                    if (mUserId == mCustomerRequest.getUserId()) {
                        //customer
                    } else {
                        //service provider
                    }
                    break;
                case ServiceStart:
                case ServiceStartNotification:
                    if (mUserId == mCustomerRequest.getUserId()) {
                        //customer
                        mCustomerRequest.setProjectStatusId(ProjectStatus.ServiceDone.getId());
                    } else {
                        //service provider
                    }
                    break;
                case ServiceDone:
                case ServiceDoneNotification:
                    if (mUserId == mCustomerRequest.getUserId()) {
                        //customer
                        mCustomerRequest.setProjectStatusId(ProjectStatus.CustomerRating.getId());
                    } else {
                        //service provider
                        mCustomerRequest.setProjectStatusId(ProjectStatus.ServiceProvRating.getId());
                    }
                    break;
                case CustomerRating:
                case CustomerRatingNotification:
                    if (mUserId == mCustomerRequest.getUserId()) {
                        //customer

                    } else {
                        //service provider
                    }
                    break;
                case ServiceProvRating:
                case ServiceProviderRatingNotification:
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
            CustomerRequestServerRequests serverRequest = new CustomerRequestServerRequests();
            serverRequest.getCustomerRequestUpdate(mCustomerRequest, url, new GetCustomerRequestCallback() {
                @Override
                public void done(CustomerRequest returnedCustomerRequest) {
                }
            });

            Intent redirect = new Intent(ProjectMessages.this, ProjectMessages.class);
            Bundle b = new Bundle();
            b.putInt("projectStatusId", mCustomerRequest.getProjectStatusId());
            b.putInt("customerRequestId", mCustomerRequest.getCustomerRequestId());
            redirect.putExtras(b);
            redirect.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            startActivity(redirect);
            finish();

            //endregion
        } else if (v == btnCancel) {
            //region Button Cancel
            AlertDialog.Builder adb = new AlertDialog.Builder(this);
            final AlertDialog alertDialog = adb.create();
            adb.setMessage(R.string.strReSltSPdr);
            adb.setTitle(R.string.strTitleReSltSPdr);
            adb.setIcon(android.R.drawable.ic_dialog_alert);
            adb.setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int which) {
//                    Toast.makeText(getBaseContext(), "OK Button is press", Toast.LENGTH_SHORT).show();

                    mCustomerRequest.setProjectStatusId(ProjectStatus.New.getId());
                    String url = getString(R.string.server_uri) + ((Globals) getApplicationContext()).getCustomerRequestUpdate();
                    CustomerRequestServerRequests serverRequest = new CustomerRequestServerRequests();
                    serverRequest.getCustomerRequestUpdate(mCustomerRequest, url, new GetCustomerRequestCallback() {
                        @Override
                        public void done(CustomerRequest returnedCustomerRequest) {
                        }
                    });

                    Intent redirect = new Intent(getBaseContext(), ServiceProviderListByServiceID.class);
                    Bundle b = new Bundle();
                    b.putInt("serviceId", mCustomerRequest.getServiceId());
                    redirect.putExtras(b);
                    redirect.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    startActivity(redirect);
                    finish();
                }
            });

            adb.setNegativeButton(android.R.string.cancel, new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int which) {
                    alertDialog.dismiss();
                }
            });
            adb.show();
            //endregion
        }
    }

    @Override
    public void onBackPressed() {
        mIsStillStayHere = false;
        Intent redirect = new Intent(this, MainActivity.class);
        redirect.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(redirect);
        finish();
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
            tvMessageBox.setText(R.string.strPageRedirect);
            tvMessageBox.setVisibility(View.VISIBLE);

            txtResultTitle.setVisibility(View.GONE);
            txtResultDescription.setVisibility(View.GONE);
            txtResultDetails.setVisibility(View.GONE);

            mCustomerRequest.setProjectStatusId(ProjectStatus.Deal.getId());
            String url = getString(R.string.server_uri) + ((Globals) getApplicationContext()).getCustomerRequestUpdate();
            CustomerRequestServerRequests serverRequest = new CustomerRequestServerRequests();
            serverRequest.getCustomerRequestUpdate(mCustomerRequest, url, new GetCustomerRequestCallback() {
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
                    b.putInt("projectStatusId", mCustomerRequest.getProjectStatusId());
                    b.putInt("customerRequestId", mCustomerRequest.getCustomerRequestId());
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
        TransactionServerRequests serverRequest = new TransactionServerRequests();
        serverRequest.transactionInsert(transaction, requestUrl, new GetTransactionCallback() {
            @Override
            public void done(Transaction returnedTransaction) {
            }
        });
    }

    public void getCustomerRequestInfo() {
        String url = getString(R.string.server_uri) + ((Globals) getApplicationContext()).getCustomerRequestGetByID();
        CustomerRequestServerRequests serverRequest = new CustomerRequestServerRequests();
        serverRequest.getCustomerRequestByID(mCustomerRequestId, url, new GetCustomerRequestCallback() {
            @Override
            public void done(CustomerRequest returnedCustomerRequest) {
                if (returnedCustomerRequest != null) {
                    mCustomerRequest = returnedCustomerRequest;
                    //Need to for Rating to get more current status instead of the status when notification trigger
                    //due to two direction rating at the same time and status done is trigger when both site rating
                    if (mCustomerRequest.getProjectStatus() == ProjectStatus.ProjectClose ||
                            mCustomerRequest.getProjectStatus() == ProjectStatus.PlanStartDateNotification) {
                        mProjectStatus = mCustomerRequest.getProjectStatus();
                    }
                    getServiceProviderInfo();
                }
            }
        });
    }

    public void getServiceProviderInfo() {
        String url = getString(R.string.server_uri) + ((Globals) getApplication()).getServiceProviderGetByID();
        ServiceProviderServerRequests serverRequest = new ServiceProviderServerRequests();
        serverRequest.getServiceProviderByID(mCustomerRequest.getServiceProviderId(), url, new GetServiceProviderCallback() {
            @Override
            public void done(ServiceProvider returnedServiceProvider) {
                if (returnedServiceProvider == null) {
                    showAlert("Get Service Provider By ID Fail!!");
                } else {
                    mServiceProvider = returnedServiceProvider;
                    getDisplayFragmentDetail();
                    getProjectStatusChecking();
                }
            }
        });
    }

    public void getProjectStatusChecking() {
        //region Project Status Checking
        NotificationManager notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        switch (mProjectStatus) {
            case Pick:
            case SelectedNotification:
                if (mUserId == mCustomerRequest.getUserId()) {
                    setTitle(R.string.strPlsWait);
                    tvMessageBox.setText(R.string.strCustCdtNtfMessage);
                } else {
                    setTitle(R.string.strCfmJobOfr);
                    tvMessageBox.setVisibility(View.GONE);
                    btnTask.setText(R.string.strSPdrCdtNtfMessage);
                    btnTask.setVisibility(View.VISIBLE);
                    notificationManager.cancel(mCustomerRequest.getCustomerRequestId());
                }
                break;
            case ConfirmRequest:
            case ConfirmRequestNotification:
                if (mUserId == mCustomerRequest.getUserId()) {
                    setTitle(R.string.strPlsWait);
                    tvMessageBox.setText(R.string.strCustCfmRqtMessage);
                    notificationManager.cancel(mCustomerRequest.getCustomerRequestId());
                } else {
                    setTitle(R.string.strCreatQtt);
                    llCreateQuotation.setVisibility(View.VISIBLE);
                    btnTask.setVisibility(View.GONE);
                    tvMessageBox.setVisibility(View.GONE);
                }
                break;
            case Quotation:
            case QuotationNotification:
                if (mUserId == mCustomerRequest.getUserId()) {
                    setTitle(R.string.strPlsWait);
                    tvMessageBox.setText(R.string.strCustQttMessage);
                    notificationManager.cancel(mCustomerRequest.getCustomerRequestId());
                } else {
                    setTitle(R.string.strCreatQtt);
                    llCreateQuotation.setVisibility(View.VISIBLE);
                    btnTask.setVisibility(View.GONE);
                    tvMessageBox.setVisibility(View.GONE);
                }
                break;
            case ConfirmQuotation:
            case ConfirmQuotationNotification:
                if (mUserId == mCustomerRequest.getUserId()) {
                    setTitle(R.string.strTitleCfmQtt);
                    llCreateQuotation.setVisibility(View.VISIBLE);
                    btnTask.setText(R.string.strOkDeal);
                    btnTask.setVisibility(View.VISIBLE);
                    btnCancel.setText(R.string.strCancelNoDeal);
                    btnCancel.setVisibility(View.VISIBLE);
                    tvMessageBox.setVisibility(View.GONE);
                    notificationManager.cancel(mCustomerRequest.getCustomerRequestId());
                } else {
                    setTitle(R.string.strPlsWait);
                    tvMessageBox.setText(R.string.strSPdrCfmQttMessage);
                }

                break;
            case Deal:
            case DealNotification:
                if (mUserId == mCustomerRequest.getUserId()) {
                    setTitle(R.string.strPlsWait);
                    tvMessageBox.setText(R.string.strCustWinAwdNtfMessage);
                } else {
                    setTitle(R.string.strTitleDDPmt);
                    tvMessageBox.setVisibility(View.GONE);
                    btnTask.setText(R.string.strSPdrWinAwdNtfTask);
                    btnTask.setVisibility(View.VISIBLE);
                    notificationManager.cancel(mCustomerRequest.getCustomerRequestId());
                }
                break;
            case PlanStartDate:
            case PlanStartDateNotification:
                if (mUserId == mCustomerRequest.getUserId()) {
                    setTitle(R.string.strPlsWait);
                    tvMessageBox.setText(R.string.strPlanStartDate);
                    notificationManager.cancel(mCustomerRequest.getCustomerRequestId());
                } else {
                    setTitle(R.string.strTitleRcvDwnPmt);
                    tvMessageBox.setVisibility(View.GONE);
                    btnTask.setText(R.string.strSPdrSvrStrTask);
                    btnTask.setVisibility(View.VISIBLE);
                }
                break;
            case ReceiveDownPayment:
                break;
            case ServiceStart:
            case ServiceStartNotification:
                if (mUserId == mCustomerRequest.getUserId()) {
                    setTitle(R.string.strSvrDone);
                    tvMessageBox.setVisibility(View.GONE);
                    btnTask.setText(R.string.strSvrDone);
                    btnTask.setVisibility(View.VISIBLE);
                    notificationManager.cancel(mCustomerRequest.getCustomerRequestId());
                } else {
                    setTitle(R.string.strSvrDone);
                    tvMessageBox.setText(R.string.strSPdrSvrStrMessage);
                    tvMessageBox.setVisibility(View.VISIBLE);
                    btnTask.setVisibility(View.GONE);
                }
                break;
            case ServiceDone:
            case ServiceDoneNotification:
                setTitle(R.string.strTitleRtg);
                tvMessageBox.setVisibility(View.GONE);
                btnTask.setVisibility(View.GONE);
                llRating.setVisibility(View.VISIBLE);

                if (mUserId == mCustomerRequest.getUserId()) {
                    tvMessageBox.setText(R.string.strWaitSPdrReply);
                } else {
                    notificationManager.cancel(mCustomerRequest.getCustomerRequestId());
                }
                break;
            case CustomerRating:
            case CustomerRatingNotification:
                setTitle(R.string.strTitleCustRtg);
                if (mUserId == mCustomerRequest.getUserId()) {
                    tvMessageBox.setText(R.string.strCustCustRtgMessage);
                    tvMessageBox.setVisibility(View.VISIBLE);
                } else {
                    tvMessageBox.setText(R.string.strSPdrCustRtgMessage);
                    tvMessageBox.setVisibility(View.VISIBLE);
                    llRating.setVisibility(View.VISIBLE);
                    notificationManager.cancel(mCustomerRequest.getCustomerRequestId());
                }
                break;
            case ServiceProvRating:
            case ServiceProviderRatingNotification:
                setTitle(R.string.strTitleSPdrRtg);
                if (mUserId == mCustomerRequest.getUserId()) {
                    tvMessageBox.setText(R.string.strCustSPdrRtgMessage);
                    tvMessageBox.setVisibility(View.VISIBLE);
                    llRating.setVisibility(View.VISIBLE);
                    notificationManager.cancel(mCustomerRequest.getCustomerRequestId());
                } else {
                    tvMessageBox.setText(R.string.strSPdrSPdrRtgMessage);
                    tvMessageBox.setVisibility(View.VISIBLE);
                }
                btnTask.setVisibility(View.GONE);
                break;
            case ProjectClose:
                setTitle(R.string.strTitlePjtDone);
                tvMessageBox.setText(R.string.strCustPjtDoneMessage);
                tvMessageBox.setVisibility(View.VISIBLE);
                llRating.setVisibility(View.GONE);
                btnTask.setVisibility(View.GONE);
                break;
            default:
                tvMessageBox.setText(R.string.strUnknownAction);
                break;
        }
        //endregion

    }

    public void getDisplayFragmentDetail() {
        FragmentManager fm = getFragmentManager();
        final FragmentTransaction ft = fm.beginTransaction();
        Fragment frag = new UserInfoFragment().newInstance(mUser.getUserName(), mCustomerRequest.getProjectStatus(), mUserId, mCustomerRequest.getUserId());
        ft.add(R.id.llUserInfo, frag);
        frag = new CustomerRequestFragment().newInstance(mCustomerRequest, mUser);
        ft.add(R.id.llCustomerRequest, frag);
        frag = new ServiceProviderFragment().newInstance(mCustomerRequest);
        ft.add(R.id.llServiceProvider, frag);

        if (mProjectStatus.getId() == ProjectStatus.ConfirmRequest.getId() ||
                mProjectStatus.getId() == ProjectStatus.ConfirmRequestNotification.getId()) {
            frag = new CreateQuotationFragment().newInstance(mCustomerRequest);
            ft.add(R.id.llCreateQuotation, frag);
        }

        if (mUserId == mCustomerRequest.getUserId() &&
                (mProjectStatus.getId() == ProjectStatus.ServiceDone.getId() ||
                        mProjectStatus.getId() == ProjectStatus.ServiceDoneNotification.getId() ||
                        mProjectStatus.getId() == ProjectStatus.ServiceProvRating.getId() ||
                        mProjectStatus.getId() == ProjectStatus.ServiceProviderRatingNotification.getId() ||
                        mProjectStatus.getId() == ProjectStatus.CustomerRating.getId() ||
                        mProjectStatus.getId() == ProjectStatus.CustomerRatingNotification.getId())
                ) {
            frag = new RatingFragment().newInstance("ServiceProvider", mUserId, mServiceProvider.getUserId(), mCustomerRequest.getCustomerRequestId());
            ft.add(R.id.llRating, frag);
        } else if (mUserId != mCustomerRequest.getUserId() &&
                (mProjectStatus.getId() == ProjectStatus.ServiceDone.getId() ||
                        mProjectStatus.getId() == ProjectStatus.ServiceDoneNotification.getId() ||
                        mProjectStatus.getId() == ProjectStatus.ServiceProvRating.getId() ||
                        mProjectStatus.getId() == ProjectStatus.ServiceProviderRatingNotification.getId() ||
                        mProjectStatus.getId() == ProjectStatus.CustomerRating.getId() ||
                        mProjectStatus.getId() == ProjectStatus.CustomerRatingNotification.getId())
                ) {
            frag = new RatingFragment().newInstance("Customer", mUserId, mCustomerRequest.getUserId(), mCustomerRequest.getCustomerRequestId());
            ft.add(R.id.llRating, frag);
        }

        ft.commit();

        setAlreadyReadNotification();
    }

    public void setAlreadyReadNotification() {
        String url = getString(R.string.server_uri) + ((Globals) getApplicationContext()).getCustomerRequestUpdate();
        CustomerRequestServerRequests serverRequest = new CustomerRequestServerRequests();
        mCustomerRequest.setAlreadyReadNotification(1); //already read
        serverRequest.getCustomerRequestUpdate(mCustomerRequest, url, new GetCustomerRequestCallback() {
            @Override
            public void done(CustomerRequest returnedCustomerRequest) {
            }
        });

    }
}
