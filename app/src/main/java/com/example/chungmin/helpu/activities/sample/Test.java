package com.example.chungmin.helpu.activities.sample;

import android.app.Activity;
import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.graphics.Color;
import android.os.Bundle;
import android.widget.Button;
import android.widget.ProgressBar;

import com.example.chungmin.helpu.R;
import com.readystatesoftware.viewbadger.BadgeView;

import fragments.UserInfoFragment;

public class Test extends Activity {
//    private ProgressBar bar;
//    private AlarmManagerBroadcastReceiver alarm;
//    UserLocalStore userLocalStore;

    Button btnTest;
    BadgeView badge;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test);

//        userLocalStore = new UserLocalStore(this);
//        User user = userLocalStore.getLoggedInUser();
//
//        if (savedInstanceState == null) {
//            FragmentManager fm = getFragmentManager();
//            FragmentTransaction ft = fm.beginTransaction();
//            Fragment frag = new UserInfoFragment().newInstance(user.getUserName(), null, 0, 0);
//            ft.add(R.id.llDisplaySection, frag);
//            ft.commit();
//        }

        btnTest = (Button) findViewById(R.id.btnTest);
        badge = new BadgeView(this, btnTest);
        badge.setText("New!");
        badge.setTextColor(Color.BLUE);
        badge.setBadgeBackgroundColor(Color.YELLOW);
        badge.setTextSize(12);
        badge.show();
    }

    @Override
    protected void onStart() {
        super.onStart();
    }

}