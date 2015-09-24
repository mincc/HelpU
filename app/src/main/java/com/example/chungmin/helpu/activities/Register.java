package com.example.chungmin.helpu.activities;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.example.chungmin.helpu.callback.GetBooleanCallback;
import com.example.chungmin.helpu.callback.GetUserCallback;
import com.example.chungmin.helpu.models.Globals;
import com.example.chungmin.helpu.R;
import com.example.chungmin.helpu.models.User;
import com.example.chungmin.helpu.serverrequest.UserManager;

import HelpUGenericUtilities.ValidationUtils;


public class Register extends HelpUBaseActivity implements View.OnClickListener, View.OnFocusChangeListener {

    private EditText etName, etUsername, etPassword, etRetypePassword, etUserContact, etUserEmail;
    private Button btnRegister;

    private String strNameEmpty;
    private String strUsernameAlreadyExists;
    private String strUsernameEmpty;
    private String strPasswordEmpty;
    private String strPasswordMismatch;
    private String strPasswordTooShort;
    private String strUserContactEmpty;
    private String strUserEmailEmpty;
    private String strUserEmailInvalid;

    private boolean mIsNameValid = false;
    private boolean mIsUsernameValid = false;
    private boolean mIsPasswordValid = false;
    private boolean mIsUserContactValid = false;
    private boolean mIsUserEmailValid = false;
    private boolean mIsValid = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        strNameEmpty = getString(R.string.strNameEmpty);
        strUsernameAlreadyExists = getString(R.string.strUsernameAlreadyExists);
        strUsernameEmpty = getString(R.string.strUsernameEmpty);
        strPasswordEmpty = getString(R.string.strPasswordEmpty);
        strPasswordMismatch = getString(R.string.strPasswordMismatch);
        strPasswordTooShort = getString(R.string.strPasswordTooShort);
        strUserContactEmpty = getString(R.string.strUserContactEmpty);
        strUserEmailEmpty = getString(R.string.strUserEmailEmpty);
        strUserEmailInvalid = getString(R.string.strUserEmailInvalid);

        etName = (EditText) findViewById(R.id.etName);
        etUsername = (EditText) findViewById(R.id.etUsername);
        etPassword = (EditText) findViewById(R.id.etPassword);
        etUserContact = (EditText) findViewById(R.id.etUserContact);
        etUserEmail = (EditText) findViewById(R.id.etUserEmail);
        btnRegister = (Button) findViewById(R.id.btnRegister);
        etRetypePassword = (EditText) findViewById(R.id.etRetypePassword);

        etName.setOnFocusChangeListener(this);
        etUsername.setOnFocusChangeListener(this);
        etPassword.setOnFocusChangeListener(this);
        etRetypePassword.setOnFocusChangeListener(this);
        etUserContact.setOnFocusChangeListener(this);
        etUserEmail.setOnFocusChangeListener(this);

        btnRegister.setOnClickListener(this);

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btnRegister:
                String name = etName.getText().toString();
                String username = etUsername.getText().toString();
                String password = etPassword.getText().toString();
                String userContact = etUserContact.getText().toString();
                String userEmail = etUserEmail.getText().toString();

                User user = new User(-1, name, username, password, userContact, userEmail);
                registerUser(user);
                break;
        }
    }

    private void registerUser(User user) {
        //recheck again if user open the activity and straight press register, so that can show the error message
        validateName();
        validateUsername();
        validatePassword();
        validateRetypePassword();
        validateUserContact();
        validateUserEmail();

        if (mIsNameValid && mIsUsernameValid && mIsPasswordValid && mIsUserEmailValid && mIsUserContactValid) {
            mIsValid = true;
        } else {
            mIsValid = false;
        }

        if (mIsValid) {
            //to prevent execute multiple time
            btnRegister.setEnabled(false);
            String url = getString(R.string.server_uri) + ((Globals) getApplication()).getUserInsertUrl();
            UserManager serverRequest = new UserManager(this);
            serverRequest.insert(user, url, new GetUserCallback() {
                @Override
                public void done(User returnedUser) {
                    Intent loginIntent = new Intent(Register.this, Login.class);
                    startActivity(loginIntent);
                    finish();
                }

                @Override
                public void fail(String msg) {

                }
            });
        }
    }

    @Override
    public void onFocusChange(View v, boolean hasFocus) {
        if (!hasFocus) {
            switch (v.getId()) {
                case R.id.etName:
                    validateName();
                    break;
                case R.id.etUsername:
                    validateUsername();
                    break;
                case R.id.etPassword:
                    validatePassword();
                    break;
                case R.id.etRetypePassword:
                    validateRetypePassword();
                    break;
                case R.id.etUserContact:
                    validateUserContact();
                    break;
                case R.id.etUserEmail:
                    validateUserEmail();
                    break;
                default:
                    showToast(getString(R.string.strUnknownAction));
                    break;
            }
        }
    }

    public void validateName() {
        if (etName.getText().toString().equals("")) {
            etName.setError(strNameEmpty);
            mIsNameValid = false;
        } else {
            mIsNameValid = true;
        }
    }

    public void validateUsername() {
        String username = etUsername.getText().toString();

        if (username.equals("")) {
            etUsername.setError(strUsernameEmpty);
            mIsUsernameValid = false;
            return;
        }

        if (!username.trim().equals("")) {
            String url = getString(R.string.server_uri) + ((Globals) getApplication()).getIsUsernameAlreadyExistsUrl();
            UserManager serverRequest = new UserManager(this);
            serverRequest.isUsernameExists(username, url, new GetBooleanCallback() {
                @Override
                public void done(Boolean isUsernameExists) {
                    if (isUsernameExists) {
                        etUsername.setError(strUsernameAlreadyExists);
                        mIsUsernameValid = false;
                    } else {
                        mIsUsernameValid = true;
                    }
                }
            });
        }
    }

    public void validatePassword() {
        String password = etPassword.getText().toString();
        if (password.equals("")) {
            etPassword.setError(strPasswordEmpty);
            mIsPasswordValid = false;
            return;
        } else {
            mIsPasswordValid = true;
        }

        if (!ValidationUtils.isValidPassword(password)) {
            etPassword.setError(strPasswordTooShort);
            mIsPasswordValid = false;
        } else {
            mIsPasswordValid = true;
        }

    }

    public void validateRetypePassword() {
        String password = etPassword.getText().toString();
        String retypePassword = etRetypePassword.getText().toString();
        if (!retypePassword.equals(password)) {
            etRetypePassword.setError(strPasswordMismatch);
            mIsPasswordValid = false;
        } else {
            mIsPasswordValid = true;
        }
    }

    public void validateUserContact() {
        if (etUserContact.getText().toString().equals("")) {
            etUserContact.setError(strUserContactEmpty);
            mIsUserContactValid = false;
        } else {
            mIsUserContactValid = true;
        }
    }

    public void validateUserEmail() {
        String email = etUserEmail.getText().toString();
        if (email.equals("")) {
            etUserEmail.setError(strUserEmailEmpty);
            mIsUserEmailValid = false;
            return;
        } else {
            mIsUserEmailValid = true;
        }

        if (!ValidationUtils.isValidEmail(email)) {
            etUserEmail.setError(strUserEmailInvalid);
            mIsUserEmailValid = false;
        } else {
            mIsUserEmailValid = true;
        }
    }
}
