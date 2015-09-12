package com.example.chungmin.helpu;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.widget.Toast;

import java.util.List;

/**
 * Created by Chung Min on 7/19/2015.
 * 09 Sep 2015 cm.choong : add customerRatingValue, serviceProviderRatingValue, alreadyReadNotification;
 */

public class AlarmManagerBroadcastReceiver extends BroadcastReceiver {

    public AlarmManagerBroadcastReceiver() {
    }

    @Override
    public void onReceive(final Context context, Intent intent) {
        TriggerNotification(context);
    }

    private void TriggerNotification(final Context context) {
        String url = context.getString(R.string.server_uri) + ((Globals) context.getApplicationContext()).getCustomerRequestNotificationTrigger();
        CustomerRequestServerRequests serverRequest = new CustomerRequestServerRequests();
        int userId = ((Globals)context.getApplicationContext()).getUserId();
        serverRequest.getCustomerRequestNotificationTrigger(userId, url, new GetCustomerRequestListCallback() {
            @Override
            public void Complete(List<CustomerRequest> customerRequestList) {
                for (int i = 0; i < customerRequestList.size(); i++) {
                    ProjectStatus projectStatus = customerRequestList.get(i).getProjectStatus();
                    Intent serviceIntent = new Intent(context, TriggerNotificationService.class);
                    switch (projectStatus) {
                        case SelectedNotification:
                        case ConfirmRequestNotification:
                        case QuotationNotification:
                        case DealNotification:
                        case PlanStartDateNotification:
                        case ServiceStartNotification:
                        case ServiceDoneNotification:
                        case CustomerRatingNotification:
                        case ServiceProviderRatingNotification:
                        case ConfirmQuotationNotification:
                            serviceIntent.putExtra("customerRequest", customerRequestList.get(i));
                            break;
                        default:
                            Toast.makeText(context, R.string.strUnknownAction, Toast.LENGTH_LONG).show();
                            break;
                    }
                    context.startService(serviceIntent);
                }
            }

            @Override
            public void Failure(String msg) {

            }
        });

    }

    public void SetAlarm(Context context) {
        AlarmManager am = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
        Intent intent = new Intent(context, AlarmManagerBroadcastReceiver.class);
        PendingIntent pi = PendingIntent.getBroadcast(context, 0, intent, 0);
        //After after 20 seconds
        am.setRepeating(AlarmManager.RTC_WAKEUP, System.currentTimeMillis(), 1000 * 20, pi);
    }

    public void CancelAlarm(Context context) {
        Intent intent = new Intent(context, AlarmManagerBroadcastReceiver.class);
        PendingIntent sender = PendingIntent.getBroadcast(context, 0, intent, 0);
        AlarmManager alarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
        alarmManager.cancel(sender);
    }
}


