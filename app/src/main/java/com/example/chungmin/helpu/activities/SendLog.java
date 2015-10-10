package com.example.chungmin.helpu.activities;

import android.app.Activity;
import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Environment;
import android.os.Bundle;
import android.support.v7.appcompat.BuildConfig;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.TextView;

import com.example.chungmin.helpu.R;
import com.example.chungmin.helpu.task.Task;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.text.SimpleDateFormat;
import java.util.Date;

import static android.os.SystemClock.sleep;

//http://stackoverflow.com/questions/19897628/need-to-handle-uncaught-exception-and-send-log-file
public class SendLog extends Activity implements View.OnClickListener {

    private Button btnReport, btnRestart;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_LEFT_ICON);
        setFinishOnTouchOutside(false); // prevent users from dismissing the dialog by tapping outside
        setContentView(R.layout.activity_send_log);
        setFeatureDrawableResource(Window.FEATURE_LEFT_ICON, android.R.drawable.ic_dialog_alert);
        setTitle("Error");

        btnReport = (Button) findViewById(R.id.btnReport);
        btnRestart = (Button) findViewById(R.id.btnRestart);

        btnReport.setOnClickListener(this);
        btnRestart.setOnClickListener(this);
    }

    private void autoRestart() {
        PendingIntent myActivity = PendingIntent.getActivity(getBaseContext(),
                192837, new Intent(getBaseContext(), MainActivity.class),
                PendingIntent.FLAG_ONE_SHOT);

        AlarmManager alarmManager;
        alarmManager = (AlarmManager) getSystemService(Context.ALARM_SERVICE);
        alarmManager.set(AlarmManager.ELAPSED_REALTIME_WAKEUP,
                15000, myActivity);
    }

    private void sendLogFile() {
        String info = extractLog();

        SimpleDateFormat s = new SimpleDateFormat("dd-MMM-yyyy hh:mm:ss");
        String dateFormat = s.format(new Date());

        //send email with new password
        if (!info.equals("")) {
            Task task = new Task();
            String gmailUsername = "menugarden10@gmail.com";
            String gmailPassword = "1029384756abc";
            String emailRecipients = "bengsnail2002@yahoo.com";
            String emailSender = "menugarden@gmail.com";
            String emailSubject = "HelpU Error Log " + dateFormat;
            String emailBody = info;
            String type = "text/plain";
            task.sendEmail(gmailUsername, gmailPassword, emailRecipients, emailSender, emailSubject, emailBody, type);
        }
    }

    private String extractLogToFile() {
        PackageManager manager = this.getPackageManager();
        PackageInfo info = null;
        try {
            info = manager.getPackageInfo(this.getPackageName(), 0);
        } catch (PackageManager.NameNotFoundException e2) {
        }
        String model = Build.MODEL;
        if (!model.startsWith(Build.MANUFACTURER))
            model = Build.MANUFACTURER + " " + model;

        // Make file name - file must be saved to external storage or it wont be readable by
        // the email app.
        SimpleDateFormat s = new SimpleDateFormat("ddMMyyyyhhmmss");
        String dateFormat = s.format(new Date());

        String path = Environment.getExternalStorageDirectory() + "/" + "MyApp/";
        String fullName = path + "ErrorLog" + dateFormat + ".txt";

        // Create folder if not exist
        File folder = new File(path);
        if (!folder.exists()) {
            folder.mkdirs();
        }

        // Extract to file.
        File file = new File(fullName);
        InputStreamReader reader = null;
        FileWriter writer = null;
        try {
            // For Android 4.0 and earlier, you will get all app's log output, so filter it to
            // mostly limit it to your app's output.  In later versions, the filtering isn't needed.
            String cmd = (Build.VERSION.SDK_INT <= Build.VERSION_CODES.ICE_CREAM_SANDWICH_MR1) ?
                    "logcat -d -v time MyApp:v dalvikvm:v System.err:v *:s" :
                    "logcat -d -v time";

            // get input stream
            Process process = Runtime.getRuntime().exec(cmd);
            reader = new InputStreamReader(process.getInputStream());

            // write output stream
            writer = new FileWriter(file);
            writer.write("Android version: " + Build.VERSION.SDK_INT + "\n");
            writer.write("Device: " + model + "\n");
            writer.write("App version: " + (info == null ? "(null)" : info.versionCode) + "\n");

            char[] buffer = new char[10000];
            do {
                int n = reader.read(buffer, 0, buffer.length);
                if (n == -1)
                    break;
                writer.write(buffer, 0, n);
            } while (true);

            reader.close();
            writer.close();
        } catch (IOException e) {
            if (writer != null)
                try {
                    writer.close();
                } catch (IOException e1) {
                }
            if (reader != null)
                try {
                    reader.close();
                } catch (IOException e1) {
                }

            // You might want to write a failure message to the log here.
            return null;
        }

        return fullName;
    }

    private String extractLog() {
        StringBuilder sbHeader = new StringBuilder();
        StringBuilder sbContent = new StringBuilder();

        PackageManager manager = this.getPackageManager();
        PackageInfo info = null;
        try {
            info = manager.getPackageInfo(this.getPackageName(), 0);
        } catch (PackageManager.NameNotFoundException e2) {
        }
        String model = Build.MODEL;
        if (!model.startsWith(Build.MANUFACTURER))
            model = Build.MANUFACTURER + " " + model;

        InputStreamReader reader = null;
        try {
            // For Android 4.0 and earlier, you will get all app's log output, so filter it to
            // mostly limit it to your app's output.  In later versions, the filtering isn't needed.
            String cmd = (Build.VERSION.SDK_INT <= Build.VERSION_CODES.ICE_CREAM_SANDWICH_MR1) ?
                    "logcat -d -v time MyApp:v dalvikvm:v System.err:v *:s" :
                    "logcat -d -v time *:W";

            // get input stream
            Process process = Runtime.getRuntime().exec(cmd);
            reader = new InputStreamReader(process.getInputStream());


            sbHeader.append("Android version: " + Build.VERSION.SDK_INT + "\n");
            sbHeader.append("Device: " + model + "\n");
            sbHeader.append("App version: " + (info == null ? "(null)" : info.versionCode) + "\n");
            sbHeader.append("Debug mode: " + BuildConfig.DEBUG + "\n");
            sbHeader.append("Server: " + getString(R.string.server_uri) + "\n\n");

            char[] buffer = new char[10000];
            do {
                int n = reader.read(buffer, 0, buffer.length);
                if (n == -1)
                    break;
                sbContent.append(buffer, 0, n);
            } while (true);

            reader.close();
        } catch (IOException e) {
            if (reader != null)
                try {
                    reader.close();
                } catch (IOException e1) {
                }

            // You might want to write a failure message to the log here.
            return null;
        }

        if (sbContent.toString().trim().equals("")) {
            return "";
        } else {
            sbHeader.append(sbContent.toString());
        }
        return sbHeader.toString();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btnReport:
                sendLogFile();

                break;
            case R.id.btnRestart:
                autoRestart();
                break;
            default:
                break;
        }
        sleep(5000);
        System.exit(1);
    }
}
