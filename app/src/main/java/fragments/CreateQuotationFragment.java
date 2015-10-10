package fragments;

import android.content.Intent;
import android.os.Bundle;
import android.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import com.example.chungmin.helpu.activities.HelpUBaseActivity;
import com.example.chungmin.helpu.callback.Callback;
import com.example.chungmin.helpu.models.CustomerRequest;
import com.example.chungmin.helpu.serverrequest.CustomerRequestManager;
import com.example.chungmin.helpu.models.Globals;
import com.example.chungmin.helpu.activities.ProjectMessages;
import com.example.chungmin.helpu.enumeration.ProjectStatus;
import com.example.chungmin.helpu.R;

public class CreateQuotationFragment extends Fragment {

    private static final String ARG_PARAM1 = "customerRequest";

    private EditText etQuotation;
    private Button btnDone;
    private CustomerRequest mCustomerRequest;


    public static CreateQuotationFragment newInstance(CustomerRequest customerRequest) {
        CreateQuotationFragment fragment = new CreateQuotationFragment();
        Bundle args = new Bundle();
        args.putParcelable(ARG_PARAM1, customerRequest);
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
            mCustomerRequest = getArguments().getParcelable(ARG_PARAM1);
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
                mCustomerRequest.setProjectStatusId(ProjectStatus.ConfirmQuotation.getId());
                mCustomerRequest.setQuotation(Double.parseDouble(etQuotation.getText().toString()));
                CustomerRequestManager.update(mCustomerRequest, new Callback.GetCustomerRequestCallback() {
                    @Override
                    public void complete(CustomerRequest returnedCustomerRequest) {
                        Intent redirect = new Intent(getActivity(), ProjectMessages.class);
                        Bundle b = new Bundle();
                        b.putInt("projectStatusId", mCustomerRequest.getProjectStatusId());
                        b.putInt("customerRequestId", mCustomerRequest.getCustomerRequestId());
                        redirect.putExtras(b);
                        redirect.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        startActivity(redirect);
                        getActivity().finish();
                    }

                    @Override
                    public void failure(String msg) {
                        msg = ((Globals) getActivity().getApplication()).translateErrorType(msg);
                        ((HelpUBaseActivity) getActivity()).showAlert(msg);
                    }
                });
            }

        });

    }

}
