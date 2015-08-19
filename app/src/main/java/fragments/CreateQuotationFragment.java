package fragments;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RatingBar;

import com.example.chungmin.helpu.CustomerRequest;
import com.example.chungmin.helpu.CustomerRequestServerRequests;
import com.example.chungmin.helpu.GetCustomerRequestCallback;
import com.example.chungmin.helpu.GetRatingCallback;
import com.example.chungmin.helpu.Globals;
import com.example.chungmin.helpu.MainActivity;
import com.example.chungmin.helpu.ProjectMessages;
import com.example.chungmin.helpu.ProjectStatus;
import com.example.chungmin.helpu.R;
import com.example.chungmin.helpu.Rating;
import com.example.chungmin.helpu.RatingServerRequests;
import com.example.chungmin.helpu.ServiceProviderConfirmQuotation;

public class CreateQuotationFragment extends Fragment {

    private static final String ARG_PARAM1 = "customerRequestId";

    private int mCustomerRequestId;

    private EditText etQuotation;
    private Button btnDone;

    public static CreateQuotationFragment newInstance(int customerRequestId) {
        CreateQuotationFragment fragment = new CreateQuotationFragment();
        Bundle args = new Bundle();
        args.putInt(ARG_PARAM1, customerRequestId);
        fragment.setArguments(args);
        return fragment;
    }

    public CreateQuotationFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mCustomerRequestId = getArguments().getInt(ARG_PARAM1);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_create_quoation, container, false);

        addListenerOnButton(view);
        return view;
    }


    public void addListenerOnButton(View view) {

        etQuotation = (EditText) view.findViewById(R.id.etQuotation);
        btnDone = (Button) view.findViewById(R.id.btnDone);

        //if click on me, then display the current rating value.
        btnDone.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                final CustomerRequest customerRequest = ((Globals)getActivity().getApplication()).getCustomerRequest();
                customerRequest.setProjectStatusId(ProjectStatus.ConfirmQuotation.getId());
                customerRequest.setQuotation(Double.parseDouble(etQuotation.getText().toString()));
                String url = getString(R.string.server_uri) + ((Globals)getActivity().getApplicationContext()).getCustomerRequestUpdate();
                CustomerRequestServerRequests serverRequest = new CustomerRequestServerRequests(getActivity().getBaseContext());
                serverRequest.getCustomerRequestUpdate(customerRequest, url, new GetCustomerRequestCallback() {
                    @Override
                    public void done(CustomerRequest returnedCustomerRequest) {
                        Intent redirect = new Intent(getActivity(), ProjectMessages.class);
                        Bundle b = new Bundle();
                        b.putInt("projectStatusId", customerRequest.getProjectStatusId());
                        redirect.putExtras(b);
                        redirect.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        startActivity(redirect);
                        getActivity().finish();
                    }
                });
            }

        });

    }

}
