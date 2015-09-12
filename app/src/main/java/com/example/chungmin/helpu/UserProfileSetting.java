package com.example.chungmin.helpu;

import android.content.Intent;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import HelpUGenericUtilities.ValidationUtils;


public class UserProfileSetting extends HelpUBaseActivity implements View.OnClickListener, View.OnFocusChangeListener {
    UserLocalStore userLocalStore;
    User mUser = null;
    EditText etName, etUsername, etUserContact, etUserEmail;
    Button btnSave;

    String strNameEmpty = "Name can not empty";
    String strUserContactEmpty = "Contact can not empty";
    String strUserEmailEmpty = "Email can not empty";
    String strUserEmailInvalid = "Invalid Email";

    boolean mIsNameValid = true;
    boolean mIsUserContactValid = true;
    boolean mIsUserEmailValid = true;
    boolean mIsValid = true;    //all the info is true until user start change

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_profile_setting);

        strNameEmpty = getString(R.string.strNameEmpty);
        strUserContactEmpty = getString(R.string.strUserContactEmpty);
        strUserEmailEmpty = getString(R.string.strUserEmailEmpty);
        strUserEmailInvalid = getString(R.string.strUserEmailInvalid);

        etUsername = (EditText) findViewById(R.id.etUsername);
        etName = (EditText) findViewById(R.id.etName);
        etUserContact = (EditText) findViewById(R.id.etUserContact);
        etUserEmail = (EditText) findViewById(R.id.etUserEmail);
        btnSave = (Button) findViewById(R.id.btnSave);

        etName.setOnFocusChangeListener(this);
        etUsername.setOnFocusChangeListener(this);
        etUserContact.setOnFocusChangeListener(this);
        etUserEmail.setOnFocusChangeListener(this);

        userLocalStore = new UserLocalStore(this);
        displayUserDetails();

        btnSave.setOnClickListener(this);
    }

    private void displayUserDetails() {
        mUser = userLocalStore.getLoggedInUser();
        etUsername.setText(mUser.getUsername());
        etName.setText(mUser.getUserName());
        etUserContact.setText(mUser.getUserContact());
        etUserEmail.setText(mUser.getUserEmail());
    }

    @Override
    public void onClick(View v) {
        if (v == btnSave) {

            //Validation
            validateName();
            validateUserContact();
            validateUserEmail();

            if (mIsNameValid && mIsUserEmailValid && mIsUserContactValid) {
                mIsValid = true;
            } else {
                mIsValid = false;
            }

            if (mIsValid) {
                mUser.setUserName(etName.getText().toString());
                mUser.setUserContact(etUserContact.getText().toString());
                mUser.setUserEmail(etUserEmail.getText().toString());
                updateUser(mUser);
            }
        }
    }

    private void updateUser(final User user) {
        String url = getString(R.string.server_uri) + ((Globals) getApplication()).getUserUpdate();
        UserServerRequests serverRequest = new UserServerRequests(this);
        serverRequest.getUserUpdate(user, url, new GetUserCallback() {
            @Override
            public void done(User returnedUser) {
                userLocalStore.storeUserData(user);
                Intent redirect = new Intent(UserProfileSetting.this, MainActivity.class);
                redirect.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(redirect);
                finish();
            }
        });
    }

    @Override
    public void onFocusChange(View v, boolean hasFocus) {
        if (!hasFocus) {
            switch (v.getId()) {
                case R.id.etUsername:
                    //not allow user to change the username
                    break;
                case R.id.etName:
                    validateName();
                    break;
                case R.id.etUserContact:
                    validateUserContact();
                    break;
                case R.id.etUserEmail:
                    validateUserEmail();
                    break;
                default:
                    String message = "Still not define yet";
                    showToast(message);
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
