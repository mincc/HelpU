package com.example.chungmin.helpu;

import android.app.AlertDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;


public class Login extends ActionBarActivity implements View.OnClickListener{

    Button bLogin;
    TextView registerLink;
    EditText etUsername, etPassword;

    UserLocalStore userLocalStore;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        bLogin = (Button) findViewById(R.id.bLogin);
        etUsername = (EditText) findViewById(R.id.etUsername);
        etPassword = (EditText) findViewById(R.id.etPassword);
        registerLink = (TextView) findViewById(R.id.tvRegisterLink);

        bLogin.setOnClickListener(this);
        registerLink.setOnClickListener(this);

        userLocalStore = new UserLocalStore(this);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.bLogin:
                String username = etUsername.getText().toString();
                String password = etPassword.getText().toString();

                User user = new User(username, password);

                authenticate(user);
                break;
            case R.id.tvRegisterLink:
                Intent registerIntent = new Intent(Login.this, Register.class);
                startActivity(registerIntent);
                finish();
                break;
        }
    }

    private void authenticate(User user) {
        String url = getString(R.string.server_uri) + ((Globals)getApplication()).getUserGetByUsernameAndPassword();
        UserServerRequests serverRequest = new UserServerRequests(this);
        serverRequest.fetchUserDataAsyncTask(user, url, new GetUserCallback() {
            @Override
            public void done(User returnedUser) {
                if (returnedUser == null) {
                    showErrorMessage();
                } else {
                    logUserIn(returnedUser);
                }
            }
        });
    }

    private void showErrorMessage() {
        AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(Login.this);
        dialogBuilder.setMessage("Incorrect user details");
        dialogBuilder.setPositiveButton("Ok", null);
        dialogBuilder.show();
    }

    private void logUserIn(User returnedUser) {
        userLocalStore.storeUserData(returnedUser);
        userLocalStore.setUserLoggedIn(true);
        startActivity(new Intent(this, MainActivity.class));
        finish();
    }
}
