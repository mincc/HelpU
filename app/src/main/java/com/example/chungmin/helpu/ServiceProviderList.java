package com.example.chungmin.helpu;

import android.app.ListActivity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.List;


public class ServiceProviderList extends ListActivity implements FetchServiceProviderDataListener,View.OnClickListener {
    private ProgressDialog dialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_service_provider_list);
        initView();

        TextView tvTitle = (TextView) this.findViewById(R.id.tvTitle);
        tvTitle.setText("Service Provided");

        // Getting listview from xml
        ListView lv = getListView();

        // Creating a button - Load More
        Button btnDone = new Button(this);
        btnDone.setText("Done");
        btnDone.setOnClickListener(new Button.OnClickListener() {
            public void onClick(View v) {
                Intent redirect = new Intent(v.getContext(), MainActivity.class);
                redirect.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(redirect);
                finish();
            }
        });

        // Adding button to listview at footer
        lv.addFooterView(btnDone);
        lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                TextView tvServiceProviderId = (TextView) view.findViewById(R.id.tvServiceProviderId);
                int serviceProviderId = Integer.parseInt((String) tvServiceProviderId.getText());

                Intent i = new Intent(ServiceProviderList.this, ServiceProviderByID.class);
                Bundle b = new Bundle();
                b.putInt("serviceProviderId", serviceProviderId);
                i.putExtras(b);
                startActivity(i);
                finish();
            }
        });

    }

    private void initView() {
        // show progress dialog
        dialog = ProgressDialog.show(this, "", "Loading...");

        String url = getString(R.string.server_uri) + ((Globals)getApplication()).getServiceProviderGetByUserID();

        UserLocalStore userLocalStore = new UserLocalStore(this);
        User user = userLocalStore.getLoggedInUser();
        String userId = String.valueOf(user.getUserId());

        FetchServiceProviderDataTask task = new FetchServiceProviderDataTask(this);
        task.execute(url, userId);
    }

    @Override
    public void Complete(List<ServiceProvider> data) {
        // dismiss the progress dialog
        if (dialog != null) dialog.dismiss();
        // create new adapter
        ServiceProviderAdapter adapter = new ServiceProviderAdapter(this, data, true);
        // set the adapter to list
        setListAdapter(adapter);
    }

    @Override
    public void Failure(String msg) {
        // dismiss the progress dialog
        if (dialog != null) dialog.dismiss();
        // show failure message
        Toast.makeText(this, msg, Toast.LENGTH_LONG).show();
    }

    @Override
    public void onClick(View v) {

    }
}