package fragments;

import android.app.AlertDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import com.example.chungmin.helpu.models.CustomerRequest;
import com.example.chungmin.helpu.callback.GetServiceProviderCallback;
import com.example.chungmin.helpu.models.Globals;
import com.example.chungmin.helpu.activities.HelpUBaseActivity;
import com.example.chungmin.helpu.enumeration.ProjectStatus;
import com.example.chungmin.helpu.R;
import com.example.chungmin.helpu.models.ServiceProvider;
import com.example.chungmin.helpu.serverrequest.ServiceProviderManager;
import com.example.chungmin.helpu.enumeration.ServiceType;

public class ServiceProviderFragment extends Fragment {
    private static final String TAG = "ServiceProviderFragment";
    private CustomerRequest mCustomerRequest = null;
    private TextView tvServiceProviderId, tvService, lblRatedValue;
    private TextView tvFragTitle;
    private RatingBar rbRatedValue;
    private ImageView imgvEmail, imgvPhone;

    public static ServiceProviderFragment newInstance(CustomerRequest customerRequest) {
        Log.d(TAG, "ServiceProviderFragment newInstance");
        ServiceProviderFragment fragment = new ServiceProviderFragment();
        Bundle args = new Bundle();
        args.putParcelable("customerRequest", customerRequest);
        fragment.setArguments(args);
        return fragment;
    }

    public ServiceProviderFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mCustomerRequest = getArguments().getParcelable("customerRequest");
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_service_provider, container, false);

        getServiceProviderDetails(view);

        return view;
    }

    private void getServiceProviderDetails(View view) {

        tvFragTitle = (TextView) view.findViewById(R.id.tvFragTitle);
        tvServiceProviderId = (TextView) view.findViewById(R.id.tvServiceProviderId);
        tvService = (TextView) view.findViewById(R.id.tvService);
        lblRatedValue = (TextView) view.findViewById(R.id.lblRatedValue);
        rbRatedValue = (RatingBar) view.findViewById(R.id.rbRatedValue);
        imgvEmail = (ImageView) view.findViewById(R.id.imgvEmail);
        imgvPhone = (ImageView) view.findViewById(R.id.imgvPhone);

        String url = getString(R.string.server_uri) + ((Globals) getActivity().getApplication()).getServiceProviderGetByIDUrl();
        ServiceProviderManager serverRequest = new ServiceProviderManager();
        serverRequest.getByID(mCustomerRequest.getServiceProviderId(), url, new GetServiceProviderCallback() {
            @Override
            public void done(final ServiceProvider returnedServiceProvider) {
                if (returnedServiceProvider == null) {
                    showErrorMessage();
                } else {
                    if (getActivity() != null) {
                        tvFragTitle.setText(getString(R.string.strSPdr) + " ( " + returnedServiceProvider.getUserName() + " ) ");
                        tvServiceProviderId.setText(returnedServiceProvider.getServiceProviderId() + "");
                        tvService.setText(ServiceType.values()[returnedServiceProvider.getServiceId()].toString());

                        imgvEmail.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                Intent i = new Intent(Intent.ACTION_SEND);
                                i.setType("message/rfc822");
                                i.putExtra(Intent.EXTRA_EMAIL, new String[]{returnedServiceProvider.getEmail()});
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
                                String number = "tel:" + returnedServiceProvider.getPhone().toString().trim();
                                Intent callIntent = new Intent(Intent.ACTION_CALL, Uri.parse(number));
                                startActivity(callIntent);
                            }
                        });

                        ProjectStatus projectStatus = mCustomerRequest.getProjectStatus();
                        if (projectStatus == ProjectStatus.CustomerRating ||
                                projectStatus == ProjectStatus.CustomerRatingNotification ||
                                projectStatus == ProjectStatus.ServiceProvRating ||
                                projectStatus == ProjectStatus.ServiceProviderRatingNotification ||
                                projectStatus == ProjectStatus.ProjectClose) {

                            if (mCustomerRequest.getServiceProviderRatingValue() > 0) {
                                rbRatedValue.setRating((float) mCustomerRequest.getServiceProviderRatingValue());

                                lblRatedValue.setVisibility(View.VISIBLE);
                                rbRatedValue.setVisibility(View.VISIBLE);
                            }
                        }

                        if ((HelpUBaseActivity) getActivity() != null) {
                            ((HelpUBaseActivity) getActivity()).hideMenuProgress();
                        }
                    }
                }
            }
        });

    }

    private void showErrorMessage() {
        Log.d(TAG, "ServiceProviderFragment showErrorMessage");
        AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(getActivity().getBaseContext());
        dialogBuilder.setMessage("Get Customer Request Fail!!");
        dialogBuilder.setPositiveButton(android.R.string.ok, null);
        dialogBuilder.show();
    }
}
