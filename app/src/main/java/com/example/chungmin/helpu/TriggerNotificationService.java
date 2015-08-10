package com.example.chungmin.helpu;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.BitmapFactory;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Bundle;
import android.os.IBinder;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

public class TriggerNotificationService extends Service {
    private static final String TAG = "MyService";
    private ProjectStatus mProjectStatus = null;
    public TriggerNotificationService() {
    }

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onCreate() {
        Toast.makeText(this, "My Service Created", Toast.LENGTH_LONG).show();
        Log.d(TAG, "onCreate");

    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        super.onStartCommand(intent, flags, startId);
        String ticker = "";
        String contentTitle = "";
        String contentText = "";
        if (mProjectStatus == ProjectStatus.CandidateNotification) {
            ticker = "Chance to earn money...";
            contentTitle = "HelpU : Job Offer";
            contentText = "Customer need your help!!";
        }else if (mProjectStatus == ProjectStatus.WinAwardNotification) {
            ticker = "Win Award...";
            contentTitle = "HelpU : Congratulation";
            contentText = "You have been successful pick as service provider by customer!!";
        }
        createNotification(ticker, contentTitle, contentText, mProjectStatus);
        return START_STICKY;
    }

    @Override
    public void onDestroy() {
        Toast.makeText(this, "My Service Stopped", Toast.LENGTH_LONG).show();
        Log.d(TAG, "onDestroy");

    }

    @Override
    public void onStart(Intent intent, int startId) {

        Bundle extras = intent.getExtras();
        if(extras == null)
            Log.d("Service","null");
        else
        {
            Log.d("Service","not null");
            mProjectStatus = ProjectStatus.values()[extras.getInt("projectStatusId")] ;
        }

        Toast.makeText(this, "My Service Started", Toast.LENGTH_LONG).show();
        Log.d(TAG, "onStart");
    }

    public void createNotification(String ticker, String contentTitle, String  contentText, ProjectStatus projectStatus) {
//        // Prepare intent which is triggered if the
//        // notification is selected
//        Intent intent = new Intent(this, NotificationJobOffer.class);
//        PendingIntent pIntent = PendingIntent.getActivity(this, 0, intent, 0);
//
//        // Build notification
//        // Actions are just fake
//        Notification noti = new Notification.Builder(this)
//                .setContentTitle("New mail from " + "test@gmail.com")
//                .setContentText("Subject").setSmallIcon(R.drawable.angrybird)
//                .setContentIntent(pIntent)
//                .addAction(R.drawable.facebook, "Call", pIntent)
//                .addAction(R.drawable.google, "More", pIntent)
//                .addAction(R.drawable.line, "And more", pIntent).build();
//        NotificationManager notificationManager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
//        // hide the notification after its selected
//        noti.flags |= Notification.FLAG_AUTO_CANCEL;
//
//        notificationManager.notify(0, noti);

        Intent notificationIntent = new Intent(this, ProjectMessages.class);
        Bundle b = new Bundle();
        b.putInt("projectStatusId", projectStatus.getId());
        notificationIntent.putExtras(b);
        PendingIntent contentIntent = PendingIntent.getActivity(this,
                0, notificationIntent,
                PendingIntent.FLAG_CANCEL_CURRENT);

        NotificationManager nm = (NotificationManager) this
                .getSystemService(Context.NOTIFICATION_SERVICE);

        Resources res = this.getResources();
        Notification.Builder builder = new Notification.Builder(this);

        Uri alarmSound = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
        builder.setContentIntent(contentIntent)
                .setSound(alarmSound)
                .setSmallIcon(R.drawable.ic_launcher)
                .setLargeIcon(BitmapFactory.decodeResource(res, R.drawable.ic_launcher))
                .setTicker(ticker)
                .setWhen(System.currentTimeMillis())
                .setAutoCancel(true)
                .setContentTitle(contentTitle)
                .setContentText(contentText);
        Notification n = builder.getNotification();

        nm.notify(0, n);
    }
}
