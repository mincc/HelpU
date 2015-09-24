package com.example.chungmin.helpu.activities;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.example.chungmin.helpu.R;
import com.example.chungmin.helpu.callback.GetUserCallback;
import com.example.chungmin.helpu.models.Globals;
import com.example.chungmin.helpu.models.User;
import com.example.chungmin.helpu.serverrequest.UserManager;
import com.example.chungmin.helpu.task.Task;

import java.util.Random;

import HelpUGenericUtilities.ValidationUtils;

public class AccountRecoveryActivity extends AppCompatActivity implements View.OnClickListener {

    private Button btnSendEmail;
    private EditText etEmailAddress;
    private String strEmailEmpty;
    private String strEmailInvalid;
    private String strEmailNotExist;
    private boolean mIsUserEmailValid = false;
    private String mEmail;
    private User mUser;
    private String mNewPassword;
    private TextView tvMessageBox;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_account_recovery);

        strEmailEmpty = getString(R.string.strEmailEmpty);
        strEmailInvalid = getString(R.string.strEmailInvalid);
        strEmailNotExist = getString(R.string.strEmailNotExist);

        etEmailAddress = (EditText) findViewById(R.id.etEmailAddress);
        btnSendEmail = (Button) findViewById(R.id.btnSendEmail);
        tvMessageBox = (TextView) findViewById(R.id.tvMessageBox);

        btnSendEmail.setOnClickListener(this);
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btnSendEmail:
                validateEmail();
                if (mIsUserEmailValid) {
                    //generate random password
                    mNewPassword = generateRandomPassword(8);

                    //update the new user password
                    updateNewPassword(mEmail, mNewPassword, this);
                }
                break;
            default:
                break;
        }
    }

    private String getEmailBody(User user, String newPassword) {
        String msg = "";
        msg = "<html>" +
                "   <body>" +
                "       <h4><b>Forgot your password, Mr. %1$s</b></h4>" +
                "       <p> \n" +
                "           HelpU received a request to reset the password for your HelpU account. <br/>" +
                "           The user detail as below : <br/><br/>" +
                "           username : %2$s<br/>" +
                "           Password : %3$s <br/>" +
                "       </p>" +
                "       <p>" +
                "           <br/><br/>Best regards,<br/>" +
                "           <b><i>HelpU Support</i></b>" +
                "       </p>" +
                "   </body>" +
                "</html>";
        msg = String.format(msg, user.getUserName(), user.getUsername(), newPassword);
        return msg;
    }

    private void updateNewPassword(String email, final String newPassword, final AccountRecoveryActivity accountRecoveryActivity) {
        String url = getString(R.string.server_uri) + ((Globals) getApplication()).getUserUpdatePasswordByEmailUrl();
        UserManager serverRequest = new UserManager(this);
        serverRequest.updatePasswordByEmail(email, newPassword, url, new GetUserCallback() {
            @Override
            public void done(User returnedUser) {
                if (returnedUser != null) {
                    mUser = returnedUser;

                    //send email with new password
                    Task task = new Task();
                    String gmailUsername = "menugarden10@gmail.com";
                    String gmailPassword = "1029384756abc";
                    String emailRecipients = mEmail;
                    String emailSender = "menugarden@gmail.com";
                    String emailSubject = getString(R.string.strResetPassword);
                    String emailBody = getEmailBody(mUser, mNewPassword);
                    task.sendEmail(gmailUsername, gmailPassword, emailRecipients, emailSender, emailSubject, emailBody);

                    tvMessageBox.setText(getString(R.string.strPwdEmailSend));
                    tvMessageBox.setBackgroundDrawable(getResources().getDrawable(R.drawable.green_box));
                    tvMessageBox.setVisibility(View.VISIBLE);
                    //back to main page
//                    Intent redirect = new Intent(accountRecoveryActivity, MainActivity.class);
//                    startActivity(redirect);
//                    finish();
                }
            }

            @Override
            public void fail(String msg) {
                msg = ((Globals) getApplication()).translateErrorType(msg);
                tvMessageBox.setText(msg);
                tvMessageBox.setBackgroundDrawable(getResources().getDrawable(R.drawable.red_box));
                tvMessageBox.setVisibility(View.VISIBLE);
            }

        });
    }

    private String generateRandomPassword(int length) {
        String alphabet = new String("0123456789ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz"); //9
        int n = alphabet.length(); //10

        String result = new String();
        Random r = new Random(); //11

        for (int i = 0; i < length; i++) //12
            result = result + alphabet.charAt(r.nextInt(n)); //13

        return result;
    }

    public void validateEmail() {
        mEmail = etEmailAddress.getText().toString();
        if (mEmail.equals("")) {
            etEmailAddress.setError(strEmailEmpty);
            mIsUserEmailValid = false;
            return;
        } else {
            mIsUserEmailValid = true;
        }

        if (!ValidationUtils.isValidEmail(mEmail)) {
            etEmailAddress.setError(strEmailInvalid);
            mIsUserEmailValid = false;
        } else {
            mIsUserEmailValid = true;
        }
    }
}
