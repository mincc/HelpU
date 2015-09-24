package com.example.chungmin.helpu.service;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;

import com.example.chungmin.helpu.models.Globals;
import com.example.chungmin.helpu.models.UserLocalStore;
import com.example.chungmin.helpu.models.User;

public class AutoStartUp extends Service {
    private AlarmManagerBroadcastReceiver alarm;
    UserLocalStore userLocalStore;

    public AutoStartUp() {
    }

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onCreate() {
        super.onCreate();

//        Debug.waitForDebugger();
//        Toast.makeText(this, "Service Started", Toast.LENGTH_LONG).show();

        //set the alarm and check status to trigger notification
        try {
            userLocalStore = new UserLocalStore(this);
            User user = userLocalStore.getLoggedInUser();
            ((Globals) this.getApplication()).setUserId(user.getUserId());
            alarm = new AlarmManagerBroadcastReceiver();
            alarm.SetAlarm(this);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
