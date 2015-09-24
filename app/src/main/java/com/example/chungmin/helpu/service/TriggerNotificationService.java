package com.example.chungmin.helpu.service;

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
import android.widget.Toast;

import com.example.chungmin.helpu.models.Globals;
import com.example.chungmin.helpu.R;
import com.example.chungmin.helpu.activities.ProjectMessages;
import com.example.chungmin.helpu.enumeration.ProjectStatus;
import com.example.chungmin.helpu.models.CustomerRequest;

import me.leolin.shortcutbadger.ShortcutBadger;

/**
 * Created by Chung Min on 7/19/2015.
 * 09-09-2015 cm.choong : add customerRatingValue, serviceProviderRatingValue, alreadyReadNotification;
 */

public class TriggerNotificationService extends Service {
    private static final String TAG = "MyService";
    private ProjectStatus mProjectStatus = null;
    private int mCustomerRequestId = 0;
    private double mQuotation = 0.0;
    private double mCustomerRatingValue = 0.0;
    private double mServiceProviderRatingValue = 0.0;
    private CustomerRequest mCustomerRequest = null;
    private int mBadgeCount;

    public TriggerNotificationService() {
    }

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onStart(Intent intent, int startId) {

        if (intent == null) {
            return;
        }

        Bundle extras = intent.getExtras();
        if (extras != null) {
            mCustomerRequest = extras.getParcelable("customerRequest");
            mCustomerRequestId = mCustomerRequest.getCustomerRequestId();
            mProjectStatus = mCustomerRequest.getProjectStatus();
            mQuotation = mCustomerRequest.getQuotation();
            mServiceProviderRatingValue = mCustomerRequest.getServiceProviderRatingValue();
            mCustomerRatingValue = mCustomerRequest.getCustomerRatingValue();
        }

    }

    @Override
    public void onCreate() {
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        super.onStartCommand(intent, flags, startId);
        String ticker = "";
        String contentTitle = "";
        String contentText = "";

        if (mProjectStatus == null) {
            return 0;
        }

        switch (mProjectStatus) {
            case SelectedNotification:
                ticker = "Chance to earn money...";
                contentTitle = "HelpU : Job Offer";
                contentText = "Customer need your help!!";
                break;
            case ConfirmRequestNotification:
                ticker = "Confirm Request...";
                contentTitle = "HelpU : Confirm Request";
                contentText = "Serv. provider confirm you request and will have an appointment with you.";
                break;
            case QuotationNotification:
                ticker = "Create Quotation...";
                contentTitle = "HelpU : Create Quotation";
                contentText = "Serv provider will create quotation after further discussion or on site checking with you.";
                break;
            case ConfirmQuotationNotification:
                String strQuotation = Double.toString(mQuotation);
                ticker = "Quotation [ RM " + strQuotation + " ]...";
                contentTitle = "HelpU : Quotation [ RM " + strQuotation + " ] ";
                contentText = "Serv. provider quote with RM " + strQuotation + ".";
                break;
            case DealNotification:
                ticker = "Win Award...";
                contentTitle = "HelpU : Congratulation";
                contentText = "You have been successful pick as serv. provider by customer!!";
                break;
            case PlanStartDateNotification:
                ticker = "Plan Project Date...";
                contentTitle = "HelpU : Plan Project Start Date";
                contentText = "Serv. provider noted the deal and start to plan the start date of your project!!";
                break;
            case ServiceStartNotification:
                ticker = "Service Start...";
                contentTitle = "HelpU : Service Start";
                contentText = "Serv. provider announce the project start.";
                break;
            case ServiceDoneNotification:
                ticker = "Service Done...";
                contentTitle = "HelpU : Service Done";
                contentText = "Customer have approve the project done.";
                break;
            case CustomerRatingNotification:
                ticker = "Customer Rating...";
                contentTitle = "HelpU : Customer Rating";
                contentText = "Customer give you ( " + mServiceProviderRatingValue + " ) out of 5.0";
                break;
            case ServiceProviderRatingNotification:
                ticker = "Service Provider Rating...";
                contentTitle = "HelpU : Service Provider Rating";
                contentText = "Serv. provider give you ( " + mCustomerRatingValue + " ) out of 5.0";
                break;
            default:
                Toast.makeText(this, R.string.strUnknownAction, Toast.LENGTH_LONG).show();
                break;
        }

        createNotification(ticker, contentTitle, contentText, mProjectStatus);
        return START_STICKY;
    }

    @Override
    public void onDestroy() {
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
        if (projectStatus == null) {
            return;
        }

        b.putInt("projectStatusId", projectStatus.getId());
        b.putInt("customerRequestId", mCustomerRequestId);
        notificationIntent.putExtras(b);
        notificationIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
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

        nm.notify(mCustomerRequestId, n);

        mBadgeCount = ((Globals) getApplication()).getBadgeCount();
        ShortcutBadger.with(getApplicationContext()).count(mBadgeCount);

    }
}
