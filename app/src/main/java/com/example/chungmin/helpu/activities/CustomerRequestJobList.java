package com.example.chungmin.helpu.activities;

import android.app.FragmentManager;
import android.app.ListFragment;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

import com.example.chungmin.helpu.models.CustomerRequest;
import com.example.chungmin.helpu.adapter.CustomerRequestAdapter;
import com.example.chungmin.helpu.serverrequest.CustomerRequestDataTask;
import com.example.chungmin.helpu.callback.GetCustomerRequestListCallback;
import com.example.chungmin.helpu.models.Globals;
import com.example.chungmin.helpu.R;
import com.example.chungmin.helpu.models.User;
import com.example.chungmin.helpu.models.UserLocalStore;

import java.util.List;


public class CustomerRequestJobList extends HelpUBaseActivity {

    private static String mTypeList = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_customer_request_list);

        Intent intent = getIntent();
        mTypeList = intent.getStringExtra("ListType"); //JobOfferList or JobDoneList

        if (mTypeList.equals("JobOfferList")) {
            setTitle(R.string.strJobOfferList);
        } else {
            setTitle(R.string.strJobDoneList);
        }

        FragmentManager fm = getFragmentManager();
        if (fm.findFragmentById(android.R.id.content) == null) {
            GetCustomerRequestJobStatusListFragment list = new GetCustomerRequestJobStatusListFragment();
            fm.beginTransaction().add(android.R.id.content, list).commit();
        }

        isAllowMenuProgressBar = false;
    }

    public static class GetCustomerRequestJobStatusListFragment extends ListFragment implements GetCustomerRequestListCallback {
        private Context mContext;

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container,
                                 Bundle savedInstanceState) {

            mContext = getActivity();
            initView();

            return super.onCreateView(inflater, container, savedInstanceState);
        }

        public void onActivityCreated(Bundle savedInstanceState) {
            super.onActivityCreated(savedInstanceState);

            // Getting list view from xml
            ListView lv = getListView();

            // Creating a button - Load More
            Button btnDone = new Button(mContext);
            btnDone.setText(R.string.strDone);
            btnDone.setBackgroundResource(R.drawable.custom_btn_beige);
            btnDone.setOnClickListener(new Button.OnClickListener() {
                public void onClick(View v) {
                    Intent redirect = new Intent(v.getContext(), MainActivity.class);
                    redirect.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    startActivity(redirect);
                    getActivity().finish();
                }
            });

            // Adding button to list view at footer
            lv.addFooterView(btnDone);

        }

        private void initView() {

            String url = "";
            if (mTypeList.equals("JobOfferList")) {
                url = getString(R.string.server_uri) + ((Globals) mContext.getApplicationContext()).getCustomerRequestJobListGetByUserIDUrl();
            } else if (mTypeList.equals("JobDoneList")) {
                url = getString(R.string.server_uri) + ((Globals) mContext.getApplicationContext()).getCustomerRequestJobDoneListGetByUserIDUrl();
            }

            UserLocalStore userLocalStore = new UserLocalStore(mContext);
            User user = userLocalStore.getLoggedInUser();
            String userId = String.valueOf(user.getUserId());

            CustomerRequestDataTask task = new CustomerRequestDataTask(this);
            task.execute(url, userId);
        }

        @Override
        public void Complete(List<CustomerRequest> data) {

            // create new adapter
            CustomerRequestAdapter adapter = new CustomerRequestAdapter(mContext, data);
            // set the adapter to list
            setListAdapter(adapter);
        }

        @Override
        public void Failure(String msg) {
            // show failure message
            Toast.makeText(mContext, msg, Toast.LENGTH_LONG).show();
        }

    }
}
