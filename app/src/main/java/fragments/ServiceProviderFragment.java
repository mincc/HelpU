package fragments;

import android.app.AlertDialog;
import android.os.Bundle;
import android.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.chungmin.helpu.GetServiceProviderCallback;
import com.example.chungmin.helpu.Globals;
import com.example.chungmin.helpu.R;
import com.example.chungmin.helpu.ServiceProvider;
import com.example.chungmin.helpu.ServiceProviderServerRequests;

public class ServiceProviderFragment extends Fragment {
    private static final String TAG = "ServiceProviderFragment";
    private int mServiceProviderId = 1;
    private TextView tvUserName, tvServiceProviderId, tvService, tvPhone, tvEmail;

    public static ServiceProviderFragment newInstance(int serviceProviderId) {
        Log.d(TAG, "ServiceProviderFragment newInstance");
        ServiceProviderFragment fragment = new ServiceProviderFragment();
        Bundle args = new Bundle();
        args.putInt("serviceProviderId", serviceProviderId);
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
            mServiceProviderId = getArguments().getInt("serviceProviderId", mServiceProviderId);
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

        tvUserName = (TextView) view.findViewById(R.id.tvUserName);
        tvServiceProviderId = (TextView) view.findViewById(R.id.tvServiceProviderId);
        tvService = (TextView) view.findViewById(R.id.tvService);
        tvPhone = (TextView) view.findViewById(R.id.tvPhone);
        tvEmail = (TextView) view.findViewById(R.id.tvEmail);

        String url = getString(R.string.server_uri) + ((Globals)getActivity().getApplication()).getServiceProviderGetByID();
        ServiceProviderServerRequests serverRequest = new ServiceProviderServerRequests(getActivity());
        serverRequest.getServiceProviderByID(mServiceProviderId, url, new GetServiceProviderCallback() {
            @Override
            public void done(ServiceProvider returnedServiceProvider) {
                if (returnedServiceProvider == null) {
                    showErrorMessage();
                } else {
                    tvUserName.setText(returnedServiceProvider.getUserName());
                    tvServiceProviderId.setText(returnedServiceProvider.getServiceProviderId() + "");
                    tvService.setText(returnedServiceProvider.getServiceName());
                    tvPhone.setText(returnedServiceProvider.getPhone());
                    tvEmail.setText(returnedServiceProvider.getEmail() + "");
                }
            }
        });
    }

    private void showErrorMessage() {
        Log.d(TAG, "ServiceProviderFragment showErrorMessage");
        AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(getActivity().getBaseContext());
        dialogBuilder.setMessage("Get Customer Request Fail!!");
        dialogBuilder.setPositiveButton("Ok", null);
        dialogBuilder.show();
    }
}
