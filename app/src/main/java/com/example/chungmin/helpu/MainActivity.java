package com.example.chungmin.helpu;

import android.app.AlertDialog;
import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import fragments.JobDoneFragment;
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
            String userId = String.valueOf(user.getUserId());
            retrieveWorkCount(userId);
            retrieveHireCount(userId);

            ((Globals) this.getApplication()).setUserId(user.getUserId());
            alarm = new AlarmManagerBroadcastReceiver();
            alarm.SetAlarm(this);


            if (savedInstanceState == null) {
                FragmentManager fm = getFragmentManager();
                FragmentTransaction ft = fm.beginTransaction();
                Fragment frag = new JobOfferFragment().newInstance(user.getUserId());
                ft.add(R.id.llJobOffer, frag);
                frag = new JobDoneFragment().newInstance(user.getUserId());
                ft.add(R.id.llJobDone, frag);
                ft.commit();
            }

        }

    }


    @Override
    public void onClick(View v) {
        Intent redirect = null;
        switch(v.getId()){
            case R.id.bLogout:
                alarm.CancelAlarm(this);
                userLocalStore.clearUserData();
                userLocalStore.setUserLoggedIn(false);
                redirect = new Intent(this, Login.class);
                break;
            case R.id.bHire:
                redirect = new Intent(this, Hire.class);
                break;
            case R.id.bWork:
                redirect = new Intent(this, Work.class);
                break;
        }
        startActivity(redirect);
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
            finish();
            return false;
        }
        return true;
    }

    private void displayUserDetails() {
        User user = userLocalStore.getLoggedInUser();
        txUsername.setText(user.getUsername());
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

    @Override
    public void onBackPressed() {
        new AlertDialog.Builder(this)
                .setTitle("Really Exit?")
                .setMessage("Are you sure you want to exit?")
                .setNegativeButton(android.R.string.no, null)
                .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface arg0, int arg1) {
                        MainActivity.super.onBackPressed();
                        finish();
                    }
                }).create().show();
    }
}
