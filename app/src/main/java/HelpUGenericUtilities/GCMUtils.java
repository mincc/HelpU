package HelpUGenericUtilities;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.os.AsyncTask;
import android.text.TextUtils;
import android.util.Log;
import android.widget.Toast;

import com.google.android.gms.gcm.GoogleCloudMessaging;

import java.io.IOException;

/**
 * Created by Chung Min on 10/15/2015.
 */
public class GCMUtils {
    private static GoogleCloudMessaging mGcm;
    private static String mRegId;

    private static final String REG_ID = "regId";
    private static final String APP_VERSION = "appVersion";
    private static final String TAG = "GCMUtils";

    public static void Init(Context context) {
        if (TextUtils.isEmpty(mRegId)) {
            mRegId = registerGCM(context);
            Log.d("RegisterActivity", "GCM RegId: " + mRegId);
        } else {
//            Toast.makeText(context,
//                    "Already Registered with GCM Server!",
//                    Toast.LENGTH_LONG).show();
        }
    }

    private static String registerGCM(Context context) {
        mGcm = GoogleCloudMessaging.getInstance(context);
        mRegId = getRegistrationId(context);
        if (TextUtils.isEmpty(mRegId)) {
            registerInBackground(context);
            Log.d("RegisterActivity",
                    "registerGCM - successfully registered with GCM server - regId: "
                            + mRegId);
        } else {
//            Toast.makeText(context,
//                    "RegId already available. RegId: " + mRegId,
//                    Toast.LENGTH_LONG).show();
        }
        return mRegId;
    }

    private static void registerInBackground(final Context context) {
        new AsyncTask<Void, Void, String>() {
            @Override
            protected String doInBackground(Void... params) {
                String msg = "";
                try {
                    if (mGcm == null) {
                        mGcm = GoogleCloudMessaging.getInstance(context);
                    }
                    mRegId = mGcm.register(Config.GOOGLE_PROJECT_ID);
                    Log.d("RegisterActivity", "registerInBackground - regId: "
                            + mRegId);
                    msg = "Device registered, registration ID=" + mRegId;

                    storeRegistrationId(context, mRegId);
                } catch (IOException ex) {
                    msg = "Error :" + ex.getMessage();
                    Log.d("RegisterActivity", "Error: " + msg);
                }
                Log.d("RegisterActivity", "AsyncTask completed: " + msg);
                return msg;
            }

            @Override
            protected void onPostExecute(String msg) {
//                Toast.makeText(context,
//                        "Registered with GCM Server." + msg, Toast.LENGTH_LONG)
//                        .show();
            }
        }.execute(null, null, null);
    }

    public static String getRegistrationId(Context context) {
        final SharedPreferences prefs = context.getSharedPreferences(TAG, Context.MODE_PRIVATE);
        String registrationId = prefs.getString(REG_ID, "");
        if (registrationId.isEmpty()) {
            Log.i(TAG, "Registration not found.");
            return "";
        }
        int registeredVersion = prefs.getInt(APP_VERSION, Integer.MIN_VALUE);
        int currentVersion = getAppVersion(context);
        if (registeredVersion != currentVersion) {
            Log.i(TAG, "App version changed.");
            return "";
        }
        return registrationId;
    }

    private static void storeRegistrationId(Context context, String regId) {
        final SharedPreferences prefs = context.getSharedPreferences(TAG, Context.MODE_PRIVATE);
        int appVersion = getAppVersion(context);
        Log.i(TAG, "Saving regId on app version " + appVersion);
        SharedPreferences.Editor editor = prefs.edit();
        editor.putString(REG_ID, regId);
        editor.putInt(APP_VERSION, appVersion);
        editor.commit();
    }

    private static int getAppVersion(Context context) {
        try {
            PackageInfo packageInfo = context.getPackageManager()
                    .getPackageInfo(context.getPackageName(), 0);
            return packageInfo.versionCode;
        } catch (PackageManager.NameNotFoundException e) {
            Log.d("RegisterActivity",
                    "I never expected this! Going down, going down!" + e);
            throw new RuntimeException(e);
        }
    }
}
