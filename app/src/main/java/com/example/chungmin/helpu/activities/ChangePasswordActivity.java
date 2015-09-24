package com.example.chungmin.helpu.activities;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.example.chungmin.helpu.R;
import com.example.chungmin.helpu.callback.GetBooleanCallback;
import com.example.chungmin.helpu.models.Globals;
import com.example.chungmin.helpu.serverrequest.UserManager;

import HelpUGenericUtilities.ValidationUtils;

public class ChangePasswordActivity extends HelpUBaseActivity implements View.OnClickListener, View.OnFocusChangeListener {

    private EditText etCurrentPassword, etNewPassword, etRetypeNewPassword;
    private Button btnCancel, btnSave;

    private String strPasswordMismatch;
    private String strPasswordEmpty;
    private String strPasswordTooShort;
    private String strInvalidPassword;
    private boolean mIsPasswordValid = false;
    private boolean mIsNewPasswordValid = false;
    private boolean mIsRetypeNewPasswordValid = false;
    private boolean mIsValid = false;
    private int mUserId;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_change_password);
        mUserId = ((Globals) getApplication()).getUserId();

        strPasswordEmpty = getString(R.string.strPasswordEmpty);
        strPasswordMismatch = getString(R.string.strPasswordMismatch);
        strPasswordTooShort = getString(R.string.strPasswordTooShort);
        strInvalidPassword = getString(R.string.strInvalidPassword);

        etCurrentPassword = (EditText) findViewById(R.id.etCurrentPassword);
        etNewPassword = (EditText) findViewById(R.id.etNewPassword);
        etRetypeNewPassword = (EditText) findViewById(R.id.etRetypeNewPassword);
        btnCancel = (Button) findViewById(R.id.btnCancel);
        btnSave = (Button) findViewById(R.id.btnSave);

        etCurrentPassword.setOnFocusChangeListener(this);
        etNewPassword.setOnFocusChangeListener(this);
        etRetypeNewPassword.setOnFocusChangeListener(this);

        btnCancel.setOnClickListener(this);
        btnSave.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        Intent redirect;
        switch (v.getId()) {
            case R.id.btnCancel:
                redirect = new Intent(this, MainActivity.class);
                startActivity(redirect);
                finish();
                break;
            case R.id.btnSave:
                changePassword();
                break;
            default:
                showToast(getString(R.string.strUnknownAction));
        }
    }

    private void changePassword() {
        validateCurrentPassword();
        validatePassword();
        validateRetypePassword();

        if (mIsPasswordValid && mIsNewPasswordValid && mIsRetypeNewPasswordValid) {
            mIsValid = true;
        } else {
            mIsValid = false;
        }

        String currentPassword = etCurrentPassword.getText().toString();
        String newPassword = etNewPassword.getText().toString();

        if (mIsValid) {
            //to prevent execute multiple time
            btnSave.setEnabled(false);
            String url = getString(R.string.server_uri) + ((Globals) getApplication()).getUserPasswordUpdateUrl();
            UserManager serverRequest = new UserManager(this);
            serverRequest.updatePassword(mUserId, currentPassword, newPassword, url, new GetBooleanCallback() {

                @Override
                public void done(Boolean isTrigger) {
                    Intent loginIntent = new Intent(ChangePasswordActivity.this, MainActivity.class);
                    startActivity(loginIntent);
                    finish();
                }
            });
        }
    }

    private void validateCurrentPassword() {
        String password = etCurrentPassword.getText().toString();
        String url = getString(R.string.server_uri) + ((Globals) getApplication()).getUserIsCurrentPasswordValidUrl();
        UserManager serverRequest = new UserManager(this);
        serverRequest.isCurrentPasswordValid(mUserId, password, url, new GetBooleanCallback() {
            @Override
            public void done(Boolean isValid) {
                if (isValid) {
                    mIsPasswordValid = true;
                } else {
                    etCurrentPassword.setError(strInvalidPassword);
                    mIsPasswordValid = false;
                }
            }
        });
    }

    public void validatePassword() {
        String password = etNewPassword.getText().toString();
        if (password.equals("")) {
            etNewPassword.setError(strPasswordEmpty);
            mIsNewPasswordValid = false;
            return;
        } else {
            mIsNewPasswordValid = true;
        }

        if (!ValidationUtils.isValidPassword(password)) {
            etNewPassword.setError(strPasswordTooShort);
            mIsNewPasswordValid = false;
        } else {
            mIsNewPasswordValid = true;
        }

    }

    public void validateRetypePassword() {
        String password = etNewPassword.getText().toString();
        String retypePassword = etRetypeNewPassword.getText().toString();
        if (!retypePassword.equals(password)) {
            etRetypeNewPassword.setError(strPasswordMismatch);
            mIsRetypeNewPasswordValid = false;
        } else {
            mIsRetypeNewPasswordValid = true;
        }
    }

    @Override
    public void onFocusChange(View v, boolean hasFocus) {
        if (!hasFocus) {
            switch (v.getId()) {
                case R.id.etCurrentPassword:
                    validateCurrentPassword();
                    break;
                case R.id.etNewPassword:
                    validatePassword();
                    break;
                case R.id.etRetypeNewPassword:
                    validateRetypePassword();
                    break;
                default:
                    showToast(getString(R.string.strUnknownAction));
                    break;
            }
        }
    }

}
