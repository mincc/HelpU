package fragments;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.example.chungmin.helpu.ProjectStatus;
import com.example.chungmin.helpu.R;
import com.example.chungmin.helpu.UserProfileSetting;

public class UserInfoFragment extends Fragment {

    private static final String ARG_PARAM1 = "username";
    private static final String ARG_PARAM2 = "projectStatus";
    private static final String ARG_PARAM3 = "userId";
    private static final String ARG_PARAM4 = "customerRequestUserId";

    private String mUsername;
    private ProjectStatus mProjectStatus;
    private int mUserId;
    private int mCustomerRequestUserId;

    TextView txUsername, lblProjectStatus, tvProjectStatus;

    public static UserInfoFragment newInstance(String username, ProjectStatus projectStatus, int userId, int customerRequestUserId) {
        UserInfoFragment fragment = new UserInfoFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, username);
        args.putSerializable(ARG_PARAM2, projectStatus);
        args.putInt(ARG_PARAM3, userId);
        args.putInt(ARG_PARAM4, customerRequestUserId);
        fragment.setArguments(args);
        return fragment;
    }

    public UserInfoFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mUsername = getArguments().getString(ARG_PARAM1);
            mProjectStatus = (ProjectStatus) getArguments().get(ARG_PARAM2);
            mUserId = getArguments().getInt(ARG_PARAM3);
            mCustomerRequestUserId = getArguments().getInt(ARG_PARAM4);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_user_info, container, false);

        txUsername = (TextView) v.findViewById(R.id.txUsername);
        lblProjectStatus = (TextView) v.findViewById(R.id.lblProjectStatus);
        tvProjectStatus = (TextView) v.findViewById(R.id.tvProjectStatus);

        txUsername.setText(mUsername);

        txUsername.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Intent intent = new Intent(v.getContext(), UserProfileSetting.class);
                startActivity(intent);
                getActivity().finish();
            }
        });

        if (mProjectStatus != null) {

//            switch (mProjectStatus) {
//                case Pick:
//                    if (mUserId == mCustomerRequestUserId) {
//                        tvProjectStatus.setText(getString(R.string.strNotificationSend));
//                        lblProjectStatus.setVisibility(View.VISIBLE);
//                        tvProjectStatus.setVisibility(View.VISIBLE);
//                    } else {
//                    }
//                    break;
//                case SelectedNotification:
//                    if (mUserId == mCustomerRequestUserId) {
//                    } else {
//                        tvProjectStatus.setText(getString(R.string.strWaitingConfirmation));
//                        lblProjectStatus.setVisibility(View.VISIBLE);
//                        tvProjectStatus.setVisibility(View.VISIBLE);
//                    }
//                    break;
//                case ConfirmRequest:
//                    if (mUserId == mCustomerRequestUserId) {
//                    } else {
//                    }
//                    break;
//                case Quotation:
//                    if (mUserId == mCustomerRequestUserId) {
//
//                    } else {
//
//                    }
//                    tvProjectStatus.setText(getString(R.string.strCreateQuotation));
//                    lblProjectStatus.setVisibility(View.VISIBLE);
//                    tvProjectStatus.setVisibility(View.VISIBLE);
//                    break;
//                case ConfirmQuotation:
//                    if (mUserId == mCustomerRequestUserId) {
//                    } else {
//
//                    }
//                    tvProjectStatus.setText(getString(R.string.strToConfirmPriceQuoted));
//                    lblProjectStatus.setVisibility(View.VISIBLE);
//                    tvProjectStatus.setVisibility(View.VISIBLE);
//                    break;
//                case Deal:
//                    if (mUserId == mCustomerRequestUserId) {
//                    } else {
//                    }
//                    break;
//                case DealNotification:
//                    if (mUserId == mCustomerRequestUserId) {
//                    } else {
//                        tvProjectStatus.setText(getString(R.string.strDeal));
//                        lblProjectStatus.setVisibility(View.VISIBLE);
//                        tvProjectStatus.setVisibility(View.VISIBLE);
//                    }
//                    break;
//                case ReceiveDownPayment:
//                    if (mUserId == mCustomerRequestUserId) {
//                    } else {
//
//                    }
//                    tvProjectStatus.setText(getString(R.string.strDownPaymentReleased));
//                    lblProjectStatus.setVisibility(View.VISIBLE);
//                    tvProjectStatus.setVisibility(View.VISIBLE);
//                    break;
//                case ServiceStart:
//                    if (mUserId == mCustomerRequestUserId) {
//                    } else {
//                    }
//                    break;
//                case ServiceDone:
//                    if (mUserId == mCustomerRequestUserId) {
//                    } else {
//                    }
//                    break;
//                case CustomerRating:
//                    if (mUserId == mCustomerRequestUserId) {
//                    } else {
//                    }
//                    break;
//                case ServiceProvRating:
//                    if (mUserId == mCustomerRequestUserId) {
//                    } else {
//                    }
//                    break;
//                case ProjectClose:
//                    break;
//                default:
//                    Toast.makeText(getActivity(), getString(R.string.strUnknownAction) , Toast.LENGTH_LONG).show();
//                    break;
//            }
        }

        //temporary totally remove and dun used
        lblProjectStatus.setVisibility(View.GONE);
        tvProjectStatus.setVisibility(View.GONE);
        return v;

    }


}
