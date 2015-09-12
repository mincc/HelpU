package com.example.chungmin.helpu;

import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import java.text.SimpleDateFormat;
import java.util.Date;

import javax.mail.AuthenticationFailedException;
import javax.mail.MessagingException;

import HelpUGenericUtilities.GMailSender;


public class SendEmail extends HelpUBaseActivity {
    EditText etSender, etRecipients, etSubject, etBody, etGmailAccountUsername, etGmailAccountPassword;

    String gmailUsername = "yourGmailAccount";
    String gmailPassword = "yourGmailPassword";
    String emailSender = "menugarden@gmail.com";
    String emailRecipients = "bengsnail2002@yahoo.com";
    String emailSubject = "Send Email Testing";
    String emailBody = "Hello World!!";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_send_email);

        etSender = (EditText) findViewById(R.id.etSender);
        etRecipients = (EditText) findViewById(R.id.etRecipients);
        etSubject = (EditText) findViewById(R.id.etSubject);
        etBody = (EditText) findViewById(R.id.etBody);
        etGmailAccountUsername = (EditText) findViewById(R.id.etGmailAccountUsername);
        etGmailAccountPassword = (EditText) findViewById(R.id.etGmailAccountPassword);

        SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd_HHmmss");
        String currentDateandTime = sdf.format(new Date());


        etSender.setText(emailSender);
        etRecipients.setText(emailRecipients);
        etSubject.setText(emailSubject + " " + currentDateandTime);
        etBody.setText(emailBody);
        etGmailAccountUsername.setText(gmailUsername);
        etGmailAccountPassword.setText(gmailPassword);

        final Button btnSend = (Button) this.findViewById(R.id.btnSend);
        btnSend.setOnClickListener(new View.OnClickListener() {

            public void onClick(View v) {
                gmailUsername = etGmailAccountUsername.getText().toString();
                gmailPassword = etGmailAccountPassword.getText().toString();
                emailSender = etSender.getText().toString();
                emailRecipients = etRecipients.getText().toString();
                emailSubject = etSubject.getText().toString();
                emailBody = etBody.getText().toString();

                new SendEmailAsyncTask().execute();
            }

            class SendEmailAsyncTask extends AsyncTask<Void, Void, Boolean> {

                GMailSender sender;

                public SendEmailAsyncTask() {
                }

                @Override
                protected Boolean doInBackground(Void... params) {
                    if (BuildConfig.DEBUG)
                        Log.v(SendEmailAsyncTask.class.getName(), "doInBackground()");
                    try {
                        sender = new GMailSender(gmailUsername, gmailPassword);
//                        sender.addAttachment("/storage/emulated/0/Samsung/Image/010.jpg","My Picture");
                        sender.sendMail(emailSubject,
                                emailBody,
                                emailSender,
                                emailRecipients);
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
        });

    }
}


