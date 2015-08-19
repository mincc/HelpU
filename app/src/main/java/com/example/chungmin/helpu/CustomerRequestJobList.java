package com.example.chungmin.helpu;

import android.app.ListActivity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

import java.util.List;


public class CustomerRequestJobList extends ListActivity implements CustomerRequestDataListener,View.OnClickListener  {
    private ProgressDialog dialog;
    private String mTypeList = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_customer_request_list);

        Intent intent = getIntent();
        mTypeList = intent.getStringExtra("ListType"); //JobOfferList or JobDoneList

        initView();

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
            public void onItemClick(AdapterView<?> parent,View view,int position,long id){
                /* Display Customer Request Item
                TextView tvCustomerRequestId = (TextView) view.findViewById(R.id.tvCustomerRequestId);
                int customerRequestId = Integer.parseInt((String) tvCustomerRequestId.getText());

                Intent i=new Intent(CustomerRequestList.this,CustomerRequestByID.class);
                Bundle b = new Bundle();
                b.putInt("customerRequestId", customerRequestId);
                i.putExtras(b);
                startActivity(i);
                */

            }
        });
    }

    private void initView() {
        // show progress dialog
        dialog = ProgressDialog.show(this, "", "Loading...");

        String url = "";
        if (mTypeList.equals("JobOfferList")) {
            url = getString(R.string.server_uri) + ((Globals) getApplication()).getCustomerRequestJobListGetByUserID();
        } else if (mTypeList.equals("JobDoneList")) {
            url = getString(R.string.server_uri) + ((Globals) getApplication()).getCustomerRequestJobDoneListGetByUserID();
        }

        UserLocalStore userLocalStore = new UserLocalStore(this);
        User user = userLocalStore.getLoggedInUser();
        String userId = String.valueOf(user.getUserId());

        CustomerRequestDataTask task = new CustomerRequestDataTask(this);
        task.execute(url, userId);
    }

    @Override
    public void Complete(List<CustomerRequest> data) {
        // dismiss the progress dialog
        if (dialog != null) dialog.dismiss();
        // create new adapter
        CustomerRequestAdapter adapter = new CustomerRequestAdapter(this, data);
        // set the adapter to list
        setListAdapter(adapter);
    }

    @Override
    public void Failure(String msg) {
        // dismiss the progress dialog
        if (dialog != null)
            dialog.dismiss();

        // show failure message
        Toast.makeText(this, msg, Toast.LENGTH_LONG).show();
    }

    @Override
    public void onClick(View v) {

    }
}
