package com.example.chungmin.helpu.activities;

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
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.example.chungmin.helpu.callback.Callback;
import com.example.chungmin.helpu.serverrequest.UserManager;
import com.example.chungmin.helpu.service.AutoStartUp;
import com.example.chungmin.helpu.models.Globals;
import com.example.chungmin.helpu.R;
import com.example.chungmin.helpu.models.User;
import com.example.chungmin.helpu.models.UserLocalStore;
import com.example.chungmin.helpu.models.UserStats;
import com.example.chungmin.helpu.serverrequest.UserStatsManager;
import com.readystatesoftware.viewbadger.BadgeView;

import HelpUGenericUtilities.GCMUtils;
import HelpUGenericUtilities.SystemUtils;
import fragments.UserInfoFragment;
import me.leolin.shortcutbadger.ShortcutBadger;


public class MainActivity extends HelpUBaseActivity implements View.OnClickListener {

    private UserLocalStore userLocalStore;
    private TextView tvTotalCountHire, tvTotalCountWork, tvTotalCountJobOffer, tvTotalCountJobDone;
    private Button btnLogout, btnHire, btnWork;
    private BadgeView badgeWork, badgeHire;
    private User mUser = null;
    private SystemUtils su;

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
                ((Globals) getApplication()).setIsAdmin(mUser.getIsAdmin());
                ((Globals) this.getApplication()).setUserId(mUser.getUserId());

                //Google gcm
                GCMUtils.Init(this);

                //restart the auto startup service if being kill before
                su = new SystemUtils(this);
                if (!su.isMyServiceRunning(AutoStartUp.class)) {
                    Intent serviceIntent = new Intent(this, AutoStartUp.class);
                    startService(serviceIntent);
                }

                if (savedInstanceState == null) {
                    final FragmentManager fm = getFragmentManager();
                    final FragmentTransaction ft = fm.beginTransaction();
                    final Fragment fragUserInfo = new UserInfoFragment().newInstance(mUser.getUserName(), null, 0, 0);
                    ft.add(R.id.llUserInfo, fragUserInfo);
                    ft.commit();

                    UserStatsManager.getByUserId(mUser.getUserId(), new Callback.GetUserStatsCallback() {
                        @Override
                        public void complete(UserStats returnedUserStats) {
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

                        @Override
                        public void failure(String msg) {
                            msg = ((Globals) getApplication()).translateErrorType(msg);
                            showAlert(msg);
                        }
                    });
                }

                checkGcmRegId();
            }
        }

        isAllowMenuProgressBar = true;

//        ((Globals)getApplication()).testUncaughtExceptionhandler();

        //reset the icon count number
        ((Globals) getApplication()).setBadgeCount(0);
        ShortcutBadger.with(getApplicationContext()).count(0);

    }

    private void checkGcmRegId() {
        String newGsmRegId = GCMUtils.getRegistrationId(this);
        String dbGcmRegId = mUser.getGcmRegId();
        if (!newGsmRegId.equals(dbGcmRegId) && !TextUtils.isEmpty(newGsmRegId)) {
            UserManager serverRequest = new UserManager(this);
            mUser.setGcmRegId(newGsmRegId);
            serverRequest.update(mUser, new Callback.GetUserCallback() {
                @Override
                public void complete(User returnedUser) {

                }

                @Override
                public void failure(String msg) {
                    msg = ((Globals) getApplication()).translateErrorType(msg);
                    showAlert(msg);
                }
            });
        }
    }

    @Override
    public void onClick(View v) {
        Intent redirect = null;
        switch(v.getId()){
            case R.id.btnLogout:
                userLocalStore.clearUserData();
                userLocalStore.setUserLoggedIn(false);
                logoutUpdate();
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

    private void logoutUpdate() {
        UserManager serverRequest = new UserManager(this);
        serverRequest.logout(mUser.getUserId(), new Callback.GetUserCallback() {
            @Override
            public void complete(User returnedUser) {
            }

            @Override
            public void failure(String msg) {
                msg = ((Globals) getApplication()).translateErrorType(msg);
                showAlert(msg);
            }
        });
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

}
