package fragments;

import android.app.AlertDialog;
import android.content.Intent;
import android.os.Bundle;
import android.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.chungmin.helpu.CountListener;
import com.example.chungmin.helpu.CustomerRequestJobList;
import com.example.chungmin.helpu.Globals;
import com.example.chungmin.helpu.R;
import com.example.chungmin.helpu.ServiceProviderServerRequests;

public class JobDoneFragment extends Fragment {

    private static final String ARG_PARAM1 = "userId";

    private int mUserId;
    private TextView tvJobDone;

    public static JobDoneFragment newInstance(int userId) {
        JobDoneFragment fragment = new JobDoneFragment();
        Bundle args = new Bundle();
        args.putInt(ARG_PARAM1, userId);
        fragment.setArguments(args);
        return fragment;
    }

    public JobDoneFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mUserId = getArguments().getInt(ARG_PARAM1);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_job_done, container, false);

        tvJobDone = (TextView) view.findViewById(R.id.tvJobDone);
        getTotalJobDone();

        tvJobDone.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Intent intent = new Intent(v.getContext(), CustomerRequestJobList.class);
                intent.putExtra("ListType", "JobDoneList");
                startActivity(intent);
            }
        });
        return view;
    }


    private void getTotalJobDone() {
        String url = getString(R.string.server_uri) + ((Globals) this.getActivity().getApplication()).getServiceProviderTotalJobDone();
        ServiceProviderServerRequests serverRequest = new ServiceProviderServerRequests(this.getActivity().getBaseContext());
        serverRequest.getServiceProviderTotalJobOffer(mUserId, url, new CountListener() {
            @Override
            public void CountComplete(int data) {
                tvJobDone.setText(Integer.toString(data));
            }

            @Override
            public void CountFailure(String msg) {
                showErrorMessage(msg);
            }
        });
    }

    private void showErrorMessage(String msg) {
        AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(this.getActivity().getBaseContext());
        dialogBuilder.setMessage(msg);
        dialogBuilder.setPositiveButton("Ok", null);
        dialogBuilder.show();
    }

}
