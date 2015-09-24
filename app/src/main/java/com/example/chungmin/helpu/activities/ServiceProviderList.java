package com.example.chungmin.helpu.activities;

import android.app.FragmentManager;
import android.app.ListFragment;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.chungmin.helpu.serverrequest.ServiceProviderDataTask;
import com.example.chungmin.helpu.models.Globals;
import com.example.chungmin.helpu.R;
import com.example.chungmin.helpu.adapter.ServiceProviderAdapter;
import com.example.chungmin.helpu.models.UserLocalStore;
import com.example.chungmin.helpu.callback.FetchServiceProviderDataListener;
import com.example.chungmin.helpu.models.ServiceProvider;
import com.example.chungmin.helpu.models.User;

import java.util.List;


public class ServiceProviderList extends HelpUBaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_service_provider_list);

        setTitle(R.string.strServiceProvided);
//        TextView tvTitle = (TextView) this.findViewById(R.id.tvTitle);
//        tvTitle.setText("Service Provided");

        FragmentManager fm = getFragmentManager();
        if (fm.findFragmentById(android.R.id.content) == null) {
            ServiceProviderListFragment list = new ServiceProviderListFragment();
            fm.beginTransaction().add(android.R.id.content, list).commit();
        }

        isAllowMenuProgressBar = false;
    }

    public static class ServiceProviderListFragment extends ListFragment implements FetchServiceProviderDataListener {
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

            // Getting listview from xml
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

            // Adding button to listview at footer
            lv.addFooterView(btnDone);
            lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    TextView tvServiceProviderId = (TextView) view.findViewById(R.id.tvServiceProviderId);
                    int serviceProviderId = Integer.parseInt((String) tvServiceProviderId.getText());

                    Intent i = new Intent(mContext, ServiceProviderByID.class);
                    Bundle b = new Bundle();
                    b.putInt("serviceProviderId", serviceProviderId);
                    i.putExtras(b);
                    startActivity(i);
                    getActivity().finish();
                }
            });

        }

        private void initView() {

            String url = getString(R.string.server_uri) + ((Globals) getActivity().getApplication()).getServiceProviderGetByUserIDUrl();

            UserLocalStore userLocalStore = new UserLocalStore(mContext);
            User user = userLocalStore.getLoggedInUser();
            String userId = String.valueOf(user.getUserId());

            ServiceProviderDataTask task = new ServiceProviderDataTask(this);
            task.execute(url, userId);
        }


        @Override
        public void Complete(List<ServiceProvider> data) {
            // create new adapter
            ServiceProviderAdapter adapter = new ServiceProviderAdapter(mContext, data, true);
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