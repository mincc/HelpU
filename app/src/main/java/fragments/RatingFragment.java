package fragments;

import android.content.Intent;
import android.os.Bundle;
import android.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.RatingBar;
import android.widget.TextView;

import com.example.chungmin.helpu.activities.HelpUBaseActivity;
import com.example.chungmin.helpu.activities.MainActivity;
import com.example.chungmin.helpu.callback.Callback;
import com.example.chungmin.helpu.models.Rating;
import com.example.chungmin.helpu.serverrequest.RatingManager;
import com.example.chungmin.helpu.models.Globals;
import com.example.chungmin.helpu.R;


public class RatingFragment extends Fragment {

    private static final String ARG_PARAM1 = "type";
    private static final String ARG_PARAM2 = "voterId";
    private static final String ARG_PARAM3 = "targetUserId";
    private static final String ARG_PARAM4 = "customerRequestId";

    private String mType;
    private int mVoterId;
    private int mTargetUserId;
    private int mCustomerRequestId;
    private RatingBar ratingBar;
    private TextView lblRateMe;
    private Button btnSubmit;
    
    public static RatingFragment newInstance(String type, int voterId, int targetUserId, int customerRequestId) {
        RatingFragment fragment = new RatingFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, type);
        args.putInt(ARG_PARAM2, voterId);
        args.putInt(ARG_PARAM3, targetUserId);
        args.putInt(ARG_PARAM4, customerRequestId);
        fragment.setArguments(args);
        return fragment;
    }

    public RatingFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mType = getArguments().getString(ARG_PARAM1);
            mVoterId = getArguments().getInt(ARG_PARAM2);
            mTargetUserId = getArguments().getInt(ARG_PARAM3);
            mCustomerRequestId = getArguments().getInt(ARG_PARAM4);
        }
    }


    public void addListenerOnButton(View view) {

        ratingBar = (RatingBar) view.findViewById(R.id.ratingBar);
        btnSubmit = (Button) view.findViewById(R.id.btnSubmit);

        //if click on me, then display the current rating value.
        btnSubmit.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                Rating rating = new Rating();
                rating.setVoterId(mVoterId);
                rating.setTargetUserId(mTargetUserId);
                rating.setRatingValue(ratingBar.getRating());
                if(mType == "Customer") {
                    rating.setRatingType("c");
                }else{
                    rating.setRatingType("s");
                }
                rating.setCustomerRequestId(mCustomerRequestId);
                //Toast.makeText(getActivity(), String.valueOf(ratingBar.getRating()), Toast.LENGTH_SHORT).show();
                RatingManager.insert(rating, new Callback.GetRatingCallback() {
                    @Override
                    public void complete(Rating returnedRating) {
                    }

                    @Override
                    public void failure(String msg) {
                        msg = ((Globals) getActivity().getApplication()).translateErrorType(msg);
                        ((HelpUBaseActivity) getActivity()).showAlert(msg);
                    }
                });

                Intent redirect = new Intent(getActivity(), MainActivity.class);
                redirect.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(redirect);
                getActivity().finish();
            }

        });

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_rating, container, false);

        lblRateMe = (TextView)view.findViewById(R.id.lblRateMe);
        if(mType == "Customer") {
            lblRateMe.setText(R.string.strCustRate);
        }else {
            lblRateMe.setText(R.string.strSPdrRate);
        }

        addListenerOnButton(view);
        return view;
    }

}
