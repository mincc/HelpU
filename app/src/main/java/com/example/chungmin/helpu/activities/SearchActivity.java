package com.example.chungmin.helpu.activities;

import android.app.FragmentManager;
import android.app.ListFragment;
import android.app.SearchManager;
import android.content.Context;
import android.content.Intent;
import android.provider.SearchRecentSuggestions;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;

import com.example.chungmin.helpu.R;
import com.example.chungmin.helpu.adapter.CustomerRequestAdapter;
import com.example.chungmin.helpu.callback.Callback;
import com.example.chungmin.helpu.models.AppLink;
import com.example.chungmin.helpu.models.CustomerRequest;
import com.example.chungmin.helpu.models.Globals;
import com.example.chungmin.helpu.models.User;
import com.example.chungmin.helpu.models.UserLocalStore;
import com.example.chungmin.helpu.provider.AppRecentSearchesProvider;
import com.example.chungmin.helpu.serverrequest.CustomerRequestManager;

import java.util.ArrayList;
import java.util.List;

public class SearchActivity extends HelpUBaseActivity {

    private ListView listView;
    private String mQuery;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);

//        listView = (ListView) findViewById(R.id.list);
//        listView.setEmptyView(findViewById(R.id.empty_view));

        Intent intent = getIntent();
        if (Intent.ACTION_SEARCH.equals(intent.getAction())) {
            mQuery = intent.getStringExtra(SearchManager.QUERY);
//            search(query);
        }

        FragmentManager fm = getFragmentManager();
        if (fm.findFragmentById(android.R.id.content) == null) {
            GetCustomerRequestSearchFragment list = new GetCustomerRequestSearchFragment().newInstance(mQuery);
            fm.beginTransaction().add(android.R.id.content, list).commit();
        }

        isAllowMenuProgressBar = false;
    }

    public static class GetCustomerRequestSearchFragment extends ListFragment {
        private static final String ARG_PARAM1 = "query";
        private String mQuery;
        private Context mContext;
        ListView lv;

        public GetCustomerRequestSearchFragment newInstance(String query) {
            GetCustomerRequestSearchFragment fragment = new GetCustomerRequestSearchFragment();
            Bundle args = new Bundle();
            args.putString(ARG_PARAM1, query);
            fragment.setArguments(args);
            return fragment;
        }

        @Override
        public void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            if (getArguments() != null) {
                mQuery = getArguments().getString(ARG_PARAM1);
            }
        }

        @Override
        public View onCreateView(LayoutInflater inflater,
                                 ViewGroup container,
                                 Bundle savedInstanceState) {

            mContext = getActivity();
            initView();

            return super.onCreateView(inflater, container, savedInstanceState);
        }

        public void onActivityCreated(Bundle savedInstanceState) {
            super.onActivityCreated(savedInstanceState);

            // Getting list view from xml
            lv = getListView();
            lv.setEmptyView(getActivity().findViewById(R.id.tvNoItemFound));

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

            UserLocalStore userLocalStore = new UserLocalStore(mContext);
            User user = userLocalStore.getLoggedInUser();
            int userId = user.getUserId();

            saveRecentQuery(mQuery);
            int customerRequestId = Integer.parseInt(mQuery.toLowerCase());
            CustomerRequestManager.getByID(customerRequestId, new Callback.GetCustomerRequestCallback() {

                @Override
                public void complete(CustomerRequest returnedCustomerRequest) {
                    List<CustomerRequest> data = new ArrayList<CustomerRequest>();
                    data.add(returnedCustomerRequest);

                    // create new adapter
                    CustomerRequestAdapter adapter = new CustomerRequestAdapter(mContext, data);
                    // set the adapter to list
                    setListAdapter(adapter);
                }

                @Override
                public void failure(String msg) {
                    if (msg.equals("No Item Found")) {
                        setListShown(true);
                    } else {
                        msg = ((Globals) mContext.getApplicationContext()).translateErrorType(msg);
                        ((HelpUBaseActivity) mContext).showAlert(msg);
                    }
                }
            });
        }

        private void saveRecentQuery(String query) {
            SearchRecentSuggestions suggestions = new SearchRecentSuggestions(
                    mContext,
                    AppRecentSearchesProvider.AUTHORITY,
                    AppRecentSearchesProvider.MODE);

            suggestions.saveRecentQuery(query, null);
        }
    }
}
