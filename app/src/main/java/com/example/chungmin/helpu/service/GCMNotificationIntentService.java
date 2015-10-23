package com.example.chungmin.helpu.service;

/**
 * Created by Chung Min on 10/13/2015.
 */

import android.app.IntentService;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.NotificationCompat;
import android.util.Log;
import android.widget.Toast;

import com.example.chungmin.helpu.activities.ChatActivity;
import com.example.chungmin.helpu.activities.MainActivity;
import com.example.chungmin.helpu.activities.ProjectMessages;
import com.example.chungmin.helpu.enumeration.ProjectStatus;
import com.example.chungmin.helpu.models.ChatMessage;
import com.example.chungmin.helpu.models.CustomerRequest;
import com.example.chungmin.helpu.models.Globals;
import com.example.chungmin.helpu.serverrequest.ChatMessageManager;
import com.example.chungmin.helpu.serverrequest.CustomerRequestManager;
import com.example.chungmin.helpu.sqlite.DBMessageController;
import com.google.android.gms.gcm.GoogleCloudMessaging;
import com.example.chungmin.helpu.R;

import org.json.JSONException;

import HelpUGenericUtilities.Config;
import HelpUGenericUtilities.WakeLocker;
import me.leolin.shortcutbadger.ShortcutBadger;

import static HelpUGenericUtilities.DateTimeUtils.parseTo;

public class GCMNotificationIntentService extends IntentService {
    public static final String TAG = "GCMNtfIntentService";
    public static final int NOTIFICATION_ID = 1;
    private NotificationManager mNotificationManager;
    NotificationCompat.Builder builder;

    public GCMNotificationIntentService() {
        super("GcmIntentService");
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        Bundle extras = intent.getExtras();

        GoogleCloudMessaging gcm = GoogleCloudMessaging.getInstance(this);
        String messageType = gcm.getMessageType(intent);

        if (!extras.isEmpty()) {
//            if (GoogleCloudMessaging.MESSAGE_TYPE_SEND_ERROR.equals(messageType)) {
//
//                sendNotification(messageType, extras);
//
//            } else if (GoogleCloudMessaging.MESSAGE_TYPE_DELETED.equals(messageType)) {
//
//                sendNotification(messageType, extras);
//
//            } else if (GoogleCloudMessaging.MESSAGE_TYPE_MESSAGE.equals(messageType)) {

//                if(notificationType.equals("Multicast")) {
//
//                    sendNotification(messageType, extras);
//
//                }else if (notificationType.equals("Message")) {
//
//                    sendNotification(messageType, extras);
//
//                }else if (notificationType.equals("ProjectNotification")) {
//
            sendNotification(messageType, extras);
//
//                }

            Log.i(TAG, "Received: " + extras.toString());
//            }
        }
        GcmBroadcastReceiver.completeWakefulIntent(intent);
    }

    private void sendNotification(String messageType, Bundle extras) {
        Log.d(TAG, "Preparing to send notification... ");

        String title = "";
        String msg = "";
        String ticker = "";
        String notificationType = extras.getString(Config.NOTIFICATION_TYPE);

        mNotificationManager = (NotificationManager) this
                .getSystemService(Context.NOTIFICATION_SERVICE);

        Intent notificationIntent = new Intent(this, MainActivity.class);
        Bundle b = new Bundle();

        if (GoogleCloudMessaging.MESSAGE_TYPE_SEND_ERROR.equals(messageType)) {

            title = "HelpU Notification";
            msg = "Send error: " + extras.toString();
            ticker = "Message Send Error";
            notificationIntent = new Intent(this, MainActivity.class);

        } else if (GoogleCloudMessaging.MESSAGE_TYPE_DELETED.equals(messageType)) {

            title = "HelpU Notification";
            msg = "Deleted messages on server: " + extras.toString();
            ticker = "Message Deleted";
            notificationIntent = new Intent(this, MainActivity.class);

        } else if (GoogleCloudMessaging.MESSAGE_TYPE_MESSAGE.equals(messageType)) {
            if (notificationType.equals("Message")) {
                String sender = extras.getString(Config.FROM_NAME);
                String receiver = extras.getString(Config.TO_NAME);

                String result = extras.getString(Config.JSON_INFO);
                ChatMessage chatMessage = null;
                try {
                    chatMessage = ChatMessageManager.buildRecord(result);
                } catch (JSONException e) {
                    e.printStackTrace();
                }

                title = "HelpU Chat Message";
                msg = "Sender : " + sender
                        + "\nReceiver : " + receiver
                        + "\nMessage : " + chatMessage.getMessage();
                ticker = chatMessage.getMessage();
                notificationIntent = new Intent(this, ChatActivity.class);

                b.putInt("userIdFrom", chatMessage.getUserIdTo());
                b.putInt("userIdTo", chatMessage.getUserIdFrom());
                b.putString("userNameFrom", receiver);
                b.putString("userNameTo", sender);

                //insert into sqlite db and display the new message
                DBMessageController db = new DBMessageController(this);
                db.insert(chatMessage);
                updateChatActivity(this, chatMessage);

            } else if (notificationType.equals("Multicast")) {

                title = "HelpU Announcement";
                msg = "Sender : Admin" + "\nMessage : " + extras.get(Config.MESSAGE_KEY);
                ticker = "HelpU Announcement";
                notificationIntent = new Intent(this, MainActivity.class);

            } else if (notificationType.equals("ProjectNotification")) {
                ProjectStatus mProjectStatus = null;
                int mBadgeCount;

                String result = extras.getString(Config.JSON_INFO);
                CustomerRequest customerRequest = null;
                try {
                    customerRequest = CustomerRequestManager.buildRecord(result);
                } catch (JSONException e) {
                    e.printStackTrace();
                }

                mProjectStatus = customerRequest.getProjectStatus();
                switch (mProjectStatus) {
                    case SelectedNotification:
                        ticker = "Chance to earn money...";
                        title = "HelpU : Job Offer";
                        msg = "Customer need your help!!";
                        break;
                    case ConfirmRequestNotification:
                        ticker = "Confirm Request...";
                        title = "HelpU : Confirm Request";
                        msg = "Serv. provider confirm you request and will have an appointment with you.";
                        break;
                    case QuotationNotification:
                        ticker = "Create Quotation...";
                        title = "HelpU : Create Quotation";
                        msg = "Serv provider will create quotation after further discussion or on site checking with you.";
                        break;
                    case ConfirmQuotationNotification:
                        String strQuotation = Double.toString(customerRequest.getQuotation());
                        ticker = "Quotation [ RM " + strQuotation + " ]...";
                        title = "HelpU : Quotation [ RM " + strQuotation + " ] ";
                        msg = "Serv. provider quote with RM " + strQuotation + ".";
                        break;
                    case DealNotification:
                        ticker = "Win Award...";
                        title = "HelpU : Congratulation";
                        msg = "You have been successful pick as serv. provider by customer!!";
                        break;
                    case PlanStartDateNotification:
                        ticker = "Plan Project Date...";
                        title = "HelpU : Plan Project Start Date";
                        msg = "Serv. provider noted the deal and start to plan the start date of your project!!";
                        break;
                    case ServiceStartNotification:
                        ticker = "Service Start...";
                        title = "HelpU : Service Start";
                        msg = "Serv. provider announce the project start.";
                        break;
                    case ServiceDoneNotification:
                        ticker = "Service Done...";
                        title = "HelpU : Service Done";
                        msg = "Customer have approve the project done.";
                        break;
                    case CustomerRatingNotification:
                        ticker = "Customer Rating...";
                        title = "HelpU : Customer Rating";
                        msg = "Customer give you ( " + customerRequest.getServiceProviderRatingValue() + " ) out of 5.0";
                        break;
                    case ServiceProviderRatingNotification:
                        ticker = "Service Provider Rating...";
                        title = "HelpU : Service Provider Rating";
                        msg = "Serv. provider give you ( " + customerRequest.getCustomerRatingValue() + " ) out of 5.0";
                        break;
                    default:
                        Toast.makeText(this, R.string.strUnknownAction, Toast.LENGTH_LONG).show();
                        break;
                }
                notificationIntent = new Intent(this, ProjectMessages.class);


                b.putInt("projectStatusId", customerRequest.getProjectStatusId());
                b.putInt("customerRequestId", customerRequest.getCustomerRequestId());

                mBadgeCount = ((Globals) getApplication()).getBadgeCount();
                ShortcutBadger.with(getApplicationContext()).count(mBadgeCount);

            }
        }

        notificationIntent.putExtras(b);
        notificationIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        PendingIntent contentIntent = PendingIntent.getActivity(this, 0,
                notificationIntent, PendingIntent.FLAG_CANCEL_CURRENT);

        Uri alarmSound = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
        NotificationCompat.Builder mBuilder = new NotificationCompat.Builder(
                this)
                .setLights(Color.BLUE, 3000, 3000)
                .setVibrate(new long[]{1000, 1000, 1000, 1000, 1000})
                .setSound(alarmSound)
                .setSmallIcon(R.drawable.ic_launcher)
                .setTicker(ticker)
                .setWhen(System.currentTimeMillis())
                .setAutoCancel(true)
                .setContentTitle(title)
                .setStyle(new NotificationCompat.BigTextStyle().bigText(msg))
                .setContentText(msg);

        mBuilder.setContentIntent(contentIntent);
        mNotificationManager.notify(NOTIFICATION_ID, mBuilder.build());
        Log.d(TAG, "Notification sent successfully.");

        // Waking up mobile if it is sleeping
        WakeLocker.acquire(this);
        // Releasing wake lock
        WakeLocker.release();
    }

    // This function will create an intent. This intent must take as parameter the "unique_name" that you registered your activity with
    static void updateChatActivity(Context context, ChatMessage chatMessage) {

        Intent intent = new Intent("ChatActivityBroadcastReceiver");

        //put whatever data you want to send, if any
        intent.putExtra("message", chatMessage.getMessage());
        intent.putExtra("senderId", chatMessage.getUserIdFrom());
        intent.putExtra("receiverId", chatMessage.getUserIdTo());
        intent.putExtra("createdDate", chatMessage.getCreatedDate());

        //send broadcast
        context.sendBroadcast(intent);
    }
}
