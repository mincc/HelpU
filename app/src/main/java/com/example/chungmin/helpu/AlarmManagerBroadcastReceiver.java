package com.example.chungmin.helpu;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;


public class AlarmManagerBroadcastReceiver extends BroadcastReceiver {
    Boolean mIsTrigger = false;
    Boolean mIsServerAlreadyResponse = true;
    Boolean mIsAlreadyFinishFullCycle = true;
    CustomerRequest mCustomerRequest = null;

    public AlarmManagerBroadcastReceiver() {

    }

    @Override
    public void onReceive(final Context context, Intent intent) {
        mIsTrigger = ((Globals) context.getApplicationContext()).getIsJobOfferTriggle();
        if (!mIsTrigger) {
            //only execute when the trigger is false
            if (mIsServerAlreadyResponse) {
                if (mIsTrigger) {
                    return;
                }
                checkNeedTriggerNotification(context);
            }
        } else {
            if(mIsAlreadyFinishFullCycle) {
                mIsAlreadyFinishFullCycle = false;
                //Retrieve related customer request id and save as global object for view the notification
                String url = context.getString(R.string.server_uri) + ((Globals) context.getApplicationContext()).getServiceProviderJobOffer();
                ServiceProviderServerRequests serverRequest = new ServiceProviderServerRequests(context);
                int userId = ((Globals) context.getApplicationContext()).getUserId();
                final ProjectStatus[] projectStatus = {null};
                serverRequest.getServiceProviderJobOffer(userId, url, new GetCustomerRequestCallback() {
                    @Override
                    public void done(CustomerRequest returnedCustomerRequestServiceProvider) {
                        if(returnedCustomerRequestServiceProvider != null) {
                            ((Globals) context.getApplicationContext()).setCustomerRequest(returnedCustomerRequestServiceProvider);
                            projectStatus[0] = returnedCustomerRequestServiceProvider.getProjectStatus();
                            if (projectStatus[0] == ProjectStatus.CandidateNotification) {
                                Intent serviceIntent = new Intent(context, TriggerNotificationService.class);
                                serviceIntent.putExtra("projectStatusId", returnedCustomerRequestServiceProvider.getProjectStatusId());
                                context.startService(serviceIntent);
                            }
                        }

                        String url = context.getString(R.string.server_uri) + ((Globals) context.getApplicationContext()).getServiceProviderWinAward();
                        ServiceProviderServerRequests serverRequest = new ServiceProviderServerRequests(context);
                        int userId = ((Globals) context.getApplicationContext()).getUserId();
                        serverRequest.getServiceProviderJobOffer(userId, url, new GetCustomerRequestCallback() {
                            @Override
                            public void done(CustomerRequest returnedCustomerRequestServiceProvider) {
                                if(returnedCustomerRequestServiceProvider != null) {
                                    ((Globals) context.getApplicationContext()).setCustomerRequest(returnedCustomerRequestServiceProvider);
                                    projectStatus[0] = returnedCustomerRequestServiceProvider.getProjectStatus();
                                    if (projectStatus[0] == ProjectStatus.WinAwardNotification) {
                                        Intent serviceIntent = new Intent(context, TriggerNotificationService.class);
                                        serviceIntent.putExtra("projectStatusId", returnedCustomerRequestServiceProvider.getProjectStatusId());
                                        context.startService(serviceIntent);
                                    }
                                }
                                mIsAlreadyFinishFullCycle = true;
                                mIsTrigger = false;
                                ((Globals) context.getApplicationContext()).setIsJobOfferTriggle(false);
                                mIsServerAlreadyResponse = false;

                            }
                        });
                    }
                });
            }
        }
    }

    private void checkNeedTriggerNotification(final Context context) {
        mIsServerAlreadyResponse = false;
        String url = context.getString(R.string.server_uri) + ((Globals)context.getApplicationContext()).getServiceProviderIsNotificationTrigger();
        ServiceProviderServerRequests serverRequest = new ServiceProviderServerRequests(context);
        int userId = ((Globals)context.getApplicationContext()).getUserId();
        serverRequest.getServiceProviderIsNotificationTrigger(userId, url, new GetBooleanCallback() {
            @Override
            public void done(Boolean isTrigger) {
                if (isTrigger == null) {
                    return;
                }

                ((Globals) context.getApplicationContext()).setIsJobOfferTriggle(isTrigger);
                mIsServerAlreadyResponse = true;
                //
                return;
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


