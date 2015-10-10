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

import com.example.chungmin.helpu.callback.Callback;
import com.example.chungmin.helpu.models.CustomerRequest;
import com.example.chungmin.helpu.adapter.CustomerRequestAdapter;
import com.example.chungmin.helpu.models.Globals;
import com.example.chungmin.helpu.R;
import com.example.chungmin.helpu.models.User;
import com.example.chungmin.helpu.models.UserLocalStore;
import com.example.chungmin.helpu.serverrequest.CustomerRequestManager;

import java.util.List;


public class CustomerRequestList extends HelpUBaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_customer_request_list);

        FragmentManager fm = getFragmentManager();
        if (fm.findFragmentById(android.R.id.content) == null) {
            GetCustomerRequestListFragment list = new GetCustomerRequestListFragment();
            fm.beginTransaction().add(android.R.id.content, list).commit();
        }

        isAllowMenuProgressBar = false;
    }

    public static class GetCustomerRequestListFragment extends ListFragment {
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

            //Getting list view from xml
            ListView lv = getListView();

            // Creating a button - Load More
            Button btnDone = new Button(mContext);
            btnDone.setText(R.string.strDone);
            btnDone.setBackgroundResource(R.drawable.custom_btn_beige);
            btnDone.setOnClickListener(new View.OnClickListener() {
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
            UserLocalStore userLocalStore = new UserLocalStore(mContext);
            User user = userLocalStore.getLoggedInUser();
            int userId = user.getUserId();

            CustomerRequestManager.getListByUserId(userId, "HireList", new Callback.GetCustomerRequestListCallback() {

                @Override
                public void complete(List<CustomerRequest> data) {
                    // create new adapter
                    CustomerRequestAdapter adapter = new CustomerRequestAdapter(mContext, data);
                    // set the adapter to list
                    setListAdapter(adapter);
                }

                @Override
                public void failure(String msg) {
                    msg = ((Globals) mContext.getApplicationContext()).translateErrorType(msg);
                    ((HelpUBaseActivity) mContext).showAlert(msg);
                }
            });
        }
    }
}
