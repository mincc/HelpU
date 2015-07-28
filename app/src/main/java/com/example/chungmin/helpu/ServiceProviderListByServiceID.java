package com.example.chungmin.helpu;

import android.app.AlertDialog;
import android.app.ListActivity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.List;


public class ServiceProviderListByServiceID extends ListActivity implements FetchServiceProviderDataListener,View.OnClickListener {
    private ProgressDialog dialog;
    private TextView tvTitle;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_service_provider_list);
        initView(this.getBaseContext());

        TextView tvTitle = (TextView) this.findViewById(R.id.tvTitle);
        tvTitle.setText("Please Select One of the Service Provider...");

        // Getting listview from xml
        ListView lv = getListView();

        // Adding button to listview at footer
        lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                TextView tvServiceProviderId = (TextView) view.findViewById(R.id.tvServiceProviderId);
                int serviceProviderId = Integer.parseInt((String) tvServiceProviderId.getText());

                Intent i = new Intent(ServiceProviderListByServiceID.this, ServiceProviderByID.class);
                Bundle b = new Bundle();
                b.putInt("serviceProviderId", serviceProviderId);
                i.putExtras(b);
                startActivity(i);
            }
        });

    }

    private void initView(final Context context) {
        //get from bundle
        Bundle b = getIntent().getExtras();
        int serviceId =b.getInt("serviceId", 0);

        String url = getString(R.string.server_uri) + "ServiceProviderGetByServiceID.php";
        ServiceProviderServerRequests serverRequest = new ServiceProviderServerRequests(this);
        serverRequest.getServiceProviderByServiceID(serviceId, url, new GetServiceProviderListCallback() {
            @Override
            public void done(List<ServiceProvider> returnedServiceProviderList) {
                if (returnedServiceProviderList == null) {
                    showErrorMessage();
                } else {
                    // create new adapter
                    ServiceProviderAdapter adapter = new ServiceProviderAdapter(context, returnedServiceProviderList);
                    // set the adapter to list
                    setListAdapter(adapter);
                }
            }
        });
    }

    private void showErrorMessage() {
        AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(ServiceProviderListByServiceID.this);
        dialogBuilder.setMessage("Get Service Provider By Service ID Fail!!");
        dialogBuilder.setPositiveButton("Ok", null);
        dialogBuilder.show();
    }

    @Override
    public void onFetchComplete(List<ServiceProvider> data) {
        // dismiss the progress dialog
        if (dialog != null) dialog.dismiss();
        // create new adapter
        ServiceProviderAdapter adapter = new ServiceProviderAdapter(this, data);
        // set the adapter to list
        setListAdapter(adapter);
    }

    @Override
    public void onFetchFailure(String msg) {
        // dismiss the progress dialog
        if (dialog != null) dialog.dismiss();
        // show failure message
        Toast.makeText(this, msg, Toast.LENGTH_LONG).show();
    }

    @Override
    public void onClick(View v) {

    }
}