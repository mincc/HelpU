package com.example.chungmin.helpu.task;

import android.os.AsyncTask;
import android.util.Log;

import com.example.chungmin.helpu.BuildConfig;

import javax.mail.AuthenticationFailedException;
import javax.mail.MessagingException;

import HelpUGenericUtilities.GMailSender;

/**
 * Created by Chung Min on 9/22/2015.
 */
public class Task {

    public Task() {
    }

    public void sendEmail(String gmailUsername, String gmailPassword, String emailRecipients,
                          String emailSender, String emailSubject, String emailBody) {
        new SendEmailAsyncTask(gmailUsername, gmailPassword, emailRecipients, emailSender, emailSubject, emailBody).execute();
    }

    class SendEmailAsyncTask extends AsyncTask<Void, Void, Boolean> {

        private GMailSender sender;
        private String gmailUsername;
        private String gmailPassword;
        private String emailRecipients;
        private String emailSender;
        private String emailSubject;
        private String emailBody;

        public SendEmailAsyncTask(String gmailUsername, String gmailPassword, String emailRecipients,
                                  String emailSender, String emailSubject, String emailBody) {
            this.gmailUsername = gmailUsername;
            this.gmailPassword = gmailPassword;
            this.emailRecipients = emailRecipients;
            this.emailSender = emailSender;
            this.emailSubject = emailSubject;
            this.emailBody = emailBody;
        }

        @Override
        protected Boolean doInBackground(Void... params) {
            if (BuildConfig.DEBUG)
                Log.v(SendEmailAsyncTask.class.getName(), "doInBackground()");
            try {
                sender = new GMailSender(gmailUsername, gmailPassword);
//                        sender.addAttachment("/storage/emulated/0/Samsung/Image/010.jpg","My Picture");
                sender.sendMail(emailSubject, emailBody, emailSender, emailRecipients);
                return true;
            } catch (AuthenticationFailedException e) {
                //need to set the account to https://www.google.com/settings/security/lesssecureapps "TURN OFF
                Log.e(SendEmailAsyncTask.class.getName(), "Bad account details");
                e.printStackTrace();
                return false;
            } catch (MessagingException e) {
                Log.e(SendEmailAsyncTask.class.getName(), emailRecipients + "failed");
                e.printStackTrace();
                return false;
            } catch (Exception e) {
                e.printStackTrace();
                return false;
            }
        }
    }
}
