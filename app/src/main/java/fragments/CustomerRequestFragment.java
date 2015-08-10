package fragments;

import android.app.AlertDialog;
import android.app.Fragment;
import android.content.Intent;
import android.os.Bundle;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.chungmin.helpu.CustomerRequest;
import com.example.chungmin.helpu.CustomerRequestServerRequests;
import com.example.chungmin.helpu.GetCustomerRequestCallback;
import com.example.chungmin.helpu.Globals;
import com.example.chungmin.helpu.ProjectStatus;
import com.example.chungmin.helpu.ProjectStatusFlow;
import com.example.chungmin.helpu.R;

public class CustomerRequestFragment extends Fragment {
    private static final String TAG = "CustomerRequestFragment";
    private int mCustomerRequestId = 1;
    private TextView tvCustomerRequestId, tvServiceName, tvDescription, tvProjectStatus, tvServiceProviderId, tvQuotation, lblServiceProviderId, lblQuotation;

    public static CustomerRequestFragment newInstance(int customerRequestId)
    {
        Log.d(TAG, "CustomerRequestFragment newInstance");
        CustomerRequestFragment f = new CustomerRequestFragment();
        Bundle args = new Bundle(1);
        args.putInt("customerRequestId", customerRequestId);
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
            mCustomerRequestId = args.getInt("customerRequestId", mCustomerRequestId);
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

        tvCustomerRequestId = (TextView) view.findViewById(R.id.tvCustomerRequestId);
        tvServiceName = (TextView) view.findViewById(R.id.tvServiceName);
        tvDescription = (TextView) view.findViewById(R.id.tvDescription);
        tvProjectStatus = (TextView) view.findViewById(R.id.tvProjectStatus);

        tvServiceProviderId = (TextView) view.findViewById(R.id.tvServiceProviderId);
        tvQuotation = (TextView) view.findViewById(R.id.tvQuotation);
        lblServiceProviderId = (TextView) view.findViewById(R.id.lblServiceProviderId);
        lblQuotation = (TextView) view.findViewById(R.id.lblQuotation);

        String url = getString(R.string.server_uri) + ((Globals)getActivity().getApplication()).getCustomerRequestGetByID();
        CustomerRequestServerRequests serverRequest = new CustomerRequestServerRequests(getActivity());
        serverRequest.getCustomerRequestByID(mCustomerRequestId, url, new GetCustomerRequestCallback() {
            @Override
            public void done(final CustomerRequest returnedCustomerRequest) {
                if (returnedCustomerRequest == null) {
                    showErrorMessage();
                } else {
                    tvCustomerRequestId.setText(returnedCustomerRequest.getCustomerRequestId()+"");
                    tvServiceName.setText(returnedCustomerRequest.getServiceName());
                    tvDescription.setText(returnedCustomerRequest.getDescription());
                    String projectStatus = returnedCustomerRequest.getProjectStatus().toString();
                    tvProjectStatus.setText(projectStatus);

                    if( projectStatus == ProjectStatus.ComfirmRequest.toString() ||
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
                        tvServiceProviderId.setText(returnedCustomerRequest.getServiceProviderId()+"");
                        tvQuotation.setText(Double.toString(returnedCustomerRequest.getQuotation()));

                        tvServiceProviderId.setVisibility(View.VISIBLE);
                        tvQuotation.setVisibility(View.VISIBLE);
                        lblServiceProviderId.setVisibility(View.VISIBLE);
                        lblQuotation.setVisibility(View.VISIBLE);
                    }
                }

                tvProjectStatus.setOnClickListener(new View.OnClickListener() {
                    public void onClick(View v) {
                        Intent intent = new Intent(v.getContext(), ProjectStatusFlow.class);
                        Bundle b = new Bundle();
                        b.putInt("customerRequestId", returnedCustomerRequest.getCustomerRequestId());
                        b.putInt("projectStatusId", returnedCustomerRequest.getProjectStatus().getId());
                        intent.putExtras(b);
                        startActivity(intent);
                    }
                });
            }
        });
    }

    private void showErrorMessage() {
        Log.d(TAG, "CustomerRequestFragment showErrorMessage");
        AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(getActivity().getBaseContext());
        dialogBuilder.setMessage("Get Customer Request Fail!!");
        dialogBuilder.setPositiveButton("Ok", null);
        dialogBuilder.show();
    }
}
