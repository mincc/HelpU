package com.example.chungmin.helpu.models;

import android.app.Application;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Handler;
import android.os.Looper;

import com.example.chungmin.helpu.R;
import com.example.chungmin.helpu.activities.SendLog;

import java.io.IOException;

/**
 * Created by Chung Min on 7/28/2015.
 */
public class Globals extends Application {
    private CustomerRequest customerRequest;
    private ServiceProvider serviceProvider;
    private int userId;
    private int isAdmin;
    private int mBadgeCount;
    private static Context context;
    private Thread.UncaughtExceptionHandler defaultUEH;

    public Globals() {
        defaultUEH = Thread.getDefaultUncaughtExceptionHandler();
    }

    public void onCreate() {
        // this method fires once as well as constructor
        // but also application has context here

        super.onCreate();
        Globals.context = getApplicationContext();

        //clear the log
        String cmd = "logcat -c";
        try {
            Process process = Runtime.getRuntime().exec(cmd);
        } catch (IOException e) {
            e.printStackTrace();
        }

        // Setup handler for uncaught exceptions.
        Thread.setDefaultUncaughtExceptionHandler(new Thread.UncaughtExceptionHandler() {
            @Override
            public void uncaughtException(Thread thread, Throwable e) {
                handleUncaughtException(thread, e);
            }
        });

        firstInstall();
    }

    public void handleUncaughtException(final Thread thread, final Throwable e) {
        e.printStackTrace(); // not all Android versions will print the stack trace automatically

        if (isUIThread()) {
            invokeLogActivity(thread, e);

        } else {  //handle non UI thread throw uncaught exception

            new Handler(Looper.getMainLooper()).post(new Runnable() {
                @Override
                public void run() {
                    invokeLogActivity(thread, e);
                }
            });
        }

    }

    private void firstInstall() {
        //Will call every time reinstall

        String SP_NAME = "firstStart";

        SharedPreferences settings = getSharedPreferences(SP_NAME, 0);
        boolean firstStart = settings.getBoolean("firstStart", true);

        if (firstStart) {

            SharedPreferences.Editor editor = settings.edit();
            editor.putBoolean("firstStart", false);
            editor.commit();
        }
    }

    //Dont delete bcos need to test in future
    public void testUncaughtExceptionhandler() {
        Thread testThread = new Thread() {
            public void run() {
                throw new RuntimeException("Expected!");
            }
        };

        testThread.start();
        try {
            testThread.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        //make sure log file is send
    }

    private void invokeLogActivity(final Thread thread, final Throwable e) {
        Intent intent = new Intent(getApplicationContext(), SendLog.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);

//        // re-throw critical exception further to the os (important)
//        sleep(20000);
//        defaultUEH.uncaughtException(thread, e);
    }

    public boolean isUIThread() {
        return Looper.getMainLooper().getThread() == Thread.currentThread();
    }

    public static Context getAppContext() {
        return Globals.context;
    }

    public CustomerRequest getCustomerRequest() {
        return customerRequest;
    }
    public void setCustomerRequest(CustomerRequest customerRequest) {
        this.customerRequest = customerRequest;
    }

    public int getUserId() {
        return userId;
    }
    public void setUserId(int userId) {
        this.userId = userId;
    }

    public int getIsAdmin() {
        return isAdmin;
    }

    public void setIsAdmin(int isAdmin) {
        this.isAdmin = isAdmin;
    }

    public int getBadgeCount() {
        if (mBadgeCount == 0) {
            //0 will remove the icon number
            mBadgeCount += 1;
        } else {
            mBadgeCount++;
        }
        return mBadgeCount;
    }
    public void setBadgeCount(int mBadgeCount) {
        this.mBadgeCount = mBadgeCount;
    }

    public String translateErrorType(String errorMessage) {
        String msg = "";

        switch (errorMessage) {
            case "Connection Timeout":
                msg = getString(R.string.strConnTimeout);
                break;
            case "Incorrect User Details":
                msg = getString(R.string.strIncorrectUserDetails);
                break;
            case "Invalid Response":
                msg = getString(R.string.strInvalidResponse);
                break;
            case "No Network Connection":
                msg = getString(R.string.strNoNetworkConn);
                break;
            case "No Response From Server":
                msg = getString(R.string.strNoResponseFromServer);
                break;
            case "Email Address Not Exist":
                msg = getString(R.string.strEmailNotExist);
                break;
            default:
                msg = getString(R.string.strUnknownAction) + ":" + errorMessage;
                break;
        }

        return msg;
    }

    //Url List for easy management

}