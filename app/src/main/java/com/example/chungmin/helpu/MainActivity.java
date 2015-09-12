package com.example.chungmin.helpu;

import android.app.ActivityManager;
import android.app.AlertDialog;
import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.readystatesoftware.viewbadger.BadgeView;

import fragments.UserInfoFragment;
import me.leolin.shortcutbadger.ShortcutBadger;


public class MainActivity extends HelpUBaseActivity implements View.OnClickListener {

    UserLocalStore userLocalStore;
    TextView tvTotalCountHire, tvTotalCountWork, tvTotalCountJobOffer, tvTotalCountJobDone;
    Button btnLogout, btnHire, btnWork;
    BadgeView badgeWork, badgeHire;

    private User mUser = null;

    @Override
    protected void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        tvTotalCountHire = (TextView) findViewById(R.id.tvTotalCountHire);
        tvTotalCountWork = (TextView) findViewById(R.id.tvTotalCountWork);
        tvTotalCountJobOffer = (TextView) findViewById(R.id.tvTotalCountJobOffer);
        tvTotalCountJobDone = (TextView) findViewById(R.id.tvTotalCountJobDone);
        btnLogout = (Button) findViewById(R.id.btnLogout);
        btnHire = (Button) findViewById(R.id.btnHire);
        btnWork = (Button) findViewById(R.id.btnWork);

        btnLogout.setOnClickListener(this);
        btnHire.setOnClickListener(this);
        btnWork.setOnClickListener(this);

        tvTotalCountHire.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Intent intent = new Intent(v.getContext(), CustomerRequestList.class);
                startActivity(intent);
                finish();
            }
        });

        tvTotalCountWork.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Intent intent = new Intent(v.getContext(), ServiceProviderList.class);
                startActivity(intent);
                finish();
            }
        });

        tvTotalCountJobDone.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Intent intent = new Intent(v.getContext(), CustomerRequestJobList.class);
                intent.putExtra("ListType", "JobDoneList");
                startActivity(intent);
            }
        });

        tvTotalCountJobOffer.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Intent intent = new Intent(v.getContext(), CustomerRequestJobList.class);
                intent.putExtra("ListType", "JobOfferList");
                startActivity(intent);
            }
        });

        if (!mIsNetworkAvailable) {
            return;
        }

        userLocalStore = new UserLocalStore(this);
        if (authenticate() == true) {

            mUser = userLocalStore.getLoggedInUser();
            if (mUser != null) {
                String userId = String.valueOf(mUser.getUserId());
                ((Globals) this.getApplication()).setUserId(mUser.getUserId());

                //restart the auto startup service if being kill before
                if (!isMyServiceRunning(AutoStartUp.class)) {
                    Intent serviceIntent = new Intent(this, AutoStartUp.class);
                    startService(serviceIntent);
                }

                if (savedInstanceState == null) {
                    final FragmentManager fm = getFragmentManager();
                    final FragmentTransaction ft = fm.beginTransaction();
                    final Fragment fragUserInfo = new UserInfoFragment().newInstance(mUser.getUserName(), null, 0, 0);
                    ft.add(R.id.llUserInfo, fragUserInfo);
                    ft.commit();

                    String url = getString(R.string.server_uri) + ((Globals) getApplication()).getUserStatsInfo();
                    UserStatsServerRequests serverRequest = new UserStatsServerRequests();
                    serverRequest.getUserStatsByUserId(mUser.getUserId(), url, new GetUserStatsCallback() {
                        @Override
                        public void done(UserStats returnedUserStats) {
                            if (returnedUserStats != null) {
                                tvTotalCountHire.setText(Integer.toString(returnedUserStats.getTotalCustomerRequest()));
                                tvTotalCountWork.setText(Integer.toString(returnedUserStats.getTotalServiceProvider()));
                                tvTotalCountJobOffer.setText(Integer.toString(returnedUserStats.getTotalJobOffer()));
                                tvTotalCountJobDone.setText(Integer.toString(returnedUserStats.getTotalJobDone()));

                                int totalWorkNotification = returnedUserStats.getTotalWorkNotification();
                                if (totalWorkNotification > 0) {
                                    badgeWork = new BadgeView(getBaseContext(), tvTotalCountJobOffer);
                                    badgeWork.setBadgeBackgroundColor(getBaseContext().getResources().getColor(R.color.DroidGreen));
                                    badgeWork.setTextColor(Color.BLACK);
                                    badgeWork.setText(Integer.toString(totalWorkNotification));
                                    badgeWork.setBadgeMargin(10, 10);
                                    badgeWork.setTextSize(10);
                                    badgeWork.show();
                                }

                                int totalHireNotification = returnedUserStats.getTotalHireNotification();
                                if (totalHireNotification > 0) {
                                    badgeHire = new BadgeView(getBaseContext(), tvTotalCountHire);
                                    badgeHire.setBadgeBackgroundColor(getBaseContext().getResources().getColor(R.color.DroidGreen));
                                    badgeHire.setTextColor(Color.BLACK);
                                    badgeHire.setText(Integer.toString(totalHireNotification));
                                    badgeHire.setBadgeMargin(10, 10);
                                    badgeHire.setTextSize(10);
                                    badgeHire.show();
                                }


                            }
                            hideMenuProgress();
                        }
                    });
                }


            }
        }

        isAllowMenuProgressBar = true;

        //reset the icon count number
        ((Globals) getApplication()).setBadgeCount(0);
        ShortcutBadger.with(getApplicationContext()).count(0);

    }

    @Override
    public void onClick(View v) {
        Intent redirect = null;
        switch(v.getId()){
            case R.id.btnLogout:
                userLocalStore.clearUserData();
                userLocalStore.setUserLoggedIn(false);
                redirect = new Intent(this, Login.class);
                break;
            case R.id.btnHire:
                redirect = new Intent(this, Hire.class);
                break;
            case R.id.btnWork:
                redirect = new Intent(this, Work.class);
                break;
        }
        startActivity(redirect);
        finish();
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

    @Override
    public void onBackPressed() {
        new AlertDialog.Builder(this)
                .setTitle(getString(R.string.strReallyExist))
                .setMessage(getString(R.string.strAreYouSureYouWantToExit))
                .setNegativeButton(android.R.string.no, null)
                .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface arg0, int arg1) {
                        finish();
                    }
                }).create().show();
    }

    private boolean isMyServiceRunning(Class<?> serviceClass) {
        ActivityManager manager = (ActivityManager) getSystemService(Context.ACTIVITY_SERVICE);
        for (ActivityManager.RunningServiceInfo service : manager.getRunningServices(Integer.MAX_VALUE)) {
            if (serviceClass.getName().equals(service.service.getClassName())) {
                return true;
            }
        }
        return false;
    }

}
