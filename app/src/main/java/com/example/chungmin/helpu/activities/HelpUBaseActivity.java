package com.example.chungmin.helpu.activities;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import com.example.chungmin.helpu.models.Globals;
import com.example.chungmin.helpu.R;

//https://tausiq.wordpress.com/2013/01/18/android-base-activity-class-example/
public abstract class HelpUBaseActivity extends ActionBarActivity {
    private MenuItem mProgressMenu = null;
    private ProgressDialog mProgressDialog;
    public boolean isAllowMenuProgressBar = false;
    public boolean mIsNetworkAvailable = true;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (!this.isNetworkAvailable()) {
            this.errorDialogExit(getString(R.string.strNetworkIssue), getString(R.string.strMakeSureGotInternet));
            mIsNetworkAvailable = false;
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        int userId = ((Globals) getApplication()).getUserId();
        if (userId == 1) {
            getMenuInflater().inflate(R.menu.menu_admin, menu);
        } else {
            getMenuInflater().inflate(R.menu.menu_common, menu);
        }

        if (isAllowMenuProgressBar) {
            mProgressMenu = menu.findItem(R.id.menu_progress);
            showMenuProgress();
        }
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        int id = item.getItemId();
        Intent redirect = null;
        switch (id) {
//            case R.id.action_settings:
//                //redirect = new Intent(this, Setting.class);
//                break;
            case R.id.action_aboutus:
                redirect = new Intent(this, AboutUs.class);
                break;
            case R.id.action_contactus:
                redirect = new Intent(this, ContactUs.class);
                break;
            case R.id.action_serverinfo:
                redirect = new Intent(this, ServerInfo.class);
                break;
            case R.id.action_sendemail:
                redirect = new Intent(this, SendEmail.class);
                break;
            case R.id.action_appstats:
                redirect = new Intent(this, AppStatsSummary.class);
                break;
            case R.id.action_changepassword:
                redirect = new Intent(this, ChangePasswordActivity.class);
                break;
//            case R.id.action_project_status_flow:
//                redirect = new Intent(this, ProjectStatusFlow.class);
//                break;
            default:
                Toast.makeText(this, "Still not create redirect link", Toast.LENGTH_LONG).show();
                break;
        }

        startActivity(redirect);
        finish();

        return super.onOptionsItemSelected(item);
    }

    private void showMenuProgress() {
        if (mProgressMenu != null) {
            mProgressMenu.setVisible(true);
        }
    }

    public void hideMenuProgress() {
        if (mProgressMenu != null) {
            mProgressMenu.setVisible(false);
        }
    }

    protected void showProgress(String msg) {
        if (mProgressDialog != null && mProgressDialog.isShowing())
            dismissProgress();

        mProgressDialog = ProgressDialog.show(this, getResources().getString(R.string.app_name), msg);
    }

//    protected void showLoading(Context context){
//        mProgressDialog = new ProgressDialog(context);
//        mProgressDialog.setCancelable(false);
//        mProgressDialog.setTitle("Processing...");
//        mProgressDialog.setMessage("Please wait...");
//    }

    protected void dismissProgress() {
        if (mProgressDialog != null) {
            mProgressDialog.dismiss();
            mProgressDialog = null;
        }
    }

    public void showToast(String msg) {
        Toast.makeText(this, msg, Toast.LENGTH_LONG).show();
    }

    public void errorDialogExit(String title, String error) {
        android.app.AlertDialog.Builder builder = new android.app.AlertDialog.Builder(this);
        builder.setTitle("" + title);
        builder.setMessage("" + error);
        builder.setCancelable(false);
        builder.setNeutralButton(android.R.string.ok, new android.content.DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int arg1) {
                dialog.dismiss();
                HelpUBaseActivity.this.finish();
            }
        }).create().show();
    }

    public void showAlert(String msg) {
        final AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle(getResources().getString(R.string.app_name))
                .setMessage(msg)
                .setCancelable(false)
                .setIcon(android.R.drawable.ic_dialog_alert)
                .setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        dialogInterface.dismiss();
                    }
                }).create().show();

//        AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(this.getActivity().getBaseContext());
//        dialogBuilder.setMessage(msg);
//        dialogBuilder.setPositiveButton(android.R.string.ok, null);
//        dialogBuilder.show();
    }

    @Override
    public void onBackPressed() {
        Intent redirect = new Intent(this, MainActivity.class);
        redirect.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(redirect);
        finish();
    }

    public boolean isNetworkAvailable() {
        ConnectivityManager connectivityManager = (ConnectivityManager) this.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        return activeNetworkInfo != null && activeNetworkInfo.isConnected();
    }
}
