package com.example.chungmin.helpu.activities;

import android.app.AlertDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.chungmin.helpu.callback.Callback;
import com.example.chungmin.helpu.models.Globals;
import com.example.chungmin.helpu.R;
import com.example.chungmin.helpu.models.User;
import com.example.chungmin.helpu.models.UserLocalStore;
import com.example.chungmin.helpu.serverrequest.UserManager;


public class Login extends ActionBarActivity implements View.OnClickListener, View.OnFocusChangeListener {

    private Button btnLogin;
    private TextView tvRegisterLink, tvForgetPassword;
    private EditText etUsername, etPassword;
    private UserLocalStore userLocalStore;
    private String strInputUsername;
    private String strInputPassword;
    private boolean mIsUsernameValid = false;
    private boolean mIsPasswordValid = false;
    boolean mIsValid = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        strInputUsername = getString(R.string.strInputUsername);
        strInputPassword = getString(R.string.strInputPassword);

        btnLogin = (Button) findViewById(R.id.btnLogin);
        etUsername = (EditText) findViewById(R.id.etUsername);
        etPassword = (EditText) findViewById(R.id.etPassword);
        tvRegisterLink = (TextView) findViewById(R.id.tvRegisterLink);
        tvForgetPassword = (TextView) findViewById(R.id.tvForgetPassword);

        btnLogin.setOnClickListener(this);
        tvRegisterLink.setOnClickListener(this);
        tvForgetPassword.setOnClickListener(this);

        etUsername.setOnFocusChangeListener(this);
        etPassword.setOnFocusChangeListener(this);

        userLocalStore = new UserLocalStore(this);
    }

    @Override
    public void onClick(View view) {
        Intent redirect;
        switch (view.getId()) {
            case R.id.btnLogin:
                String username = etUsername.getText().toString();
                String password = etPassword.getText().toString();

                User user = new User(username, password);

                validateUsername();
                validatePassword();
                if (mIsUsernameValid && mIsPasswordValid) {
                    mIsValid = true;
                } else {
                    mIsValid = false;
                }

                if (mIsValid) {
                    authenticate(user);
                }

                break;
            case R.id.tvRegisterLink:
                redirect = new Intent(Login.this, Register.class);
                startActivity(redirect);
                finish();
                break;
            case R.id.tvForgetPassword:
                redirect = new Intent(Login.this, AccountRecoveryActivity.class);
                startActivity(redirect);
                finish();
                break;
        }
    }

    private void authenticate(User user) {
        UserManager serverRequest = new UserManager(this);
        serverRequest.login(user, new Callback.GetUserCallback() {
            @Override
            public void complete(User returnedUser) {
                if (returnedUser != null) {
                    logUserIn(returnedUser);
                    ((Globals) getApplication()).setIsAdmin(returnedUser.getIsAdmin());
                }
            }

            @Override
            public void failure(String msg) {
                msg = ((Globals) getApplication()).translateErrorType(msg);
                showErrorMessage(msg);
            }
        });

    }

    private void showErrorMessage(String msg) {
        AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(Login.this);
        dialogBuilder.setMessage(msg);
        dialogBuilder.setPositiveButton(android.R.string.ok, null);
        dialogBuilder.show();
    }

    private void logUserIn(User returnedUser) {
        userLocalStore.storeUserData(returnedUser);
        userLocalStore.setUserLoggedIn(true);
        startActivity(new Intent(this, MainActivity.class));
        finish();
    }

    public void validateUsername() {
        if (etUsername.getText().toString().equals("")) {
            etUsername.setError(strInputUsername);
            mIsUsernameValid = false;
        } else {
            mIsUsernameValid = true;
        }
    }

    public void validatePassword() {
        if (etPassword.getText().toString().equals("")) {
            etPassword.setError(strInputPassword);
            mIsPasswordValid = false;
        } else {
            mIsPasswordValid = true;
        }
    }

    @Override
    public void onFocusChange(View v, boolean hasFocus) {
        if (!hasFocus) {
            switch (v.getId()) {
                case R.id.etUsername:
                    validateUsername();
                    break;
                case R.id.etPassword:
                    validatePassword();
                    break;
                default:
                    Toast.makeText(this, getString(R.string.strUnknownAction), Toast.LENGTH_LONG).show();
                    break;
            }
        }
    }
}
