package fragments;

import android.app.AlertDialog;
import android.app.Fragment;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import com.example.chungmin.helpu.BuildConfig;
import com.example.chungmin.helpu.CustomerRequest;
import com.example.chungmin.helpu.CustomerRequestServerRequests;
import com.example.chungmin.helpu.GetCustomerRequestCallback;
import com.example.chungmin.helpu.Globals;
import com.example.chungmin.helpu.ProjectStatus;
import com.example.chungmin.helpu.ProjectStatusFlow;
import com.example.chungmin.helpu.R;
import com.example.chungmin.helpu.ServiceType;
import com.example.chungmin.helpu.User;

public class CustomerRequestFragment extends Fragment {

    private static final String ARG_PARAM_CustomerRequest = "customerRequest";
    private static final String ARG_PARAM_User = "user";

    private static final String TAG = "CustomerRequestFragment";
    private TextView tvCustomerRequestId, tvServiceName, tvDescription, tvProjectStatus, lblProjectStatus,
            tvServiceProviderId, tvQuotation, lblServiceProviderId, lblQuotation, lblRatedValue;
    private TextView tvFragTitle;
    private RatingBar rbRatedValue;
    private ImageView imgvEmail, imgvPhone;
    private CustomerRequest mCustomerRequest;
    private User mUser;

    public static CustomerRequestFragment newInstance(CustomerRequest customerRequest, User user)
    {
        Log.d(TAG, "CustomerRequestFragment newInstance");
        CustomerRequestFragment f = new CustomerRequestFragment();
        Bundle args = new Bundle(1);
        args.putParcelable(ARG_PARAM_CustomerRequest, customerRequest);
        args.putParcelable(ARG_PARAM_User, user);
        f.setArguments(args);
        return f ;
    }

    public CustomerRequestFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        Log.d(TAG, "CustomerRequestFragment onCreate");
        super.onCreate(savedInstanceState);
        Bundle args = getArguments();
        if(args != null) {
            mCustomerRequest = args.getParcelable(ARG_PARAM_CustomerRequest);
            mUser = args.getParcelable(ARG_PARAM_User);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        Log.d(TAG, "CustomerRequestFragment onCreateView");
        View view = inflater.inflate(R.layout.fragment_customer_request, container, false);

        getCustomerRequestDetails(view);

        // Inflate the layout for this fragment
        return view;
    }

    private void getCustomerRequestDetails(View view) {

        tvFragTitle = (TextView) view.findViewById(R.id.tvFragTitle);
        tvCustomerRequestId = (TextView) view.findViewById(R.id.tvCustomerRequestId);
        tvServiceName = (TextView) view.findViewById(R.id.tvServiceName);
        tvDescription = (TextView) view.findViewById(R.id.tvDescription);
        tvProjectStatus = (TextView) view.findViewById(R.id.tvProjectStatus);
        lblProjectStatus = (TextView) view.findViewById(R.id.lblProjectStatus);

        tvServiceProviderId = (TextView) view.findViewById(R.id.tvServiceProviderId);
        tvQuotation = (TextView) view.findViewById(R.id.tvQuotation);
        lblServiceProviderId = (TextView) view.findViewById(R.id.lblServiceProviderId);
        lblQuotation = (TextView) view.findViewById(R.id.lblQuotation);
        lblRatedValue = (TextView) view.findViewById(R.id.lblRatedValue);
        rbRatedValue = (RatingBar) view.findViewById(R.id.rbRatedValue);
        imgvEmail = (ImageView) view.findViewById(R.id.imgvEmail);
        imgvPhone = (ImageView) view.findViewById(R.id.imgvPhone);

        if (BuildConfig.DEBUG) {
            lblProjectStatus.setVisibility(View.VISIBLE);
            tvProjectStatus.setVisibility(View.VISIBLE);
        }

        if (getActivity() != null) {
            tvFragTitle.setText(getString(R.string.strCustomer) + " ( " + mCustomerRequest.getUserName() + " ) ");
            tvCustomerRequestId.setText(mCustomerRequest.getCustomerRequestId() + "");
            tvServiceName.setText(ServiceType.values()[mCustomerRequest.getServiceId()].toString());
            tvDescription.setText(mCustomerRequest.getDescription());
            String projectStatus = mCustomerRequest.getProjectStatus().toString();
            tvProjectStatus.setText(projectStatus);

            imgvEmail.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent i = new Intent(Intent.ACTION_SEND);
                    i.setType("message/rfc822");
                    i.putExtra(Intent.EXTRA_EMAIL, new String[]{mCustomerRequest.getUserEmail()});
                    i.putExtra(Intent.EXTRA_SUBJECT, "subject of email");
                    i.putExtra(Intent.EXTRA_TEXT, "body of email");
                    try {
                        startActivity(Intent.createChooser(i, "Send mail..."));
                    } catch (android.content.ActivityNotFoundException ex) {
                        Toast.makeText(v.getContext(), "There are no email clients installed.", Toast.LENGTH_SHORT).show();
                    }
                }
            });

            imgvPhone.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    String number = "tel:" + mCustomerRequest.getUserContact().toString().trim();
                    Intent callIntent = new Intent(Intent.ACTION_CALL, Uri.parse(number));
                    startActivity(callIntent);
                }
            });

            if (projectStatus.equals(ProjectStatus.ConfirmRequest.toString()) ||
                    projectStatus.equals(ProjectStatus.ConfirmRequestNotification.toString())) {

                tvServiceProviderId.setText(mCustomerRequest.getServiceProviderId() + "");
                tvServiceProviderId.setVisibility(View.VISIBLE);
                lblServiceProviderId.setVisibility(View.VISIBLE);
                imgvEmail.setVisibility(View.VISIBLE);
                imgvPhone.setVisibility(View.VISIBLE);
            } else if (projectStatus.equals(ProjectStatus.Quotation.toString()) ||
                    projectStatus.equals(ProjectStatus.QuotationNotification.toString()) ||
                    projectStatus.equals(ProjectStatus.ConfirmQuotation.toString()) ||
                    projectStatus.equals(ProjectStatus.ConfirmQuotationNotification.toString()) ||
                    projectStatus.equals(ProjectStatus.Deal.toString()) ||
                    projectStatus.equals(ProjectStatus.DealNotification.toString()) ||
                    projectStatus.equals(ProjectStatus.PlanStartDate.toString()) ||
                    projectStatus.equals(ProjectStatus.PlanStartDateNotification.toString()) ||
                    projectStatus.equals(ProjectStatus.ReceiveDownPayment.toString()) ||
                    projectStatus.equals(ProjectStatus.ServiceStart.toString()) ||
                    projectStatus.equals(ProjectStatus.ServiceStartNotification.toString()) ||
                    projectStatus.equals(ProjectStatus.ServiceDone.toString()) ||
                    projectStatus.equals(ProjectStatus.ServiceDoneNotification.toString()) ||
                    projectStatus.equals(ProjectStatus.CustomerRating.toString()) ||
                    projectStatus.equals(ProjectStatus.CustomerRatingNotification.toString())
                    ) {
                tvServiceProviderId.setText(mCustomerRequest.getServiceProviderId() + "");
                tvQuotation.setText(Double.toString(mCustomerRequest.getQuotation()));

                tvServiceProviderId.setVisibility(View.VISIBLE);
                tvQuotation.setVisibility(View.VISIBLE);
                lblServiceProviderId.setVisibility(View.VISIBLE);
                lblQuotation.setVisibility(View.VISIBLE);
                imgvEmail.setVisibility(View.VISIBLE);
                imgvPhone.setVisibility(View.VISIBLE);

            } else if (projectStatus.equals(ProjectStatus.ServiceProvRating.toString()) ||
                    projectStatus.equals(ProjectStatus.ServiceProviderRatingNotification.toString()) ||
                    projectStatus.equals(ProjectStatus.ProjectClose.toString())) {
                tvServiceProviderId.setText(mCustomerRequest.getServiceProviderId() + "");
                tvQuotation.setText(Double.toString(mCustomerRequest.getQuotation()));
                rbRatedValue.setRating((float) mCustomerRequest.getCustomerRatingValue());

                tvServiceProviderId.setVisibility(View.VISIBLE);
                tvQuotation.setVisibility(View.VISIBLE);
                lblServiceProviderId.setVisibility(View.VISIBLE);
                lblQuotation.setVisibility(View.VISIBLE);
                lblRatedValue.setVisibility(View.VISIBLE);
                rbRatedValue.setVisibility(View.VISIBLE);
                imgvEmail.setVisibility(View.VISIBLE);
                imgvPhone.setVisibility(View.VISIBLE);
            }
        }
    }

//        tvProjectStatus.setOnClickListener(new View.OnClickListener() {
//            public void onClick(View v) {
//                Intent intent = new Intent(v.getContext(), ProjectStatusFlow.class);
//                Bundle b = new Bundle();
//                if(mCustomerRequest != null) {
//                    b.putInt("customerRequestId", mCustomerRequest.getCustomerRequestId());
//                    b.putInt("projectStatusId", mCustomerRequest.getProjectStatus().getId());
//                }
//                intent.putExtras(b);
//                startActivity(intent);
//                getActivity().finish();
//            }
//        });


    private void showErrorMessage() {
        Log.d(TAG, "CustomerRequestFragment showErrorMessage");
        AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(getActivity().getBaseContext());
        dialogBuilder.setMessage("Get Customer Request Fail!!");
        dialogBuilder.setPositiveButton(android.R.string.ok, null);
        dialogBuilder.show();
    }
}
