package com.example.chungmin.helpu;

import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import fragments.JobOfferFragment;


public class MainActivity extends ActionBarActivity implements FetchServiceProviderCountListener, View.OnClickListener, CountListener {
    private ProgressDialog dialog;
    private ProgressDialog dialogCustomerRequest;
    UserLocalStore userLocalStore;
    TextView txUsername,tvTotalCountHire,tvTotalCountWork;
    Button bLogout, bHire, bWork;
    private AlarmManagerBroadcastReceiver alarm;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        txUsername = (TextView) findViewById(R.id.txUsername);
        tvTotalCountHire = (TextView) findViewById(R.id.tvTotalCountHire);
        tvTotalCountWork = (TextView) findViewById(R.id.tvTotalCountWork);
        bLogout = (Button) findViewById(R.id.bLogout);
        bHire  = (Button) findViewById(R.id.bHire);
        bWork = (Button) findViewById(R.id.bWork);

        bLogout.setOnClickListener(this);
        bHire.setOnClickListener(this);
        bWork.setOnClickListener(this);

        txUsername.setOnClickListener(new View.OnClickListener(){
            public void onClick(View v){
                Intent intent = new Intent(v.getContext(), UserProfileSetting.class);
                startActivity(intent);
            }
        });

        tvTotalCountHire.setOnClickListener(new View.OnClickListener(){
            public void onClick(View v){
                Intent intent = new Intent(v.getContext(), CustomerRequestList.class);
                startActivity(intent);
            }
        });

        tvTotalCountWork.setOnClickListener(new View.OnClickListener(){
            public void onClick(View v){
                Intent intent = new Intent(v.getContext(), ServiceProviderList.class);
                startActivity(intent);
            }
        });

        userLocalStore = new UserLocalStore(this);
        User user = userLocalStore.getLoggedInUser();
        if(user != null) {
            String userId = String.valueOf(user.userId);
            retrieveWorkCount(userId);
            retrieveHireCount(userId);

            ((Globals)this.getApplication()).setUserId(user.userId);
            alarm = new AlarmManagerBroadcastReceiver();
            alarm.SetAlarm(this);


            if (savedInstanceState == null) {
                FragmentManager fm = getFragmentManager();
                FragmentTransaction ft = fm.beginTransaction();
                Fragment frag = new JobOfferFragment().newInstance(user.userId);
                ft.add(R.id.llJobOffer, frag);
                ft.commit();
            }

        }

    }


    @Override
    public void onClick(View v) {
        Intent redirect;
        switch(v.getId()){
            case R.id.bLogout:
                alarm.CancelAlarm(this);
                userLocalStore.clearUserData();
                userLocalStore.setUserLoggedIn(false);
                Intent loginIntent = new Intent(this, Login.class);
                startActivity(loginIntent);
                break;
            case R.id.bHire:
                redirect = new Intent(this, Hire.class);
                startActivity(redirect);
                break;
            case R.id.bWork:
                redirect = new Intent(this, Work.class);
                startActivity(redirect);
                break;
        }
    }

    @Override
    protected void onStart() {
        super.onStart();
        if (authenticate() == true) {
            displayUserDetails();
        }
    }

    private boolean authenticate() {
        if (userLocalStore.getLoggedInUser() == null) {
            Intent intent = new Intent(this, Login.class);
            startActivity(intent);
            return false;
        }
        return true;
    }

    private void displayUserDetails() {
        User user = userLocalStore.getLoggedInUser();
        txUsername.setText(user.username);
    }

    private void retrieveWorkCount(String userId) {
        // show progress dialog
        dialog = ProgressDialog.show(this, "", "Loading...");
        String url = getString(R.string.server_uri) + ((Globals)getApplication()).getServiceProviderGetCount();
        FetchServiceProviderCountTask task = new FetchServiceProviderCountTask(this);
        task.execute(url, userId);
    }

    private void retrieveHireCount(String userId) {
        // show progress dialog
        dialogCustomerRequest = ProgressDialog.show(this, "", "Loading...");
        String url = getString(R.string.server_uri) + ((Globals)getApplication()).getCustomerRequestGetCount();
        CustomerRequestCountTask task = new CustomerRequestCountTask(this);
        task.execute(url, userId);
    }

    @Override
    public void Complete(int data) {
        // dismiss the progress dialog
        if (dialog != null)
            dialog.dismiss();

        tvTotalCountWork.setText(Integer.toString(data));
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
    public void CountComplete(int data) {
        // dismiss the progress dialog
        if (dialogCustomerRequest != null) {
            dialogCustomerRequest.dismiss();
        }

        tvTotalCountHire.setText(Integer.toString(data));
    }

    @Override
    public void CountFailure(String msg) {
        // dismiss the progress dialog
        if (dialogCustomerRequest != null)
            dialogCustomerRequest.dismiss();
        // show failure message
        Toast.makeText(this, msg, Toast.LENGTH_LONG).show();
    }
}
